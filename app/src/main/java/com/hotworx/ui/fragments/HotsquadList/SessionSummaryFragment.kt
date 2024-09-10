package com.hotworx.ui.fragments.HotsquadList

import SessionSquadEventsResponse
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.hotworx.R
import com.hotworx.Singletons.ApiHeaderSingleton
import com.hotworx.databinding.FragmentSessionSummaryBinding
import com.hotworx.databinding.FragmentSquadPendingMemberBinding
import com.hotworx.global.Constants
import com.hotworx.models.ErrorResponseEnt
import com.hotworx.models.HotsquadList.RemoveMemberResponse
import com.hotworx.models.HotsquadList.Session.SessionHighlightsRequest
import com.hotworx.models.HotsquadList.SquadMemberDetailsResponse
import com.hotworx.models.HotsquadList.removeSquadMemberRequest
import com.hotworx.models.HotsquadList.squadMemberDetailRequest
import com.hotworx.retrofit.GsonFactory
import com.hotworx.ui.adapters.HotsquadListAdapter.Sessions.EventHighlightAdapter
import com.hotworx.ui.adapters.HotsquadListAdapter.Sessions.EventHighlightProfileAdapter
import com.hotworx.ui.adapters.HotsquadListAdapter.Sessions.EventMemberAdapter
import com.hotworx.ui.adapters.HotsquadListAdapter.SquadMemberListAdapter
import com.hotworx.ui.fragments.BaseFragment
import com.hotworx.ui.views.TitleBar
import java.net.URLDecoder


class SessionSummaryFragment : BaseFragment(), SquadMemberListAdapter.OnItemClickListener {

    private var _binding: FragmentSessionSummaryBinding? = null
    private val binding get() = _binding!!
    private var adapter: EventHighlightAdapter? = null
    private var adapterMember: EventMemberAdapter? = null
    private var adapterHighlightProfile: EventHighlightProfileAdapter? = null
    private var squadId: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSessionSummaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve the squad ID from the fragment arguments
        arguments?.let {
            squadId = it.getString("squad_id") ?: ""
            Log.d("SquadID", squadId)
        }

        callInvitationApi(Constants.GET_EVENT_HIGHLIGHT_LIST, "")

    }

    override fun onItemClick(item: SquadMemberDetailsResponse.SquadData.Member) {
        // Handle member item click event here
    }

    private fun callInvitationApi(type: String, data: String) {
        when (type) {
            Constants.GET_EVENT_HIGHLIGHT_LIST -> {
                val request = SessionHighlightsRequest(squadId)

                getServiceHelper()?.enqueueCallExtended(
                    getWebService()?.getEventsList(
                        ApiHeaderSingleton.apiHeader(requireContext()),
                        request
                    ), Constants.GET_EVENT_HIGHLIGHT_LIST, true
                )
            }
        }
    }

    override fun onSuccess(liveData: LiveData<String>, tag: String) {
        super.onSuccess(liveData, tag)
        when (tag) {
            Constants.GET_EVENT_HIGHLIGHT_LIST -> {
                val responseJson = liveData.value
                Log.d("Response", "LiveData value: $responseJson")

                if (responseJson != null) {
                    try {
                        val response = GsonFactory.getConfiguredGson()?.fromJson(responseJson, SessionSquadEventsResponse::class.java)
                        if (response?.status == true) {

                            if (response.data.squadEvents.isEmpty() && response.data.members.isEmpty()) {
                                binding.tvHighlightNotFound.visibility = View.VISIBLE
                                binding.tvMemberNotFound.visibility = View.VISIBLE
                            } else {
                                binding.tvHighlightNotFound.visibility = if (response.data.squadEvents.isEmpty()) View.VISIBLE else View.GONE
                                binding.tvMemberNotFound.visibility = if (response.data.members.isEmpty()) View.VISIBLE else View.GONE
                            }

                            val highlight = response.data.squadEvents
                            val members = response.data.members
                            setAdapter(highlight)
                            setMemberAdapter(members)
                        } else {
                            dockActivity?.showErrorMessage("Something Went Wrong")
                        }
                    } catch (e: Exception) {
                        val genericMsgResponse = GsonFactory.getConfiguredGson()
                            ?.fromJson(responseJson, ErrorResponseEnt::class.java)
                        dockActivity?.showErrorMessage(genericMsgResponse?.error.toString())
                        Log.i("Error", e.message.toString())
                    }
                } else {
                    Log.e("Error", "LiveData value is null")
                    dockActivity?.showErrorMessage("No response from server")
                }
            }
        }
    }

    private fun setAdapter(members: MutableList<SessionSquadEventsResponse.SquadEvent>) {
        adapter = EventHighlightAdapter(members, requireContext(),object : EventHighlightAdapter.OnItemClickListener {
            override fun onItemClick(item: SessionSquadEventsResponse.SquadEvent, position: Int) {
                Log.d("testing","testing data")
            }
        })
        binding.recyclerViewHighlight.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewHighlight.adapter = adapter
    }

    private fun setMemberAdapter(members: MutableList<SessionSquadEventsResponse.Member>) {
        adapterMember = EventMemberAdapter(members, requireContext(),object : EventMemberAdapter.OnItemClickListener {
            override fun onItemClick(item: SessionSquadEventsResponse.Member, position: Int) {
                Log.d("testing","testing dataaaaaaaa")
            }
        })
        binding.recyclerViewMember.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewMember.adapter = adapterMember
    }

    private fun updateAdapterList(newList: MutableList<SessionSquadEventsResponse.SquadEvent>) {
        adapter?.updateData(newList)
    }

    private fun onItemActionSuccess(position: Int) {
        adapter?.let {
            if (position >= 0 && position < it.itemCount) {
                it.removeItem(position)
            }
        }
    }

    override fun ResponseFailure(message: String?, tag: String?) {
        Log.e("ResponseFailure", "Failed to load squad members: $message")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
