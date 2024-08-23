package com.hotworx.ui.adapters.HotsquadListAdapter.tabsadapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.hotworx.ui.fragments.HotsquadList.fragments.squadAcceptedMemberFragment
import com.hotworx.ui.fragments.HotsquadList.fragments.squadPendingMemberFragment

class MemberRequestViewPagerAdapter(
    fm: FragmentManager,
    private val tabCount: Int,
    private val squadId: String?,
    private val hasSquadAccess: Boolean
) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount() = tabCount

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> createFragment(squadPendingMemberFragment())
            else -> createFragment(squadAcceptedMemberFragment())
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
