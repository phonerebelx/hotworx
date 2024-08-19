package com.hotworx.ui.dialog.ReferralLocation

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hotworx.R
import com.hotworx.databinding.FragmentReferralLocationBinding
import com.hotworx.interfaces.OnClickItemListener
import com.hotworx.models.ComposeModel.RefferalDetailModel.Data
import com.hotworx.ui.adapters.ReferralLocation.ReferralLocationAdapter
import kotlin.properties.Delegates


class ReferralLocationDialogFragment(private val clickListener: OnClickItemListener) : BottomSheetDialogFragment(),OnClickItemListener {

    lateinit var binding: FragmentReferralLocationBinding
    lateinit var referralData: List<Data>
    var veriftyIsLocationOrNot by Delegates.notNull<Boolean>()
    lateinit var referralLocationAdapter: ReferralLocationAdapter
    var utm = ""
    var titleValue = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentReferralLocationBinding.inflate(layoutInflater)

        arguments?.let { bundle ->
            utm = bundle.getString("UTMVALUE")?.trim() ?: ""
            Log.d("jskjhjkh",utm)
        }

        setCountData()
        initRecyclerView(referralData)
        Log.d("onCreateView: ",referralData.toString())

        return binding.root
    }


    @SuppressLint("SetTextI18n")
    private fun setCountData(){
        val countText = if (utm.equals("UTM", ignoreCase = true)) {
            "UTM(${referralData.size})"
        } else {
            "Location(${referralData.size})"
        }
        binding.tvLocationCount.text = countText
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val offsetFromTop = 200
        (dialog as? BottomSheetDialog)?.behavior?.apply {
            isFitToContents = true

            state = BottomSheetBehavior.STATE_EXPANDED
        }

    }

    private fun initRecyclerView(referralData: List<Data>){
        if (referralData.size > 0) {
            referralLocationAdapter = ReferralLocationAdapter(requireContext(), this)
            referralLocationAdapter.setList(referralData as ArrayList<com.hotworx.models.ComposeModel.RefferalDetailModel.Data>)
            binding.rvLocationSender.adapter = referralLocationAdapter
        }
    }

    override fun <T> onItemClick(data: T, type: String) {
        val recieveData: Data = data as Data
        if (veriftyIsLocationOrNot){

        clickListener.onItemClick(recieveData,"LOCATION_DETAIL")

        }else{
            clickListener.onItemClick(recieveData,"UTM_DETAIL")

        }

        dialog?.dismiss()
    }



}