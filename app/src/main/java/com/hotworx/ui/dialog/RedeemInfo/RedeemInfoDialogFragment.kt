package com.hotworx.ui.dialog.RedeemInfo

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.hotworx.R
import com.hotworx.databinding.FragmentRedeemBalanceDialogBinding
import com.hotworx.databinding.FragmentRedeemInfoDialogBinding
import org.w3c.dom.Text


class RedeemInfoDialogFragment : DialogFragment() {
    lateinit var binding: FragmentRedeemInfoDialogBinding
    lateinit var redeemed_text: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRedeemInfoDialogBinding.inflate(layoutInflater)
        setData()
        setOnCLickListener()
        return binding.root
    }
    private fun setData(){
        binding.let{
            it.tvReferalDesc.text = Html.fromHtml( redeemed_text, Html.FROM_HTML_MODE_LEGACY)
        }
    }

    private fun setOnCLickListener(){
        binding.let {
            it.myImageButton.setOnClickListener {
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