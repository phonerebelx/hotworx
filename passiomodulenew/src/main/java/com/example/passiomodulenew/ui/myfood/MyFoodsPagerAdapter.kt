package com.example.passiomodulenew.ui.myfood

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.passiomodulenew.ui.customfoods.CustomFoodsFragment
import com.example.passiomodulenew.ui.myreceipes.MyRecipesFragment

class MyFoodsPagerAdapter(parentFragment: Fragment) : FragmentStateAdapter(parentFragment) {
    private val fragmentList = listOf(
        CustomFoodsFragment(),
        MyRecipesFragment()
    )

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}