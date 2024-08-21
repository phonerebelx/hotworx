package com.hotworx.ui.fragments.HotsquadList.Bottomsheet

import android.app.Dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.hotworx.Singletons.ApiHeaderSingleton
import com.hotworx.activities.DockActivity
import com.hotworx.databinding.BottomSheetSearchuserBinding
import com.hotworx.global.Constants
import com.hotworx.models.ErrorResponseEnt
import com.hotworx.models.HotsquadList.FoundUser
import com.hotworx.models.HotsquadList.NotFoundUser
import com.hotworx.models.HotsquadList.ReferralInviteModel
import com.hotworx.models.HotsquadList.SearchUserModel
import com.hotworx.models.HotsquadList.SendMemberInviteModel
import com.hotworx.models.HotsquadList.sendMemberInvitationRequest
import com.hotworx.models.HotsquadList.sendReferralInvitationRequest
import com.hotworx.retrofit.GsonFactory
import com.hotworx.ui.adapters.HotsquadListAdapter.SearchNotFoundUserAdapter
import com.hotworx.ui.adapters.HotsquadListAdapter.SearchRegisteredAdapter
import com.hotworx.ui.dialog.NewActivityDialog.ReferSquadInviteDialogFragment
import com.hotworx.ui.fragments.BaseBottomsheetFragment

class SearchUserBottomSheet(): BaseBottomsheetFragment(){

