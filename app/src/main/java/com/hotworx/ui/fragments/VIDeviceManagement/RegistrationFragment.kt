package com.hotworx.ui.fragments.VIDeviceManagement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hotworx.R
import com.hotworx.databinding.FragmentAddListBinding
import com.hotworx.databinding.FragmentRegisterationBinding
import com.hotworx.ui.fragments.BaseFragment

class RegistrationFragment : BaseFragment() {

    lateinit var binding: FragmentRegisterationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRegisterationBinding.inflate(inflater, container, false)
        setonClickListener()
        return binding.root
    }


    private fun setonClickListener(){
        val args = Bundle()
        val conformRegistrationFragment = ConformRegistrationFragment()

        binding.btnRegister.setOnClickListener {
            args.putString("register_service","Register")
            conformRegistrationFragment.arguments = args
            dockActivity.replaceDockableFragment(conformRegistrationFragment)
        }
        binding.btnUnRegister.setOnClickListener {
            args.putString("register_service","UnRegister")
            conformRegistrationFragment.arguments = args
            dockActivity.replaceDockableFragment(conformRegistrationFragment)
        }
    }



}