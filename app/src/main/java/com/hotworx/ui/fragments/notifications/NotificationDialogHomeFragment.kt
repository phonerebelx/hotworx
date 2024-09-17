package com.hotworx.ui.fragments.notifications

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.hotworx.R
import com.hotworx.databinding.FragmentNotificationDialogHomeBinding
import com.hotworx.helpers.ImageLoaderHelper
import com.hotworx.interfaces.OnClickItemListener
import com.hotworx.models.NotificationHistory.NotificationModel


class NotificationDialogHomeFragment(val onItemClick: OnClickItemListener) : DialogFragment() {
    lateinit var binding: FragmentNotificationDialogHomeBinding
    private lateinit var notificationModel: NotificationModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotificationDialogHomeBinding.inflate(inflater, container, false)
        setOnClickListener()
        setUi()

        return binding.root
    }




    private fun setUi(){
        if ( notificationModel.image != null  && notificationModel.image != "" ){
        Glide.with(requireContext())
            .load(notificationModel.image)
            .into(binding.banner)
        }else{
            binding.cvBanner.visibility = View.GONE
            binding.tvTitle.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))

        }
        binding.tvTitle.text = notificationModel.title
        binding.tvDesc.text = notificationModel.body

        Log.d("bkdjbcjksbdjkbcsnf",notificationModel.type.toString())
    }

    fun setNotificationModel(id: String?,title: String?,body: String?,image: String?,type: String?){
        notificationModel =  NotificationModel(id,title,body,image,type)
    }

    private fun setOnClickListener(){
        binding.ivCancel.setOnClickListener {
            onItemClick.onItemClick("","COME_FROM_CLOSE")
            dialog?.dismiss()
        }

        binding.btnDetail.setOnClickListener {
            Log.d("bkdjbcjksbdjkbc",notificationModel.type.toString())
            onItemClick.onItemClick(notificationModel,"COME_FROM_VIEW_DETAIL")
            dialog?.dismiss()
        }

    }

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        dialog?.setCancelable(false)
        dialog?.window?.setGravity(Gravity.CENTER)
        dialog?.window?.attributes = params as WindowManager.LayoutParams
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

}