package com.hotworx.ui.fragments.HotsquadList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.passio.modulepassio.Singletons.ApiHeaderSingleton
import com.passio.modulepassio.Singletons.ApiHeaderSingleton.apiHeader
import com.hotworx.databinding.FragmentPendingInvitesBinding
import com.hotworx.global.Constants
import com.hotworx.global.WebServiceConstants
import com.hotworx.models.ErrorResponseEnt
import com.hotworx.models.HotsquadList.PendingInvitationResponse
import com.hotworx.models.HotsquadList.pendingListAcceptRejectRequest
import com.hotworx.retrofit.GsonFactory
import com.hotworx.ui.adapters.HotsquadListAdapter.PendingRequestAdapter
import com.hotworx.ui.fragments.BaseFragment

class PendingInvitesFragment : BaseFragment() {

    private var _binding: FragmentPendingInvitesBinding? = null
    private val binding get() = _binding

    private lateinit var pendingRequestModel: PendingInvitationResponse
    private var adapter: PendingRequestAdapter? = null
    private val pendingList = mutableListOf<PendingInvitationResponse.SquadData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPendingInvitesBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize PendingRequestModel with an empty list
        pendingRequestModel = PendingInvitationResponse(status = false, message = "", data = mutableListOf())

        setAdapter(pendingList)

        // Fetch the pending request list
        getPendingRequestList()
    }

    private fun getPendingRequestList() {
        getServiceHelper().enqueueCall(
            getWebService().getPendingRequestList(apiHeader(requireContext())),
            WebServiceConstants.GET_PENDING_REQUEST_LIST, true
        )
    }

    override fun ResponseSuccess(result: String?, tag: String?) {
        if (!isAdded) return // Safeguard to prevent updates if fragment is not added

        Log.d("Response", "Raw JSON response: $result")
        pendingRequestModel = GsonFactory.getConfiguredGson().fromJson(result, PendingInvitationResponse::class.java)

        Log.d("Response", "Deserialized data: ${pendingRequestModel.data}")

        if (pendingRequestModel.data.isNullOrEmpty()) {
            binding?.tvNoListFound?.apply {
                visibility = View.VISIBLE
                text = "No Squad Request Found!"
            }
        } else {
            binding?.tvNoListFound?.visibility = View.GONE
            updateAdapterList(pendingRequestModel.data)
        }
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
                        val response = GsonFactory.getConfiguredGson().fromJson(responseJson, PendingInvitationResponse::class.java)
                        if (response.status) {
                            dockActivity?.showSuccessMessage(response.message)
                        } else {
                            dockActivity?.showErrorMessage(response.message)
                        }
                    } catch (e: Exception) {
                        val genericMsgResponse = GsonFactory.getConfiguredGson().fromJson(responseJson, ErrorResponseEnt::class.java)
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
        adapter = PendingRequestAdapter(mutableListOf(), requireContext(), object : PendingRequestAdapter.OnItemClickListener {
            override fun onItemClick(item: PendingInvitationResponse.SquadData, position: Int) {
                val request = pendingListAcceptRejectRequest(
                    item.squad_id.toString(),
                    item.invitation_id.toString(),
                    true
                )
                getServiceHelper().enqueueCallExtended(
                    getWebService().PendingRequestAccept(apiHeader(requireContext()), request),
                    Constants.PENDING_ACCEPT_REJECT, true
                )
                onItemActionSuccess(position)
            }

            override fun onItemClickDecline(item: PendingInvitationResponse.SquadData, position: Int) {
                val request = pendingListAcceptRejectRequest(
                    item.squad_id.toString(),
                    item.invitation_id.toString(),
                    false
                )
                getServiceHelper().enqueueCallExtended(
                    getWebService().PendingRequestAccept(apiHeader(requireContext()), request),
                    Constants.PENDING_ACCEPT_REJECT, true
                )
                onItemActionSuccess(position)
            }
        })

        // Set adapter to RecyclerView
        binding?.recyclerView?.layoutManager = LinearLayoutManager(requireContext())
        binding?.recyclerView?.itemAnimator = DefaultItemAnimator() // Ensure animations are enabled
        binding?.recyclerView?.adapter = adapter
    }

    private fun updateAdapterList(newList: MutableList<PendingInvitationResponse.SquadData>) {
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
            binding?.tvNoListFound?.apply {
                visibility = View.VISIBLE
                text = "No Squad Request Found!"
            }
        }
    }

    private fun scrollToNotification(listId: String) {
        binding?.recyclerView?.post {
            val layoutManager = binding!!.recyclerView.layoutManager as LinearLayoutManager
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

