package com.hotworx.ui.fragments.HotsquadList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.hotworx.R
import com.hotworx.databinding.FragmentRecieverPendingRequestBinding
import com.hotworx.databinding.FragmentSquadMemberListBinding
import com.hotworx.ui.adapters.HotsquadListAdapter.tabsadapter.MemberPendingRequestViewPagerAdapter
import com.hotworx.ui.adapters.HotsquadListAdapter.tabsadapter.MemberRequestViewPagerAdapter
import com.hotworx.ui.fragments.BaseFragment


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
    }

    private fun setupAdapter() {
        pagerAdapter = MemberPendingRequestViewPagerAdapter(childFragmentManager, 2)
        binding.viewPager.adapter = pagerAdapter
        binding.viewPager.offscreenPageLimit = 2
        binding.tabLayout.addOnTabSelectedListener(this)
        binding.viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout))

        binding.viewPager.setOnTouchListener { _, _ -> false }
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