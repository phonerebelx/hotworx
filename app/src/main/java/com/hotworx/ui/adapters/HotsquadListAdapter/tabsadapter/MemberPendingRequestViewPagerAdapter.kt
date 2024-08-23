package com.hotworx.ui.adapters.HotsquadListAdapter.tabsadapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.hotworx.ui.fragments.HotsquadList.PendingInvitesFragment
import com.hotworx.ui.fragments.HotsquadList.squadAcceptedMemberFragment
import com.hotworx.ui.fragments.HotsquadList.squadPendingMemberFragment

class MemberPendingRequestViewPagerAdapter(
    fm: FragmentManager,
    private val tabCount: Int,
) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount() = tabCount

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> PendingInvitesFragment()
            else -> squadAcceptedMemberFragment()
        }
    }
}
