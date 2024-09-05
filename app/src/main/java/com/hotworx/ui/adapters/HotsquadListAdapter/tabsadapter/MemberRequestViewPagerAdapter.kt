package com.hotworx.ui.adapters.HotsquadListAdapter.tabsadapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.hotworx.ui.fragments.HotsquadList.SessionSummaryFragment
import com.hotworx.ui.fragments.HotsquadList.squadAcceptedMemberFragment
import com.hotworx.ui.fragments.HotsquadList.squadPendingMemberFragment

class MemberRequestViewPagerAdapter(
    fm: FragmentManager,
    private val squadId: String?,
    private val hasSquadAccess: Boolean
) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    // Adjust tab count based on hasSquadAccess
    override fun getCount(): Int {
        return if (hasSquadAccess) {
            2 // Show both tabs
        } else {
            1 // Show only the accepted tab
        }
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> createFragment(squadAcceptedMemberFragment())
            1 -> createFragment(SessionSummaryFragment())
            else -> throw IllegalArgumentException("Invalid position $position")
        }
    }

    private fun createFragment(fragment: Fragment): Fragment {
        fragment.arguments = Bundle().apply {
            putString("squad_id", squadId)
            putBoolean("squad_access", hasSquadAccess)
        }
        return fragment
    }
}
