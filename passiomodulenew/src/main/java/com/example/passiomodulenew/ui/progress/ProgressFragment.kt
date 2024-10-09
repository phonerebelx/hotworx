package com.example.passiomodulenew.ui.progress

import com.example.passiomodulenew.ui.progress.MacrosViewPagerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.passiomodulenew.ui.base.BaseFragment
import com.example.passiomodulenew.ui.base.BaseToolbar
import com.google.android.material.tabs.TabLayoutMediator
import com.passio.passiomodulenew.R
import com.passio.passiomodulenew.databinding.FragmentProgressBinding

class ProgressFragment : BaseFragment<ProgressViewModel>() {

    private var _binding: FragmentProgressBinding? = null
    private val binding: FragmentProgressBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProgressBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding)
        {
            toolbar.setup(getString(R.string.my_progress), baseToolbarListener)
            viewPager.adapter = MacrosViewPagerAdapter(this@ProgressFragment)
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = requireContext().getString(R.string.macros)
                    1 -> tab.text = requireContext().getString(R.string.micros)
                }
            }.attach()
        }

    }

    private val baseToolbarListener = object : BaseToolbar.ToolbarListener {
        override fun onBack() {
            viewModel.navigateBack()
        }

        override fun onRightIconClicked() {
            showPopupMenu(binding.toolbar.findViewById(R.id.toolbarMenu))
        }

    }

}