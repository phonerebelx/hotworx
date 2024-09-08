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
import com.hotworx.databinding.FragmentSessionProfileSummaryBinding
import com.hotworx.databinding.FragmentSessionSummaryBinding
import com.hotworx.global.Constants
import com.hotworx.global.WebServiceConstants
import com.hotworx.models.ErrorResponseEnt
import com.hotworx.models.HotsquadList.Session.PendingSessionResponse
import com.hotworx.models.HotsquadList.Session.SessionHighlightsRequest
import com.hotworx.models.HotsquadList.Session.UserActivitiesResponse
import com.hotworx.models.HotsquadList.SquadMemberDetailsResponse
import com.hotworx.retrofit.GsonFactory
import com.hotworx.ui.adapters.HotsquadListAdapter.Sessions.EventHighlightAdapter
import com.hotworx.ui.adapters.HotsquadListAdapter.Sessions.EventHighlightProfileAdapter
import com.hotworx.ui.adapters.HotsquadListAdapter.Sessions.EventMemberAdapter
import com.hotworx.ui.adapters.HotsquadListAdapter.Sessions.SessionProfileHighlightAdapter
import com.hotworx.ui.adapters.HotsquadListAdapter.SquadMemberListAdapter
import com.hotworx.ui.fragments.BaseFragment

class SessionProfileSummaryFragment : BaseFragment(){

    private var _binding: FragmentSessionProfileSummaryBinding? = null
    private val binding get() = _binding!!
    private lateinit var summaryProfile: UserActivitiesResponse
    private val highlightList = mutableListOf<UserActivitiesResponse.UserData.Highlight>()
    private var adapter: SessionProfileHighlightAdapter? = null
    private var adapterMember: EventMemberAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSessionProfileSummaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        callInvitationApi(Constants.GET_EVENT_HIGHLIGHT_LIST, "")

    }

    private fun callInvitationApi(type: String, data: String) {
        getServiceHelper()?.enqueueCall(
            getWebService()?.getSessionUserProfile(
                ApiHeaderSingleton.apiHeader(requireContext()),
            ),  WebServiceConstants.GET_SESSION_PROFILE, true
        )
    }

    override fun ResponseSuccess(result: String?, tag: String?) {
        if (!isAdded) return // Safeguard to prevent updates if fragment is not added

        summaryProfile = GsonFactory.getConfiguredGson().fromJson(result, UserActivitiesResponse::class.java)

        if (!summaryProfile.data.highlights.isNullOrEmpty()) {
            highlightList.clear() // Clear existing items
            highlightList.addAll(summaryProfile.data.highlights) // Add new items
//            updateAdapterList(highlightList) // Update adapter with the new list
        } else {
        }
    }

    private fun setAdapter(members: MutableList<UserActivitiesResponse.UserData.Highlight>) {
        adapter = SessionProfileHighlightAdapter(members, requireContext(),object : SessionProfileHighlightAdapter.OnItemClickListener {
            override fun onItemClick(item: UserActivitiesResponse.UserData.Highlight, position: Int) {
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

    override fun ResponseFailure(message: String?, tag: String?) {
        Log.e("ResponseFailure", "Failed to load squad members: $message")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
