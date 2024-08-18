package com.hotworx.ui.fragments.HotsquadList.Bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hotworx.databinding.BottomSheetSearchuserBinding
import com.hotworx.models.HotsquadList.FoundUser
import com.hotworx.models.HotsquadList.NotFoundUser
import com.hotworx.models.HotsquadList.SearchUserModel
import com.hotworx.retrofit.GsonFactory
import com.hotworx.ui.adapters.HotsquadListAdapter.SearchNotFoundUserAdapter
import com.hotworx.ui.adapters.HotsquadListAdapter.SearchRegisteredAdapter



class SearchUserBottomSheet(): BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetSearchuserBinding
    private var foundUserListForServer = mutableListOf<String>()
    private var notfoundUserListForServer = mutableListOf<String>()

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

        val responseString = arguments?.getString("response")
        Log.d("Response String", "Response String from arguments: $responseString")

        if (responseString != null) {
            val response = GsonFactory.getConfiguredGson()?.fromJson(responseString, SearchUserModel::class.java)
            Log.d("ParsedResponse", "Parsed Response: $response")

            if (response?.status == true) {
                val foundUserList = response.data?.foundUser ?: emptyList()
                val notFoundUserList = response.data?.notFoundUser ?: emptyList()
                setFoundUserAdapter(foundUserList)
                setNotFoundUserAdapter(notFoundUserList)
            } else {
                Log.e("Error", "Response status is false or null")
            }
        } else {
            Log.e("Error", "Response String is null")
        }
    }

    private fun setNotFoundUserAdapter(notFoundUserList: List<NotFoundUser>) {
        val adapter = SearchNotFoundUserAdapter(notFoundUserList, requireContext(), object : SearchNotFoundUserAdapter.OnItemClickListener {
            override fun onItemClick(item: NotFoundUser) {
                item?.let {
                    if (item.selected) {
                        notfoundUserListForServer.add(it.referralInviteId)
                    } else {
                        notfoundUserListForServer.remove(it.referralInviteId)
                    }
                    Log.d(TAG, "notfoundUserList ${notfoundUserListForServer.toString()}")
                }
            }
        })

        binding.recyclerViewContact.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewContact.adapter = adapter
    }

    private fun setFoundUserAdapter(foundUserList: List<FoundUser>) {
        val adapter = SearchRegisteredAdapter(foundUserList, requireContext(), object : SearchRegisteredAdapter.OnItemClickListener {
            override fun onItemClick(item: FoundUser) {
                item?.let {
                    if (item.selected) {
                        foundUserListForServer.add(it.squadInviteId)
                    } else {
                        foundUserListForServer.remove(it.squadInviteId)
                    }
                    Log.d(TAG, "foundUserList ${foundUserListForServer.toString()}")
                }
            }
        })

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }



    interface OnItemClickListener {
//        fun onItemClick(item: TourTypeResponse.Type) {}
    }
}