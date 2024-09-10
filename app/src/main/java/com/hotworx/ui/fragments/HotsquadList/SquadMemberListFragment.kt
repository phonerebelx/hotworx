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
import com.google.android.material.tabs.TabLayout
import com.hotworx.R
import com.hotworx.databinding.FragmentSquadMemberListBinding
import com.hotworx.databinding.FragmentSquadPendingMemberBinding
import com.hotworx.ui.adapters.HotsquadListAdapter.tabsadapter.MemberRequestViewPagerAdapter
import com.hotworx.ui.fragments.BaseFragment

class SquadMemberListFragment : BaseFragment(), TabLayout.OnTabSelectedListener {

    private var _binding: FragmentSquadMemberListBinding? = null
    private val binding get() = _binding!!
    private lateinit var pagerAdapter: MemberRequestViewPagerAdapter

    private var squadId: String? = null
//    private var hasSquadAccess: Boolean = false

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
//            hasSquadAccess = it.getBoolean("squad_access")
        }

        setupAdapter()
        setupTabs()

        // Disable swipe gestures on the ViewPager
        binding.viewPager.setOnTouchListener { _, event ->
            event.action == MotionEvent.ACTION_MOVE
        }

        binding.userImage.setOnClickListener{
            // Create a new instance of SquadPendingMemberFragment
            val squadPendingMemberFragment = squadPendingMemberFragment()
            val bundle = Bundle().apply {
                putString("squad_id", squadId) // Pass the squadId
            }
            squadPendingMemberFragment.arguments = bundle
            dockActivity.replaceDockableFragment(squadPendingMemberFragment)
        }
    }

    private fun setupAdapter() {
        pagerAdapter = MemberRequestViewPagerAdapter(childFragmentManager,  squadId,2)
        binding.viewPager.adapter = pagerAdapter
        binding.viewPager.offscreenPageLimit = 2
        binding.tabLayout.addOnTabSelectedListener(this)
        binding.viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout))

        binding.viewPager.setOnTouchListener { _, _ -> false }
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
