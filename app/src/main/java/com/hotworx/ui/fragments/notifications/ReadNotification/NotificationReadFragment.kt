package com.hotworx.ui.fragments.notifications.ReadNotification

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hotworx.R
import com.hotworx.Singletons.ApiHeaderSingleton
import com.hotworx.databinding.FragmentMyReferralAdapterBinding
import com.hotworx.databinding.FragmentNitificationReadBinding
import com.hotworx.databinding.FragmentNotificationBinding
import com.hotworx.databinding.FragmentRefferalDetailBinding
import com.hotworx.global.Constants
import com.hotworx.helpers.ServiceHelper
import com.hotworx.models.NotificationHistory.Data
import com.hotworx.retrofit.WebService
import com.hotworx.retrofit.WebServiceFactory

class NotificationReadFragment : DialogFragment() {

    lateinit var binding: FragmentNitificationReadBinding

    lateinit var notificationData: Data

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNitificationReadBinding.inflate(inflater, container, false)
        setOnClickListener()
        setUi()
      return binding.root
    }

    private fun setUi() {
        if (notificationData.image_url != null){
            Glide.with(requireContext())
                .load(notificationData.image_url)
                .into(binding.ivNotification)
        }

        binding.tvBooking.text = notificationData.title
        binding.tvDesc.text = notificationData.body
    }

    private fun setOnClickListener(){
        binding.ivCancel.setOnClickListener {
            dialog?.dismiss()
        }
        binding.btnThnks.setOnClickListener {
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