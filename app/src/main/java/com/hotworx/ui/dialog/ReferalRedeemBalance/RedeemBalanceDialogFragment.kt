package com.hotworx.ui.dialog.ReferalRedeemBalance

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
import com.hotworx.models.ComposeModel.RefferalDetailModel.Data
import com.hotworx.databinding.FragmentRedeemBalanceDialogBinding
import com.hotworx.helpers.Utils

class RedeemBalanceDialogFragment : DialogFragment() {
    lateinit var binding: FragmentRedeemBalanceDialogBinding
    lateinit var data: Data
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRedeemBalanceDialogBinding.inflate(layoutInflater)
        setData()
        setOnCLickListener()
        return binding.root

    }

    private fun setData(){
        binding.let{
            if (::data.isInitialized){
                it.tvReferalDesc.text = Html.fromHtml( data.redeemed_text ?: """Hey, Warrior! Keep referring friends so that you can unlock Merch Perx! For every friend who joins, you earn $10""", Html.FROM_HTML_MODE_LEGACY)
            } else{
                it.tvReferalDesc.text = ""
                Utils.customToast(context,"Data not found")
            }
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