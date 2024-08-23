package com.hotworx.ui.fragments.HotsquadList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.hotworx.databinding.FragmentSquadMemberListBinding
import com.hotworx.ui.adapters.HotsquadListAdapter.tabsadapter.MemberRequestViewPagerAdapter
import com.hotworx.ui.fragments.BaseFragment

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            squadId = it.getString("squad_id")
            hasSquadAccess = it.getBoolean("squad_access")
        }

        setupAdapter()
    }

    private fun setupAdapter() {
        pagerAdapter = MemberRequestViewPagerAdapter(childFragmentManager, 2, squadId, hasSquadAccess)
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
