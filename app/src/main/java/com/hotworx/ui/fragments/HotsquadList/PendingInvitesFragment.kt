package com.hotworx.ui.fragments.HotsquadList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hotworx.R
import com.hotworx.databinding.FragmentMyHotsquadListBinding
import com.hotworx.databinding.FragmentPendingInvitesBinding
import com.hotworx.ui.fragments.BaseFragment

class PendingInvitesFragment : BaseFragment() {

    private var _binding: FragmentPendingInvitesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPendingInvitesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}