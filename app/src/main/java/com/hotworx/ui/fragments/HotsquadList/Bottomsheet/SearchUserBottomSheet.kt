package com.hotworx.ui.fragments.HotsquadList.Bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hotworx.R
import com.hotworx.databinding.BottomSheetSearchuserBinding
import com.hotworx.models.HotsquadList.RegisteredMemberItem
import com.hotworx.ui.adapters.HotsquadListAdapter.SearchRegisteredAdapter



class SearchUserBottomSheet(): BottomSheetDialogFragment() {

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
                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
                bottomSheet.layoutParams = layoutParams
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        /* Set Adapter */
        setAdapter()

    }



    private fun setAdapter() {

        val registeredMemberList = listOf(
            RegisteredMemberItem("John smith", "03242788955", "",R.drawable.registeredmember),
            RegisteredMemberItem("John smith", "", "john.smaith@gmail.com",R.drawable.registeredmember),
            RegisteredMemberItem("John smith", "03242788955", "",R.drawable.registeredmember),
            RegisteredMemberItem("John smith", "03242788955", "",R.drawable.registeredmember),
        )

        val adapter = SearchRegisteredAdapter(registeredMemberList)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    interface OnItemClickListener {
//        fun onItemClick(item: TourTypeResponse.Type) {}
    }
}