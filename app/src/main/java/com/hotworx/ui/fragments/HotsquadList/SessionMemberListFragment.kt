package com.hotworx.ui.fragments.HotsquadList

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.hotsquad.hotsquadlist.extensions.showMaterialAlertDialog
import com.hotsquad.hotsquadlist.listener.DialogListeners
import com.hotworx.R
import com.hotworx.Singletons.ApiHeaderSingleton
import com.hotworx.databinding.FragmentSessionMemberListBinding
import com.hotworx.databinding.FragmentSquadMemberDetailBinding
import com.hotworx.global.Constants
import com.hotworx.models.ErrorResponseEnt
import com.hotworx.models.HotsquadList.RemoveMemberResponse
import com.hotworx.models.HotsquadList.Session.SessionMemberResponse
import com.hotworx.models.HotsquadList.Session.SquadSessionMemberRequest
import com.hotworx.models.HotsquadList.SquadMemberDetailsResponse
import com.hotworx.models.HotsquadList.removeSquadMemberRequest
import com.hotworx.models.HotsquadList.squadMemberDetailRequest
import com.hotworx.retrofit.GsonFactory
import com.hotworx.ui.adapters.HotsquadListAdapter.Sessions.SessionMemberListAdapter
import com.hotworx.ui.adapters.HotsquadListAdapter.SquadMemberListAdapter
import com.hotworx.ui.fragments.BaseFragment
import com.hotworx.ui.fragments.HotsquadList.Bottomsheet.SearchUserBottomSheet.Companion.TAG
import com.hotworx.ui.views.TitleBar

class SessionMemberListFragment : BaseFragment(), SquadMemberListAdapter.OnItemClickListener {

    private var _binding: FragmentSessionMemberListBinding? = null
    private val binding get() = _binding!!
    private var adapter: SessionMemberListAdapter? = null
    private var member_ivite_id: String = ""
    private var squadId: String = ""
    private var recordId: String = ""
    private lateinit var memberListModel: SessionMemberResponse.SquadData

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSessionMemberListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve the squad ID from the fragment arguments
        arguments?.let {
            squadId = it.getString("squad_id") ?: ""
            recordId = it.getString("recordId")?: ""
            Log.d("SquadID", squadId)
            Log.d("recordId", recordId)
//            Log.d("squadAccess",squadAccess.toString())
        }

        callInvitationApi(Constants.SESSION_MEMBER, "")

        binding.tvRemove.setOnClickListener{
            callInvitationApi(Constants.REMOVE_SQUAD_MEMBER, "")
        }
    }

    override fun onItemClick(item: SquadMemberDetailsResponse.SquadData.Member) {
        // Handle member item click event here
    }

    private fun callInvitationApi(type: String, data: String) {
        when (type) {
            Constants.SESSION_MEMBER -> {
                val request = SquadSessionMemberRequest(
                    squad_id = squadId,       // Your squad ID
                    recordId
                )// Your list of search strings)

                getServiceHelper()?.enqueueCallExtended(
                    getWebService()?.getSquadMemberForSession(
                        ApiHeaderSingleton.apiHeader(requireContext()),
                        request
                    ), Constants.SESSION_MEMBER, true
                )
            }

//            Constants.REMOVE_SQUAD_MEMBER -> {
//
//                dockActivity?.showMaterialAlertDialog("Are you sure You want to remove!",object :
//                    DialogListeners {
//                    override fun onNegativeButtonTap(dialog: DialogInterface?) {
//                        dialog?.dismiss()
//                    }
//
//                    override fun onPositionButtonTap(dialog: DialogInterface?) {
//                        dialog?.dismiss()
//                        val request = removeSquadMemberRequest(
//                            squadId,       // Your squad ID
//                            userListForServer
//                        )
//
//                        getServiceHelper()?.enqueueCallExtended(
//                            getWebService()?.removeSquadMember(
//                                ApiHeaderSingleton.apiHeader(requireContext()),
//                                request
//                            ), Constants.REMOVE_SQUAD_MEMBER, true
//                        )
//                    }
//                })
//            }
        }
    }

    override fun onSuccess(liveData: LiveData<String>, tag: String) {
        super.onSuccess(liveData, tag)
        when (tag) {
            Constants.SESSION_MEMBER -> {
                val responseJson = liveData.value
                Log.d("Response", "LiveData value: $responseJson")

                if (responseJson != null) {
                    try {
                        val response = GsonFactory.getConfiguredGson()?.fromJson(responseJson, SessionMemberResponse::class.java)!!
                        if (response.status) {
                            binding.tvNoListFound.visibility = View.GONE
                            setAdapter(response.data.members)
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

//            Constants.REMOVE_SQUAD_MEMBER -> {
//                val responseJson = liveData.value
//                Log.d("Response", "LiveData value: $responseJson")
//
//                if (responseJson != null) {
//                    try {
//                        val response = GsonFactory.getConfiguredGson()?.fromJson(responseJson, RemoveMemberResponse::class.java)!!
//                        if (response.status) {
//                            dockActivity.showSuccessMessage(response.message)
//                        } else {
//
//                        }
//                    } catch (e: Exception) {
//                        val genericMsgResponse = GsonFactory.getConfiguredGson()
//                            ?.fromJson(responseJson, ErrorResponseEnt::class.java)!!
//                        dockActivity?.showErrorMessage(genericMsgResponse.error.toString())
//                        Log.i("Error", e.message.toString())
//                    }
//                } else {
//                    Log.e("Error", "LiveData value is null")
//                    dockActivity?.showErrorMessage("No response from server")
//                }
//            }
        }
    }

    private fun setAdapter(members: List<SessionMemberResponse.SquadData.Member>) {
        adapter = SessionMemberListAdapter(members, requireContext(),object : SessionMemberListAdapter.OnItemClickListener {
            override fun onItemClick(item: SessionMemberResponse.SquadData.Member) {
                item?.let {

//                    if (item.selected) {
//                        userListForServer.add(it.member_id)
//                    } else {
//                        userListForServer.remove(it.member_id)
//                    }
//                    Log.d(TAG, "userListForServer ${userListForServer.toString()}")
                }
            }
        })
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    override fun ResponseFailure(message: String?, tag: String?) {
        Log.e("ResponseFailure", "Failed to load squad members: $message")
        binding.tvNoListFound.text = getString(R.string.no_squad_members_found)
        binding.tvNoListFound.visibility = View.VISIBLE
    }

    override fun setTitleBar(titleBar: TitleBar) {
        titleBar.showBackButton()
        titleBar.subHeading = getString(R.string.squad_members)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
