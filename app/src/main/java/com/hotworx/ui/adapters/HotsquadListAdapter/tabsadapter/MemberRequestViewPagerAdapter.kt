package com.hotworx.ui.adapters.HotsquadListAdapter.tabsadapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.hotworx.ui.fragments.HotsquadList.SquadMemberDetailFragment
import org.apache.commons.lang3.math.NumberUtils

class MemberRequestViewPagerAdapter(var fm: FragmentManager, var tabCount: Int) :
    FragmentStatePagerAdapter(fm, tabCount) {

    override fun getCount() = tabCount

    override fun getItem(position: Int): Fragment {
        return when (position) {
            NumberUtils.INTEGER_ZERO -> {
                SquadMemberDetailFragment()
            }
            else -> {
                SquadMemberDetailFragment()
            }
        }
    }
}