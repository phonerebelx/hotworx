package com.hotworx.ui.fragments.HotsquadList

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.hotworx.R
import com.hotworx.Singletons.ApiHeaderSingleton
import com.hotworx.Singletons.ApiHeaderSingleton.apiHeader
import com.hotworx.activities.DockActivity
import com.hotworx.databinding.FragmentMyHotsquadListBinding
import com.hotworx.databinding.FragmentPendingInvitesBinding
import com.hotworx.global.Constants
import com.hotworx.global.WebServiceConstants
import com.hotworx.models.ErrorResponseEnt
import com.hotworx.models.HotsquadList.FoundUser
import com.hotworx.models.HotsquadList.Hotsquad
import com.hotworx.models.HotsquadList.HotsquadListModel
import com.hotworx.models.HotsquadList.PendingInvitationResponse
import com.hotworx.models.HotsquadList.SearchListRequest
import com.hotworx.models.HotsquadList.SearchUserModel
import com.hotworx.models.HotsquadList.pendingListAcceptRejectRequest
import com.hotworx.retrofit.GsonFactory
import com.hotworx.ui.adapters.HotsquadListAdapter.PendingRequestAdapter
import com.hotworx.ui.adapters.HotsquadListAdapter.SearchRegisteredAdapter
import com.hotworx.ui.adapters.HotsquadListAdapter.SquadListAdapter
import com.hotworx.ui.fragments.BaseFragment
import com.hotworx.ui.fragments.HotsquadList.Bottomsheet.SearchUserBottomSheet.Companion.TAG
import com.hotworx.ui.views.TitleBar

class PendingInvitesFragment : BaseFragment(){

    private var _binding: FragmentPendingInvitesBinding? = null
    private val binding get() = _binding!!
    private lateinit var pendingRequestModel: PendingInvitationResponse
    private var adapter: PendingRequestAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPendingInvitesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getPendingRequestList()

        // Ensure PendingListModel is initialized with an empty list to avoid the UninitializedPropertyAccessException
        pendingRequestModel = PendingInvitationResponse(status =false,message = "",data = mutableListOf())
        setAdapter(pendingRequestModel.data)
    }

    override fun setTitleBar(titleBar: TitleBar) {
        titleBar.showBackButton()
        titleBar.subHeading = getString(R.string.pending_invite)
    }

    private fun getPendingRequestList() {
        getServiceHelper().enqueueCall(
            getWebService().getPendingRequestList(
                apiHeader(
                    requireContext()
                )
            ), WebServiceConstants.GET_PENDING_REQUEST_LIST, true
        )
    }

    override fun ResponseSuccess(result: String?, Tag: String?) {
        pendingRequestModel = GsonFactory.getConfiguredGson().fromJson(result, PendingInvitationResponse::class.java)

        if (!pendingRequestModel.data.isNullOrEmpty()) {
            binding.tvNoListFound.visibility = View.GONE
            setAdapter(pendingRequestModel.data!!)
        } else {
            binding.tvNoListFound.visibility = View.VISIBLE
            binding.tvNoListFound.text = "No Squad List Found"
        }
    }

    override fun onSuccess(liveData: LiveData<String>, tag: String) {
        super.onSuccess(liveData, tag)
        when (tag) {
            Constants.PENDING_ACCEPT_REJECT -> {
                val responseJson = liveData.value
                Log.d("Response", "LiveData value: $responseJson")

                if (responseJson != null) {
                    try {
                        val response = GsonFactory.getConfiguredGson()?.fromJson(responseJson, PendingInvitationResponse::class.java)!!
                        if (response.status) {
                            dockActivity?.showSuccessMessage(response.message)

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

    private fun setAdapter(pendingList: MutableList<PendingInvitationResponse.SquadData>) {
        val adapter = PendingRequestAdapter(pendingList, requireContext(), object : PendingRequestAdapter.OnItemClickListener {
            override fun onItemClick(item: PendingInvitationResponse.SquadData, position: Int) {
                item?.let {
                    val request = pendingListAcceptRejectRequest(
                        item.squad_id.toString(),
                        item.invitation_id.toString(),
                        true
                    )
                    getServiceHelper().enqueueCallExtended(
                        getWebService().PendingRequestAccept(
                            ApiHeaderSingleton.apiHeader(requireContext()),
                            request
                        ), Constants.PENDING_ACCEPT_REJECT, true
                    )

                    // Pass the position to the success handler
                    onItemActionSuccess(position)
                }
            }

            override fun onItemClickDecline(item: PendingInvitationResponse.SquadData,position: Int) {
                item?.let {
                    val request = pendingListAcceptRejectRequest(
                        item.squad_id.toString(),
                        item.invitation_id.toString(),
                        false
                    )
                    getServiceHelper().enqueueCallExtended(
                        getWebService().PendingRequestAccept(
                            ApiHeaderSingleton.apiHeader(requireContext()),
                            request
                        ), Constants.PENDING_ACCEPT_REJECT, true
                    )

                    onItemActionSuccess(position)
                }
            }
        })

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    private fun onItemActionSuccess(position: Int) {
        adapter?.let {
            it.items.removeAt(position)
            it.notifyItemRemoved(position)
        }
    }

    override fun ResponseFailure(message: String?, tag: String?) {
        binding.tvNoListFound.text = "No Squad List Found"
        binding.tvNoListFound.visibility = View.VISIBLE
    }

    private fun scrollToNotification(listId: String) {
        binding.recyclerView.post {
            val layoutManager = binding.recyclerView.layoutManager as LinearLayoutManager
            val position: Int = adapter?.getPositionById(listId) ?: -1
            if (position != -1) {
                layoutManager.scrollToPositionWithOffset(position, 0)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}