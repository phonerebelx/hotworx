package com.hotworx.ui.fragments.HotsquadList

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.hotworx.R
import com.hotworx.Singletons.ApiHeaderSingleton
import com.hotworx.Singletons.ApiHeaderSingleton.apiHeader
import com.hotworx.databinding.FragmentPendingInvitesBinding
import com.hotworx.databinding.FragmentSessionPendingListBinding
import com.hotworx.global.Constants
import com.hotworx.global.WebServiceConstants
import com.hotworx.helpers.UIHelper
import com.hotworx.helpers.Utils
import com.hotworx.interfaces.OnClickPendingModelInterface
import com.hotworx.interfaces.OnClickSessionPendingModelInterface
import com.hotworx.models.DashboardData.TodaysPendingSession
import com.hotworx.models.ErrorResponseEnt
import com.hotworx.models.HotsquadList.PendingInvitationResponse
import com.hotworx.models.HotsquadList.Session.PendingSessionResponse
import com.hotworx.models.HotsquadList.Session.sendSessionInvitationRequest
import com.hotworx.models.HotsquadList.pendingListAcceptRejectRequest
import com.hotworx.retrofit.GsonFactory
import com.hotworx.ui.adapters.HotsquadListAdapter.PendingRequestAdapter
import com.hotworx.ui.adapters.HotsquadListAdapter.Sessions.SessionPendingListAdapter
import com.hotworx.ui.dialog.Hotsquad.PendingSessionDialogFragment
import com.hotworx.ui.dialog.SessionDialog.StartSessionDialogFragment
import com.hotworx.ui.fragments.BaseFragment

class SessionPendingListFragment : BaseFragment() , OnClickSessionPendingModelInterface {

    private var _binding: FragmentSessionPendingListBinding? = null
    private val binding get() = _binding!!
    private lateinit var pendingSessionResponse: PendingSessionResponse
    private var adapter: SessionPendingListAdapter? = null
    private val pendingList = mutableListOf<PendingSessionResponse.SquadInvitation>()
    private var lastAcceptedPosition: Int = -1
    private var lastDeclinedPosition: Int = -1

    var squadId = ""
    var squad_event_id = ""
    var invitation_id = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSessionPendingListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ensure PendingListModel is initialized with an empty list to avoid the UninitializedPropertyAccessException
        pendingSessionResponse =
            PendingSessionResponse(status = false, message = "", data = mutableListOf())

        setAdapter(pendingList)