    private lateinit var binding: BottomSheetSearchuserBinding
    private var foundUserListForServer = mutableListOf<String>()
    private var notfoundUserListForServer = mutableListOf<String>()
    protected var myDockActivity: DockActivity? = null
    var squadID = ""
    var referralUrl = ""
    lateinit var  referSquadInviteDialogFragment: ReferSquadInviteDialogFragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetSearchuserBinding.inflate(layoutInflater)
        return binding.root
    }
    companion object {
        const val TAG = "SearchUserBottomSheet"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val responseString = arguments?.getString("response")
        squadID = arguments?.getString("squad_id")?: ""

        if (responseString != null) {
            val response = GsonFactory.getConfiguredGson()?.fromJson(responseString, SearchUserModel::class.java)
            Log.d("ParsedResponse", "Parsed Response: $response")

            if (response?.status == true) {
                val foundUserList = response.data?.foundUser ?: emptyList()
                val notFoundUserList = response.data?.notFoundUser ?: emptyList()
                referralUrl = response.data?.referralUrl?: ""
                setFoundUserAdapter(foundUserList)
                setNotFoundUserAdapter(notFoundUserList)
            } else {
                Log.e("Error", "Response status is false or null")
            }
        } else {
            Log.e("Error", "Response String is null")
        }

        referSquadInviteDialogFragment = ReferSquadInviteDialogFragment.newInstance(referralUrl)

        binding.SendInvite.setOnClickListener(View.OnClickListener {
            callInvitationApi(Constants.SEND_MEMBER_INVITATION,"")
        })

        binding.referralInvite.setOnClickListener(View.OnClickListener {
            callInvitationApi(Constants.SEND_REFERRAL_INVITATION,"")
        })
    }

    override fun onFailureWithResponseCode(code: Int, message: String, tag: String) {
        TODO("Not yet implemented")
    }

    private fun setNotFoundUserAdapter(notFoundUserList: List<NotFoundUser>) {
        val adapter = SearchNotFoundUserAdapter(notFoundUserList, requireContext(), object : SearchNotFoundUserAdapter.OnItemClickListener {
            override fun onItemClick(item: NotFoundUser) {
                item?.let {
                    if (item.selected) {
                        notfoundUserListForServer.add(it.referralInviteId)
                    } else {
                        notfoundUserListForServer.remove(it.referralInviteId)
                    }
                    Log.d(TAG, "notfoundUserList ${notfoundUserListForServer.toString()}")
                }
            }
        })

        binding.recyclerViewContact.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewContact.adapter = adapter
    }

    private fun setFoundUserAdapter(foundUserList: List<FoundUser>) {
        val adapter = SearchRegisteredAdapter(foundUserList, requireContext(), object : SearchRegisteredAdapter.OnItemClickListener {
            override fun onItemClick(item: FoundUser) {
                item?.let {
                    if (item.selected) {
                        binding.SendInvite.visibility = View.VISIBLE
                        foundUserListForServer.add(it.squadInviteId)
                    } else {
                        foundUserListForServer.remove(it.squadInviteId)
                    }
                    Log.d(TAG, "foundUserList ${foundUserListForServer.toString()}")
                }
            }
        })

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    private fun callInvitationApi(type: String, data: String) {
        when (type) {
            Constants.SEND_MEMBER_INVITATION -> {
                val request = sendMemberInvitationRequest(
                    squadID,       // Your squad ID
                    squad_invite_list = foundUserListForServer  // Your list of search strings
                )

                getServiceHelpers()?.enqueueCallExtended(
                    getWebServices()?.sendSquadMemberInvitation(
                        ApiHeaderSingleton.apiHeader(requireContext()),
                        request
                    ), Constants.SEND_MEMBER_INVITATION, true
                )
            }

            Constants.SEND_REFERRAL_INVITATION -> {
                val request = sendReferralInvitationRequest(
                    squadID,       // Your squad ID
                    squad_referral_invite_list = notfoundUserListForServer  // Your list of search strings
                )

                getServiceHelpers()?.enqueueCallExtended(
                    getWebServices()?.sendReferralInvitation(
                        ApiHeaderSingleton.apiHeader(requireContext()),
                        request
                    ), Constants.SEND_REFERRAL_INVITATION, true
                )
            }
        }
    }

    override fun onSuccess(liveData: LiveData<String>, tag: String) {
        super.onSuccess(liveData, tag)
        when (tag) {
            Constants.SEND_MEMBER_INVITATION -> {
                val responseJson = liveData.value
                Log.d("Response", "LiveData value: $responseJson")

                if (responseJson != null) {
                    try {
                        val response = GsonFactory.getConfiguredGson()?.fromJson(responseJson, SendMemberInviteModel::class.java)!!
                        if (response.status) {
                            binding.tvNoListFound.visibility = View.VISIBLE
                            binding.recyclerView.visibility = View.GONE
                        } else {
                            dockActivity?.showErrorMessage("Something Went Wrong")
                        }
                    } catch (e: Exception) {
                        val genericMsgResponse = GsonFactory.getConfiguredGson()
                            ?.fromJson(responseJson, ErrorResponseEnt::class.java)!!
                        dockActivity?.showErrorMessage(genericMsgResponse.error.toString())
                        Log.i("Error", e.message.toString())
                    }
                } else {
                    Log.e("Error", "LiveData value is null")
                    dockActivity?.showErrorMessage("No response from server")
                }
            }

            Constants.SEND_REFERRAL_INVITATION -> {
                val responseJson = liveData.value
                Log.d("Response", "LiveData value: $responseJson")

                if (responseJson != null) {
                    try {
                        val response = GsonFactory.getConfiguredGson()?.fromJson(responseJson, ReferralInviteModel::class.java)!!
                        if (response.status) {
//                            val referSquadInviteFragment = ReferSquadInviteFragment()
//                            dockActivity?.replaceDockableFragment(referSquadInviteFragment)
                            initRedeemInfo()
                        } else {
                            dockActivity?.showErrorMessage("Something Went Wrong")
                        }
                    } catch (e: Exception) {
                        val genericMsgResponse = GsonFactory.getConfiguredGson()
                            ?.fromJson(responseJson, ErrorResponseEnt::class.java)!!
                        dockActivity?.showErrorMessage(genericMsgResponse.error.toString())
                        Log.i("Error", e.message.toString())
                    }
                } else {
                    Log.e("Error", "LiveData value is null")
                    dockActivity?.showErrorMessage("No response from server")
                }
            }
        }
    }

    private fun initRedeemInfo(){
        referSquadInviteDialogFragment.show(
            childFragmentManager,
            Constants.REFERRAL_INVITATION
        )
    }

    interface OnItemClickListener {
//        fun onItemClick(item: TourTypeResponse.Type) {}
    }
}