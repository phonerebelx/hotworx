package com.hotworx.ui.dialog.NewActivityDialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.hotworx.R
import com.hotworx.databinding.FragmentRefersquadInviteDialogBinding

class ReferSquadInviteDialogFragment : DialogFragment() {

    lateinit var binding: FragmentRefersquadInviteDialogBinding
    private var referralUrl: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRefersquadInviteDialogBinding.inflate(inflater, container, false)

        arguments?.let {
            referralUrl = it.getString("REFERRAL_URL")
        }

        setData()
        setOnClickListener()
        return binding.root
    }

    private fun setData(){
        binding.let{
            referralUrl?.let { url ->
               Log.d("bjbdjkbjkb",url.toString())
            }
        }
    }

    private fun setOnClickListener(){
        binding.btnSendReferral.setOnClickListener {
            // Implement what happens when the referral is sent
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

    companion object {
        fun newInstance(referralUrl: String): ReferSquadInviteDialogFragment {
            val fragment = ReferSquadInviteDialogFragment()
            val args = Bundle()
            args.putString("REFERRAL_URL", referralUrl)
            fragment.arguments = args
            return fragment
        }
    }
}
