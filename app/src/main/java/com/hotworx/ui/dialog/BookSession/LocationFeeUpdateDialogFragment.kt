package com.hotworx.ui.dialog.BookSession

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.hotworx.R
import com.hotworx.activities.DockActivity
import com.hotworx.databinding.FragmentLocationFeeUpdateDialogBinding
import com.hotworx.interfaces.OnClickItemListener
import com.hotworx.models.SessionBookingModel.Location

class LocationFeeUpdateDialogFragment : DialogFragment() {

    lateinit var binding: FragmentLocationFeeUpdateDialogBinding
    lateinit var getLocationDetail: Location
    lateinit var dockActivity: DockActivity
    lateinit var onClickItemListener: OnClickItemListener
    lateinit var type: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLocationFeeUpdateDialogBinding.inflate(inflater, container, false)

        setUI()
        setOnClickListener()

        return binding.root
    }

    private fun setUI(){
        binding.tvTitle.text = getLocationDetail.location_name.toString() ?: ""
        binding.tvDescription.text = getLocationDetail.description.toString() ?: ""
        if (getLocationDetail.location_tier == "Premium") {
            Glide.with(requireContext())
                .load(R.drawable.premium)
                .into(binding.ivTier)
        }

        if (getLocationDetail.location_tier == "Elite"){
            Glide.with(requireContext())
                .load(R.drawable.elite)
                .into(binding.ivTier)
        }
    }

    private fun setOnClickListener(){
        binding.apply {
            btnCancel.setOnClickListener {
                dialog?.cancel()
            }
            btnContinue.setOnClickListener {
                getLocationDetail.type = type
                onClickItemListener.onItemClick(getLocationDetail,"COME_FROM_DIALOG_FRAGMENT")
                dialog?.cancel()
            }
        }
    }


    fun setCustomOnClickStringTypeListener(listener: OnClickItemListener) {
        onClickItemListener = listener
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
