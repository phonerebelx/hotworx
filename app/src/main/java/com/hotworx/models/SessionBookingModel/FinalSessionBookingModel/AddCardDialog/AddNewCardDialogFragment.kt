package com.hotworx.models.SessionBookingModel.FinalSessionBookingModel.AddCardDialog

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
import com.hotworx.R
import com.hotworx.databinding.FragmentAddNewCardDialogBinding
import com.hotworx.databinding.FragmentReconformBookingDialogBinding
import com.hotworx.interfaces.OnClickItemListener
import com.hotworx.models.SessionBookingModel.FinalSessionBookingModel.BookSessionWebModel.WebViewUrlModel


class AddNewCardDialogFragment(var clickListener: OnClickItemListener) : DialogFragment() {
    lateinit var binding: FragmentAddNewCardDialogBinding
    lateinit var getWebViewUrlModel: WebViewUrlModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAddNewCardDialogBinding.inflate(inflater, container, false)
        setUI()
        setOnClickListener()
        return binding.root
    }

    private fun setUI(){
        binding.tvCardNoDetail.text = "xxxx xxxx xxxx ${getWebViewUrlModel.card_number}"
    }

    private fun setOnClickListener(){
        binding.let {
            it.btnCancel.setOnClickListener {
                clickListener.onItemClick("","COME_FROM_RECONFORM_BOOKING_CANCEL_REQUEST_CHECK_CARD")
                dialog?.dismiss()
            }

            it.btnContinue.setOnClickListener {
                clickListener.onItemClick("","COME_FROM_RECONFORM_BOOKING_CONTINUE_REQUEST_CHECK_CARD")
                dialog?.dismiss()
            }
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