        // Fetch the pending request list
        getPendingRequestList()
    }

    private fun getPendingRequestList() {
        getServiceHelper().enqueueCall(
            getWebService().getPendingSessionList(
                apiHeader(requireContext())
            ), WebServiceConstants.GET_PENDING_SESSION_LIST, true
        )
    }

    override fun ResponseSuccess(result: String?, tag: String?) {
        if (!isAdded) return // Safeguard to prevent updates if fragment is not added

        pendingSessionResponse =
            GsonFactory.getConfiguredGson().fromJson(result, PendingSessionResponse::class.java)

        if (!pendingSessionResponse.data.isNullOrEmpty()) {
            binding.tvNoListFound.visibility = View.GONE

            pendingList.clear() // Clear existing items
            pendingList.addAll(pendingSessionResponse.data) // Add new items

            updateAdapterList(pendingList) // Update adapter with the new list
        } else {
            binding.tvNoListFound.visibility = View.VISIBLE
            binding.tvNoListFound.text = "No Session List Found"
        }
    }


    private fun setAdapter(pendingList: MutableList<PendingSessionResponse.SquadInvitation>) {
        adapter = SessionPendingListAdapter(
            mutableListOf(),
            requireContext(),
            object : SessionPendingListAdapter.OnItemClickListener {
                override fun onItemClick(
                    item: PendingSessionResponse.SquadInvitation,
                    position: Int
                ) {
                    setSessionDialog(item)
                }
            })

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.itemAnimator = DefaultItemAnimator()
        binding.recyclerView.adapter = adapter
    }

    private fun updateAdapterList(newList: MutableList<PendingSessionResponse.SquadInvitation>) {
        if (isAdded) { // Check if fragment is added
            adapter?.updateData(newList)
        }
    }

    private fun onItemActionSuccess(position: Int) {
        adapter?.let {
            if (position >= 0 && position < it.itemCount) {
                it.removeItem(position)
            }
        }
    }

    override fun ResponseFailure(message: String?, tag: String?) {
        if (isAdded) { // Check if fragment is added
            binding.tvNoListFound.text = "No Session List Found"
            binding.tvNoListFound.visibility = View.VISIBLE
        }
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

    private fun setSessionDialog(getSelectPendingSession: PendingSessionResponse.SquadInvitation) {
        val pendingSessionDialogFragment = PendingSessionDialogFragment(this)
        pendingSessionDialogFragment.todaysPendingSession = getSelectPendingSession
        pendingSessionDialogFragment.show(
            childFragmentManager, Constants.PendingSessionDialogFragment
        )
    }

    override fun onSuccess(liveData: LiveData<String>, tag: String) {
        super.onSuccess(liveData, tag)
        if (!isAdded) return // Safeguard to prevent updates if fragment is not added
        when (tag) {
            Constants.PENDING_ACCEPT_REJECT -> {
                val responseJson = liveData.value
                Log.d("Response", "LiveData value: $responseJson")

                if (responseJson != null) {
                    try {
                        val response = GsonFactory.getConfiguredGson()
                            ?.fromJson(responseJson, PendingSessionResponse::class.java)!!
                        if (response.status) {
                            dockActivity?.showSuccessMessage(response.message)
                            onItemActionSuccess(position = lastAcceptedPosition)
                        } else {
                            dockActivity?.showErrorMessage(response.message)
                        }
                    } catch (e: Exception) {
                        dockActivity?.showErrorMessage(e.message.toString())
                    }
                } else {
                    Log.e("Error", "LiveData value is null")
                    dockActivity?.showErrorMessage("No response from server")
                }
            }
        }
    }

    override fun onFailure(message: String, tag: String) {

        when (tag) {
            Constants.PENDING_ACCEPT_REJECT -> {
                myDockActivity?.showErrorMessage(message)
                Log.i("xxError", "Error")
            }
        }
    }

    override fun onItemClick(value: PendingSessionResponse.SquadInvitation, type: String) {
        when (type) {
            "COME_FROM_ACCEPT" -> {
                Log.d("bdskjbkjbsdjk", value.invitation_id)
                val request = sendSessionInvitationRequest(
                    value.squad_id,
                    value.squad_event_id,
                    value.invitation_id,
                    true
                )
                val position = pendingList.indexOf(value)

                lastAcceptedPosition = position

                // Check if the item is actually in the list
                if (position != -1) {
                    getServiceHelper().enqueueCallExtended(
                        getWebService().responseSessionInvitationRequest(
                            ApiHeaderSingleton.apiHeader(requireContext()),
                            request
                        ), Constants.PENDING_ACCEPT_REJECT, true
                    )

                    onItemActionSuccess(position = position)
                }
            }
        }
    }

    override fun onItemClickDecline(value: PendingSessionResponse.SquadInvitation, type: String) {
        when (type) {
            "COME_FROM_DECLINE" -> {
                val request = sendSessionInvitationRequest(
                    value.squad_id,
                    value.squad_event_id,
                    value.invitation_id,
                    false
                )
                val position = pendingList.indexOf(value)
                lastDeclinedPosition = position // Store the position for further reference

                // Check if the item is actually in the list
                if (position != -1) {
                    // Send the decline request
                    getServiceHelper().enqueueCallExtended(
                        getWebService().responseSessionInvitationRequest(
                            ApiHeaderSingleton.apiHeader(requireContext()),
                            request
                        ), Constants.PENDING_ACCEPT_REJECT, true
                    )

                    onItemActionSuccess(position = position)
                }
            }

        }
    }
}