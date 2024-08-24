package com.hotworx.ui.adapters.HotsquadListAdapter.tabsadapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import com.hotworx.ui.fragments.HotsquadList.PendingInvitesFragment
import com.hotworx.ui.fragments.HotsquadList.squadAcceptedMemberFragment

class MemberPendingRequestViewPagerAdapter(
    fm: FragmentManager,
    private val tabCount: Int
) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount() = tabCount

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> PendingInvitesFragment()
            else -> squadAcceptedMemberFragment()
        }
    }

    // Provide stable IDs for the fragments
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}
