package com.hotworx.ui.fragments.HotsquadList.Bottomsheet

import android.app.Dialog
import android.content.DialogInterface

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hotsquad.hotsquadlist.extensions.showMaterialAlertDialog
import com.hotworx.R
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
    private lateinit var foundUserList: MutableList<FoundUser>
    private lateinit var notFoundUserList: MutableList<NotFoundUser>
    protected var myDockActivity: DockActivity? = null
    var squadID = ""
    var referralUrl = ""
    lateinit var  referSquadInviteDialogFragment: ReferSquadInviteDialogFragment
    private var bottomSheetBehavior: BottomSheetBehavior<View>? = null

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
        dialog.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout = bottomSheetDialog.findViewById<View>(
                com.google.android.material.R.id.design_bottom_sheet
            )
            parentLayout?.let { bottomSheet ->
                bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet).apply {
                    state = BottomSheetBehavior.STATE_EXPANDED
                    isDraggable = false // Disable dragging
                }
                val layoutParams = bottomSheet.layoutParams
                layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
                bottomSheet.layoutParams = layoutParams
            }
        }
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val responseString = arguments?.getString("response")
        squadID = arguments?.getString("squad_id")?: ""

        if (responseString != null) {
            val response = GsonFactory.getConfiguredGson()?.fromJson(responseString, SearchUserModel::class.java)
            Log.d("ParsedResponse", "Parsed Response: $response")

            if (response?.status == true) {
                foundUserList = response.data?.foundUser?.toMutableList() ?: mutableListOf()
                notFoundUserList = response.data?.notFoundUser?.toMutableList() ?: mutableListOf()
                referralUrl = response.data?.referralUrl ?: ""
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
            if (foundUserListForServer.isNotEmpty()) {
                binding.loadingSpin.visibility = View.VISIBLE
                callInvitationApi(Constants.SEND_MEMBER_INVITATION, "")
            }
        })

        binding.referralInvite.setOnClickListener(View.OnClickListener {
            if (notFoundUserList.isNotEmpty()) {
                binding.loadingSpin.visibility = View.VISIBLE
                callInvitationApi(Constants.SEND_REFERRAL_INVITATION,"")
            }
        })

        binding.crossIcon.setOnClickListener{
            dismiss()
        }
    }

    override fun onFailureWithResponseCode(code: Int, message: String, tag: String) {
        Log.e("ResponseFailure", "Failed to load squad: $message")
        binding.tvNoListFound.text = getString(R.string.no_squad_members_found)
        binding.tvNoListFound.visibility = View.VISIBLE
    }

    private fun setNotFoundUserAdapter(notFoundUserList: List<NotFoundUser>) {
        val updatedList = notFoundUserList.map { it.copy(selected = true) }

        // Add selected items to the list
        notfoundUserListForServer.clear()
        updatedList.forEach { item ->
            if (item.selected) {
                notfoundUserListForServer.add(item.referralInviteId)
            }
        }
        Log.d(TAG, "notfoundUserList ${notfoundUserListForServer.toString()}")

        val adapter = SearchNotFoundUserAdapter(updatedList, requireContext(), object : SearchNotFoundUserAdapter.OnItemClickListener {
            override fun onItemClick(item: NotFoundUser) {
                item?.let {
                    if (item.selected) {
                        notfoundUserListForServer.add(it.referralInviteId)
                    } else {
                        notfoundUserListForServer.remove(it.referralInviteId)
                    }
                    Log.d(TAG, "notfoundUserList ${notfoundUserListForServer.toString()}")
                    updateSendReferralButtonState()
                }
            }
        })

        binding.recyclerViewContact.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewContact.adapter = adapter

        updateSendReferralButtonState()
    }

    private fun setFoundUserAdapter(foundUserList: MutableList<FoundUser>) {
//        val updatedList = foundUserList.map {
//            it.copy(selected = true)
//        }.toMutableList()
//
//        // Clear previous list and add new selected items
//        foundUserListForServer.clear()
//        updatedList.forEach { item ->
//            if (item.selected) {
//                foundUserListForServer.add(item.squadInviteId)
//            }
//        }
//        Log.d(TAG, "foundUserList ${foundUserListForServer.toString()}")

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
                    updateSendInviteButtonState()
                }
            }
        })

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        updateSendInviteButtonState()
    }

    override fun onSuccess(liveData: LiveData<String>, tag: String) {
        super.onSuccess(liveData, tag)
        when (tag) {
            Constants.SEND_MEMBER_INVITATION -> {
                binding.loadingSpin.visibility = View.VISIBLE

                val responseJson = liveData.value
                Log.d("Response", "LiveData value: $responseJson")

                if (responseJson != null) {
                    try {
                        val response = GsonFactory.getConfiguredGson()?.fromJson(responseJson, SendMemberInviteModel::class.java)!!
                        if (response.status) {
                            val iterator = foundUserList.iterator()
                            while (iterator.hasNext()) {
                                val user = iterator.next()
                                if (user.squadInviteId in foundUserListForServer) {
                                    iterator.remove()
                                }
                            }

                            foundUserListForServer.clear()

                            if (foundUserList.isEmpty()) {
                                binding.recyclerView.visibility = View.GONE
                                binding.tvNoListFound.visibility = View.VISIBLE
                            } else {
                                setFoundUserAdapter(foundUserList)
                            }

                            binding.loadingSpin.visibility = View.GONE
                        } else {
                            binding.loadingSpin.visibility = View.GONE
                            dockActivity?.showErrorMessage("Something Went Wrong")
                        }
                    } catch (e: Exception) {
                        binding.loadingSpin.visibility = View.GONE

                        val genericMsgResponse = GsonFactory.getConfiguredGson()
                            ?.fromJson(responseJson, ErrorResponseEnt::class.java)!!
                        dockActivity?.showErrorMessage(genericMsgResponse.error.toString())
                        Log.i("Error", e.message.toString())
                    }
                } else {
                    binding.loadingSpin.visibility = View.GONE
                    Log.e("Error", "LiveData value is null")
                    dockActivity?.showErrorMessage("No response from server")
                }
            }


            Constants.SEND_REFERRAL_INVITATION -> {
                binding.loadingSpin.visibility = View.VISIBLE

                val responseJson = liveData.value
                Log.d("Response", "LiveData value: $responseJson")

                if (responseJson != null) {
                    try {
                        val response = GsonFactory.getConfiguredGson()?.fromJson(responseJson, ReferralInviteModel::class.java)!!
                        if (response.status) {
                            val iterator = notFoundUserList.iterator()
                            while (iterator.hasNext()) {
                                val user = iterator.next()
                                if (user.referralInviteId in notfoundUserListForServer) {
                                    iterator.remove()
                                }
                            }

                            notfoundUserListForServer.clear()

                            if (notFoundUserList.isEmpty()) {
                                binding.recyclerViewContact.visibility = View.GONE
                                binding.tvSuccess.visibility = View.VISIBLE
                            } else {
                                setNotFoundUserAdapter(notFoundUserList)
                            }

                            binding.loadingSpin.visibility = View.GONE
                        } else {
                            binding.loadingSpin.visibility = View.GONE
                            dockActivity?.showErrorMessage("Something Went Wrong")
                        }
                    } catch (e: Exception) {
                        binding.loadingSpin.visibility = View.GONE
                        val genericMsgResponse = GsonFactory.getConfiguredGson()
                            ?.fromJson(responseJson, ErrorResponseEnt::class.java)!!
                        dockActivity?.showErrorMessage(genericMsgResponse.error.toString())
                        Log.i("Error", e.message.toString())
                    }
                } else {
                    binding.loadingSpin.visibility = View.GONE
                    Log.e("Error", "LiveData value is null")
                    dockActivity?.showErrorMessage("No response from server")
                }
            }
        }
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

    private fun updateSendInviteButtonState() {
        binding.SendInvite.isEnabled = foundUserListForServer.isNotEmpty()
    }

    private fun updateSendReferralButtonState() {
        binding.SendInvite.isEnabled = foundUserListForServer.isNotEmpty()
    }

    interface OnItemClickListener {
//        fun onItemClick(item: TourTypeResponse.Type) {}
    }
}