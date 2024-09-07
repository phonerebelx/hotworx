package com.hotworx.ui.dialog.ReferralUrl

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hotworx.databinding.FragmentReferralurlBinding
import com.hotworx.interfaces.OnClickItemListener
import com.hotworx.models.ComposeModel.RefferalDetailModel.Data
import com.hotworx.ui.adapters.ReferralUrl.ReferralUrlAdapter
import kotlin.properties.Delegates


class ReferralUrlDialogFragment(private val clickListener: OnClickItemListener) : BottomSheetDialogFragment(),OnClickItemListener {

    lateinit var binding: FragmentReferralurlBinding
    lateinit var referralData: List<Data>
    var veriftyIsLocationOrNot by Delegates.notNull<Boolean>()
    lateinit var referralLocationAdapter: ReferralUrlAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentReferralurlBinding.inflate(layoutInflater)
        setCountData()
        Log.d("onCreateView: ",referralData.toString())
        initRecyclerView(referralData)


        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun setCountData(){
        binding.tvLocationCount.text = "URL List (0${referralData.size})"
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
        Log.d("ReferralData", referralData.toString())  // Add this line to log your data
        if (referralData.isNotEmpty()) {
            referralLocationAdapter = ReferralUrlAdapter(requireContext(), this)
            referralLocationAdapter.setList(referralData as ArrayList<Data>)
            binding.rvLocationSender.adapter = referralLocationAdapter
            referralLocationAdapter.notifyDataSetChanged()
        }
    }

    override fun <T> onItemClick(data: T, type: String) {
        val recieveData: Data = data as Data
        clickListener.onItemClick(recieveData,"LOCATION_URL")
        dialog?.dismiss()
    }
}