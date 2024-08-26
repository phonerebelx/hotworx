package com.hotworx.ui.fragments.HotsquadList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.hotworx.R
import com.hotworx.databinding.FragmentRecieverPendingRequestBinding
import com.hotworx.ui.adapters.HotsquadListAdapter.tabsadapter.MemberPendingRequestViewPagerAdapter
import com.hotworx.ui.fragments.BaseFragment
import com.hotworx.ui.views.TitleBar

class RecieverPendingRequestFragment : BaseFragment(), TabLayout.OnTabSelectedListener {

    private var _binding: FragmentRecieverPendingRequestBinding? = null
    private val binding get() = _binding!!
    private lateinit var pagerAdapter: MemberPendingRequestViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecieverPendingRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()

//        // Restore selected tab position
//        savedInstanceState?.let {
//            val savedTabPosition = it.getInt("current_tab", 0)
//            binding.viewPager.currentItem = savedTabPosition
//        }
    }

    override fun setTitleBar(titleBar: TitleBar) {
        super.setTitleBar(titleBar)
        titleBar.showBackButton()
        titleBar.subHeading = getString(R.string.Pending_request)
    }

    private fun setupAdapter() {
        pagerAdapter = MemberPendingRequestViewPagerAdapter(childFragmentManager, 2)
        binding.viewPager.adapter = pagerAdapter
        binding.viewPager.offscreenPageLimit = 2
        binding.tabLayout.addOnTabSelectedListener(this)
        binding.viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout))
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        tab?.let {
            binding.viewPager.currentItem = it.position
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {}

    override fun onTabReselected(tab: TabLayout.Tab?) {}
}
