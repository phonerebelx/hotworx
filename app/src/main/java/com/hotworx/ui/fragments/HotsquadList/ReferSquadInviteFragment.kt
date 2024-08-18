package com.hotworx.ui.fragments.HotsquadList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hotworx.R
import com.hotworx.databinding.FragmentHotsquadCreateBinding
import com.hotworx.databinding.FragmentReferSquadInviteBinding
import com.hotworx.ui.fragments.BaseFragment
import com.hotworx.ui.views.TitleBar


class ReferSquadInviteFragment  : BaseFragment(){
    private var _binding: FragmentReferSquadInviteBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReferSquadInviteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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