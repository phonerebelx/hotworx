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
import com.hotworx.models.ErrorResponseEnt
import com.hotworx.models.HotsquadList.PendingInvitationResponse
import com.hotworx.models.HotsquadList.Session.PendingSessionResponse
import com.hotworx.models.HotsquadList.pendingListAcceptRejectRequest
import com.hotworx.retrofit.GsonFactory
import com.hotworx.ui.adapters.HotsquadListAdapter.PendingRequestAdapter
import com.hotworx.ui.adapters.HotsquadListAdapter.Sessions.SessionPendingListAdapter
import com.hotworx.ui.fragments.BaseFragment

class SessionPendingListFragment : BaseFragment() {

    private var _binding: FragmentSessionPendingListBinding? = null
    private val binding get() = _binding!!
    private lateinit var pendingSessionResponse: PendingSessionResponse
    private var adapter: SessionPendingListAdapter? = null
    private val pendingList = mutableListOf<PendingSessionResponse.SquadInvitation>()

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
        pendingSessionResponse = PendingSessionResponse(status = false, message = "", data = mutableListOf())

        setAdapter(pendingList)

        // Fetch the pending request list
        getPendingRequestList()
    }

    private fun getPendingRequestList() {
        getServiceHelper().enqueueCallExtended(
            getWebService().getPendingSessionList(
                apiHeader(requireContext())
            ), WebServiceConstants.GET_PENDING_SESSION_LIST, true
        )
    }

    override fun ResponseSuccess(result: String?, tag: String?) {
        if (!isAdded) return // Safeguard to prevent updates if fragment is not added

        pendingSessionResponse = GsonFactory.getConfiguredGson().fromJson(result, PendingSessionResponse::class.java)

        if (!pendingSessionResponse.data.isNullOrEmpty()) {
            binding.tvNoListFound.visibility = View.GONE
            updateAdapterList(pendingList)
        } else {
            binding.tvNoListFound.visibility = View.VISIBLE
            binding.tvNoListFound.text = "No Squad List Found"
        }
    }

//    override fun onSuccess(liveData: LiveData<String>, tag: String) {
//        super.onSuccess(liveData, tag)
//        if (!isAdded) return // Safeguard to prevent updates if fragment is not added
//
//        when (tag) {
//            Constants.PENDING_ACCEPT_REJECT -> {
//                val responseJson = liveData.value
//                Log.d("Response", "LiveData value: $responseJson")
//
//                if (responseJson != null) {
//                    try {
//                        val response = GsonFactory.getConfiguredGson()?.fromJson(responseJson, PendingInvitationResponse::class.java)!!
//                        if (response.status) {
//                            dockActivity?.showSuccessMessage(response.message)
//                        } else {
//                            dockActivity?.showErrorMessage(response.message)
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
//        }
//    }

    private fun setAdapter(pendingList: MutableList<PendingSessionResponse.SquadInvitation>)  {
        adapter = SessionPendingListAdapter(mutableListOf(), requireContext(), object : SessionPendingListAdapter.OnItemClickListener {
            override fun onItemClick(item:PendingSessionResponse.SquadInvitation, position: Int) {

            }
        })

        // Set adapter to RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.itemAnimator = DefaultItemAnimator() // Ensure animations are enabled
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
            binding.tvNoListFound.text = "No Squad List Found"
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
}