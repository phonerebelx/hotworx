package com.hotworx.ui.fragments.notifications.ReadNotification

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.hotworx.R
import com.hotworx.databinding.FragmentNitificationReadBinding
import com.hotworx.models.NotificationHistory.Data


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

        binding.attachmentImg.setOnClickListener(View.OnClickListener {

            val attachmentUrl = notificationData.attachment_url

            if (!attachmentUrl.isNullOrEmpty()) {
                try {
                    val uri = Uri.parse(attachmentUrl)
                    val httpIntent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(httpIntent)
                } catch (e: Exception) {
                    Toast.makeText(context, "Invalid Attachment Link!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "No Attachment Link Found!", Toast.LENGTH_SHORT).show()
            }

        })
        return binding.root
    }

    private fun setUi() {
        if (notificationData.image_url != null){
            binding.imgRect.visibility = View.VISIBLE
            Glide.with(requireContext())
                .load(notificationData.image_url)
                .into(binding.banner)
        }else{
            binding.imgRect.visibility = View.GONE
        }

        binding.title.text = notificationData.title
        binding.detail.text = notificationData.body
        binding.time.text = notificationData.sent_at

        Glide.with(requireContext())
            .load(notificationData.image_url)
            .into(binding.banner)
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