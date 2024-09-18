package com.hotworx.ui.fragments.HotsquadList

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.LiveData
import com.google.android.material.tabs.TabLayout
import com.google.gson.GsonBuilder
import com.hotworx.R
import com.hotworx.databinding.FragmentSquadMemberListBinding
import com.hotworx.databinding.FragmentSquadPendingMemberBinding
import com.hotworx.global.Constants
import com.hotworx.models.ErrorResponseEnt
import com.hotworx.models.HotsquadList.RemoveMemberResponse
import com.hotworx.models.HotsquadList.SquadMemberDetailsResponse
import com.hotworx.models.HotsquadList.removeSquadMemberRequest
import com.hotworx.models.HotsquadList.squadMemberDetailRequest
import com.hotworx.retrofit.GsonFactory
import com.hotworx.ui.adapters.HotsquadListAdapter.tabsadapter.MemberRequestViewPagerAdapter
import com.hotworx.ui.fragments.BaseFragment
import com.passio.modulepassio.Singletons.ApiHeaderSingleton
import java.net.URLDecoder

class SquadMemberListFragment : BaseFragment(), TabLayout.OnTabSelectedListener {

    private var _binding: FragmentSquadMemberListBinding? = null
    private val binding get() = _binding!!
    private lateinit var pagerAdapter: MemberRequestViewPagerAdapter

    private var squadId: String? = null
    private var hasSquadAccess: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSquadMemberListBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            squadId = it.getString("squad_id")
            hasSquadAccess = it.getBoolean("squad_access")
        }

        setupAdapter()
        setupTabs()

        // Disable swipe gestures on the ViewPager
        binding.viewPager.setOnTouchListener { _, event ->
            event.action == MotionEvent.ACTION_MOVE
        }

        if(hasSquadAccess){
            binding.iconLayout.visibility = View.VISIBLE
            binding.pendingUsers.setOnClickListener{
                // Create a new instance of SquadPendingMemberFragment
                val squadPendingMemberFragment = squadPendingMemberFragment()
                val bundle = Bundle().apply {
                    putString("squad_id", squadId) // Pass the squadId
                    putBoolean("squad_access", hasSquadAccess) // Pass the squadId
                }
                squadPendingMemberFragment.arguments = bundle
                dockActivity.replaceDockableFragment(squadPendingMemberFragment)
            }
        }else{
            binding.iconLayout.visibility = View.GONE
        }

        callInvitationApi(Constants.GET_SQUAD_MEMBER_LIST,"")
    }

    private fun setupAdapter() {
        pagerAdapter = MemberRequestViewPagerAdapter(childFragmentManager, squadId,hasSquadAccess,2)
        binding.viewPager.adapter = pagerAdapter
        binding.viewPager.offscreenPageLimit = 2
        binding.tabLayout.addOnTabSelectedListener(this)
        binding.viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout))

        binding.viewPager.setOnTouchListener { _, _ -> false }
    }

    private fun callInvitationApi(type: String, data: String) {
        when (type) {
            Constants.GET_SQUAD_MEMBER_LIST -> {
                val request = squadMemberDetailRequest(squadId.toString())

                getServiceHelper()?.enqueueCallExtended(
                    getWebService()?.getSquadDetail(
                        ApiHeaderSingleton.apiHeader(requireContext()),
                        request
                    ), Constants.GET_SQUAD_MEMBER_LIST, true
                )
            }
        }
    }

    override fun onSuccess(liveData: LiveData<String>, tag: String) {
        super.onSuccess(liveData, tag)
        when (tag) {
            Constants.GET_SQUAD_MEMBER_LIST -> {
                val responseJson = liveData.value
                Log.d("Response", "LiveData value: $responseJson")

                if (responseJson != null) {
                    try {
                        val response = GsonFactory.getConfiguredGson()?.fromJson(responseJson, SquadMemberDetailsResponse::class.java)
                        if (response?.status == true) {
                            val members = response.data.members
                            if(response.data.pending_invites == 0){
                                binding.flView.visibility = View.GONE
                            }else{
                                binding.flView.visibility = View.VISIBLE
                                binding.tvNotificationNo.text = response.data.pending_invites.toString()
                            }

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

    private fun setupTabs() {
        // Clear any existing tabs
        binding.tabLayout.removeAllTabs()

        val acceptedTab = binding.tabLayout.newTab().setText(R.string.friends)
        binding.tabLayout.addTab(acceptedTab)

        val pendingTab = binding.tabLayout.newTab().setText(R.string.session_summary)
        binding.tabLayout.addTab(pendingTab)

//        pendingTab.view.isClickable = hasSquadAccess
//        pendingTab.view.isEnabled = hasSquadAccess
//
//        if (!hasSquadAccess) {
//            pendingTab.view.alpha = 0.5f // Makes the tab appear disabled
//        } else {
//            pendingTab.view.alpha = 1.0f // Ensure normal appearance for enabled tab
//        }

        binding.viewPager.currentItem = 0
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        tab?.let {
            binding.viewPager.currentItem = it.position
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {}

    override fun onTabReselected(tab: TabLayout.Tab?) {
        tab?.let {
            binding.viewPager.offsetLeftAndRight(1)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
