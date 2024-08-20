package com.hotworx.ui.fragments.VIDeviceManagement

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import com.hotworx.databinding.FragmentRegisterationBinding
import com.hotworx.ui.fragments.BaseFragment
import com.hotworx.ui.views.TitleBar
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

class RegistrationFragment : BaseFragment() {

    private lateinit var binding: FragmentRegisterationBinding
    private val args = Bundle()
    private val conformRegistrationFragment = ConformRegistrationFragment()

    // Declare the launcher here to avoid losing it during fragment recreation
    private lateinit var barcodeLauncher: ActivityResultLauncher<ScanOptions>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterationBinding.inflate(inflater, container, false)
        registerBarcodeLauncher() // Register the launcher when the view is created
        setOnClickListener()

        return binding.root
    }

    private fun registerBarcodeLauncher() {
        barcodeLauncher = registerForActivityResult(ScanContract()) { result ->
            dockActivity.onLoadingFinished()
            if (result.contents == null) {
                Toast.makeText(
                    requireActivity(),
                    "Cancelled",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                args.putString("register_service", "Register")
                args.putString("device_id", result.contents.toString())
                conformRegistrationFragment.arguments = args
                dockActivity.replaceDockableFragment(conformRegistrationFragment)
            }
        }
    }

    private fun setOnClickListener() {
        binding.btnRegister.setOnClickListener {
            // Ensure the fragment is in a valid state before launching the scanner
            if (isAdded) {
                dockActivity.onLoadingStarted()
                barcodeLauncher.launch(ScanOptions())
            } else {
                Log.e("RegistrationFragment", "Fragment not attached yet")
            }
        }
        binding.btnUnRegister.setOnClickListener {
            args.putString("register_service", "UnRegister")
            conformRegistrationFragment.arguments = args
            dockActivity.replaceDockableFragment(conformRegistrationFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        dockActivity.onLoadingFinished()
    }

    override fun setTitleBar(titleBar: TitleBar) {
        super.setTitleBar(titleBar)
        titleBar.showBackButton()
        titleBar.hideNotificationBtn()
    }
}
