package com.hotworx.ui.fragments.HotsquadList

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.hotworx.R
import com.hotworx.databinding.FragmentRecieverPendingRequestBinding
import com.hotworx.global.Constants
import com.hotworx.ui.adapters.HotsquadListAdapter.tabsadapter.MemberPendingRequestViewPagerAdapter
import com.hotworx.ui.fragments.BaseFragment
import com.hotworx.ui.views.TitleBar


class RecieverPendingRequestFragment : BaseFragment(), TabLayout.OnTabSelectedListener {

    private var _binding: FragmentRecieverPendingRequestBinding? = null
    private val binding get() = _binding!!
    private lateinit var pagerAdapter: MemberPendingRequestViewPagerAdapter
    var squadKey = ""
    var sessionKey = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecieverPendingRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()

        setupTabs()

        // Retrieve arguments
        val args = arguments
        if (args != null) {
            squadKey = args.getString("squad_key").toString()
            sessionKey = args.getString("session_key").toString()
        }

        if(Constants.PendingInvitesFragment == squadKey){
            Log.d("RecieverPendingRequestFragment", "Selecting squad tab")
            binding.tabLayout.getTabAt(0)?.select()
        }else if(Constants.SessionPendingListFragment == sessionKey){
            Log.d("RecieverPendingRequestFragment", "Selecting session tab")
            binding.tabLayout.getTabAt(1)?.select()
        }else{
            Log.d("RecieverPendingRequestFragment", "Selecting default tab")
            binding.tabLayout.getTabAt(0)?.select()
        }

        // Disable swipe gestures on the ViewPager
        binding.viewPager.setOnTouchListener { _, event ->
            event.action == MotionEvent.ACTION_MOVE
        }

    }

    override fun setTitleBar(titleBar: TitleBar) {
        super.setTitleBar(titleBar)
        titleBar.showBackButton()
        titleBar.hidePassioBtn()
        titleBar.subHeading = getString(R.string.Pending_request)
    }

    private fun setupAdapter() {
        pagerAdapter = MemberPendingRequestViewPagerAdapter(childFragmentManager, 2)
        binding.viewPager.adapter = pagerAdapter
        binding.viewPager.offscreenPageLimit = 2
        binding.tabLayout.addOnTabSelectedListener(this)
        binding.viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout))
    }

    private fun setupTabs() {
        // Clear any existing tabs
        binding.tabLayout.removeAllTabs()

        val squadTab = binding.tabLayout.newTab().setText(R.string.squad_request)
        binding.tabLayout.addTab(squadTab)

        val sessionTab = binding.tabLayout.newTab().setText(R.string.session_request)
        binding.tabLayout.addTab(sessionTab)

        binding.viewPager.currentItem = 0
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        tab?.let {
            binding.viewPager.currentItem = it.position
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {}

    override fun onTabReselected(tab: TabLayout.Tab?) {}
}
