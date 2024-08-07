package com.hotworx.ui.fragments.HotsquadList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hotworx.R
import com.hotworx.databinding.FragmentHotsquadCreateBinding
import com.hotworx.ui.fragments.BaseFragment
import com.hotworx.ui.views.TitleBar

class CreateHotsquadFragment : BaseFragment() {
    private var _binding: FragmentHotsquadCreateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHotsquadCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCreateHotsquad.setOnClickListener {
            val addListFragment = AddListFragment()
            dockActivity.replaceDockableFragment(addListFragment)
        }
    }

    override fun setTitleBar(titleBar: TitleBar) {
        super.setTitleBar(titleBar)
        titleBar.showBackButton()
        titleBar.subHeading = getString(R.string.leaderboard)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
