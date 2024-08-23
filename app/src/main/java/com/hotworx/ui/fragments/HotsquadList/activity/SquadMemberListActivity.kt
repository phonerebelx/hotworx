package com.hotworx.ui.fragments.HotsquadList.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.tabs.TabLayout
import com.hotworx.R
import com.hotworx.activities.DockActivity
import com.hotworx.databinding.ActivitySquadMemberListBinding

import com.hotworx.ui.BaseActivity
import com.hotworx.ui.adapters.HotsquadListAdapter.tabsadapter.MemberRequestViewPagerAdapter
import com.hotworx.ui.views.TitleBar


class SquadMemberListActivity: DockActivity(){

    private var _binding: ActivitySquadMemberListBinding? = null
    private val binding get() = _binding!!
    lateinit var pagerAdapter: MemberRequestViewPagerAdapter

    override fun onLoadingStarted() {
        binding.layoutProgressIndicator.progressIndicator.visibility =View.VISIBLE
    }

    override fun onLoadingFinished() {
        binding.layoutProgressIndicator.progressIndicator.visibility =View.GONE
    }

    override fun onProgressUpdated(percentLoaded: Int) {}

    override fun getDockFrameLayoutId(): Int {
        return R.id.mainFrameLayout
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivitySquadMemberListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ensure context is valid
        val packageManager = packageManager
        if (packageManager == null) {
            Log.e("SquadMemberListActivity", "packageManager is null")
        }

//        setupAdapter()
    }

//    override fun setTitleBar(titleBar: TitleBar) {
//        titleBar.showBackButton()
//        titleBar.subHeading = getString(R.string.hotsquad_list)
//    }

//    private fun setupAdapter() {
//        pagerAdapter = MemberRequestViewPagerAdapter(supportFragmentManager, binding.tabLayout.tabCount,sq)
//        binding.viewPager.adapter = pagerAdapter
//        binding.viewPager.offscreenPageLimit = binding.tabLayout.tabCount
//        binding.tabLayout.addOnTabSelectedListener(this)
//        binding.viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout))
//
//        // Disabling swipe gesture of viewpager
//        binding.viewPager.setOnTouchListener { _, _ -> false }
//    }

//    override fun onTabSelected(tab: TabLayout.Tab?) {
//        tab?.let {
//            binding.viewPager.currentItem = it.position
//        }
//    }
//
//    override fun onTabUnselected(tab: TabLayout.Tab?) {}
//
//    override fun onTabReselected(tab: TabLayout.Tab?) {
//        tab?.let {
//            binding.viewPager.offsetLeftAndRight(1)
//        }
//    }
//
//    fun selectPage(page: Int) {
//        binding.viewPager.currentItem = page
//        binding.tabLayout.getTabAt(page)?.select()
//    }
}