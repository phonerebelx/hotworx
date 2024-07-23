package com.hotworx.ui.dialog.NewActivityDialog

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
import com.hotworx.databinding.FragmentBarDataShowDialogBinding
import com.hotworx.databinding.FragmentNewActivityScreenBinding
import com.hotworx.global.Constants
import com.hotworx.models.NewActivityModels.GraphDataModel
import com.hotworx.models.NewActivityModels.GraphDataModelItem
import com.hotworx.models.SessionBookingModel.FinalSessionBookingModel.PostBookSessionDataModel


class BarDataShowDialog : DialogFragment() {

    lateinit var binding: FragmentBarDataShowDialogBinding
    lateinit var barDataGraphModel: GraphDataModelItem
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBarDataShowDialogBinding.inflate(layoutInflater)
        setData()
        return binding.root
    }

    private fun setData(){
        binding.tvSession.text = barDataGraphModel.session_type
        binding.tvCompleted.text = barDataGraphModel.session_count.toString()
        binding.tvCalorie.text = barDataGraphModel.calories_count.toString()
        binding.TVBalanceRatio.text = barDataGraphModel.ratio.toString()
    }



    override fun onResume() {
        super.onResume()

        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        params?.width = ViewGroup.LayoutParams.WRAP_CONTENT
//        dialog?.setCancelable(false)
        dialog?.window?.setGravity(Gravity.CENTER)
        dialog?.window?.attributes = params as WindowManager.LayoutParams
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}