package com.hotworx.ui.fragments.HotsquadList.Bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hotworx.databinding.BottomSheetSearchuserBinding


class SearchUserBottomSheet(private val itemClickListener: OnItemClickListener): BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetSearchuserBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetSearchuserBinding.inflate(layoutInflater)
        return binding.root
    }
    companion object {
        const val TAG = "SearchUserBottomSheet"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        dialog.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout = bottomSheetDialog.findViewById<View>(
                com.google.android.material.R.id.design_bottom_sheet
            )
            parentLayout?.let { bottomSheet ->
                val behaviour = BottomSheetBehavior.from(bottomSheet)
                val layoutParams = bottomSheet.layoutParams
                layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
                bottomSheet.layoutParams = layoutParams
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        /* Set Adapter */
//        setAdapter()
        
        /**
         * Filter List Locally on Text Change
         */
//        binding.etSearch.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                if (s.isNotEmpty() && s != "")
//                    search(s.toString())
//            }
//            override fun afterTextChanged(s: Editable) {}
//        })

    }

    interface OnItemClickListener {
//        fun onItemClick(item: TourTypeResponse.Type) {}
    }
}