package com.hotworx.ui.fragments.notifications.ReadNotification.LargeImageView

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.hotworx.R
import com.hotworx.databinding.FragmentLargeImageViewDialogBinding
import com.hotworx.helpers.ImageLoaderHelper
import com.hotworx.models.NotificationHistory.Data

class LargeImageViewDialogFragment : DialogFragment() {

    lateinit var binding: FragmentLargeImageViewDialogBinding
    lateinit var notificationData: Data
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLargeImageViewDialogBinding.inflate(inflater, container, false)
        setUI()
        setOnClickListener()
        return binding.root
    }

    private fun setUI(){

        Glide.with(requireContext())
            .load(notificationData.image_url)
            .into(binding.ivNotification)
    }


    private fun setOnClickListener(){
        binding.ivCancel.setOnClickListener {
            dialog?.dismiss()
        }
        binding.btnClose.setOnClickListener {
            dialog?.dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        params?.width = ViewGroup.LayoutParams.MATCH_PARENT
        dialog?.setCancelable(false)
        dialog?.window?.setGravity(Gravity.CENTER)
        dialog?.window?.attributes = params as WindowManager.LayoutParams
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }


}