package com.hotworx.ui.fragments.HotsquadList

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.core.content.ContextCompat
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.hotworx.R
import com.hotworx.Singletons.ApiHeaderSingleton
import com.hotworx.databinding.FragmentSquadAcceptedMemberBinding
import com.hotworx.global.Constants
import com.hotworx.models.ErrorResponseEnt
import com.hotworx.models.HotsquadList.RemoveMemberResponse
import com.hotworx.models.HotsquadList.SquadMemberDetailsResponse
import com.hotworx.models.HotsquadList.removeSquadMemberRequest
import com.hotworx.models.HotsquadList.squadMemberDetailRequest
import com.hotworx.retrofit.GsonFactory
import com.hotworx.ui.adapters.HotsquadListAdapter.SquadMemberListAdapter
import com.hotworx.ui.fragments.BaseFragment
import com.hotworx.ui.views.TitleBar
import java.net.URLDecoder


class squadAcceptedMemberFragment : BaseFragment(), SquadMemberListAdapter.OnItemClickListener {

    private var _binding: FragmentSquadAcceptedMemberBinding? = null
    private val binding get() = _binding!!
    private var adapter: SquadMemberListAdapter? = null
    private var squadId: String = ""
    var squadAccess = false
    private var userListForServer = mutableListOf<String>()
    private lateinit var memberListModel: SquadMemberDetailsResponse.SquadData

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSquadAcceptedMemberBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve the squad ID from the fragment arguments
        arguments?.let {
            squadId = it.getString("squad_id") ?: ""
            squadAccess = it.getBoolean("squad_access")
            Log.d("SquadID", squadId)
            Log.d("squadAccess",squadAccess.toString())
        }

        callInvitationApi(Constants.GET_SQUAD_MEMBER_LIST, "")
    }

    override fun onItemClick(item: SquadMemberDetailsResponse.SquadData.Member) {
        // Handle member item click event here
    }

    private fun callInvitationApi(type: String, data: String) {
        when (type) {
            Constants.GET_SQUAD_MEMBER_LIST -> {
                val request = squadMemberDetailRequest(squadId)

                getServiceHelper()?.enqueueCallExtended(
                    getWebService()?.getSquadDetail(
                        ApiHeaderSingleton.apiHeader(requireContext()),
                        request
                    ), Constants.GET_SQUAD_MEMBER_LIST, true
                )
            }

            Constants.REMOVE_SQUAD_MEMBER -> {
                val gson = GsonBuilder()
                    .disableHtmlEscaping() // Ensures special characters like `=` are not escaped
                    .create()

                val decodedMemberList = userListForServer.map { encodedMemberId ->
                    URLDecoder.decode(encodedMemberId, "UTF-8")
                }
                Log.d("DecodedMemberList", decodedMemberList.toString())

                val request = removeSquadMemberRequest(squadId, decodedMemberList)
                val jsonRequest = gson.toJson(request)
                Log.d("SerializedRequest", jsonRequest)

                // Now send the request
                getServiceHelper()?.enqueueCallExtended(
                    getWebService()?.removeSquadMember(
                        ApiHeaderSingleton.apiHeader(requireContext()),
                        request
                    ), Constants.REMOVE_SQUAD_MEMBER, true
                )
            }
        }
    }

    override fun onSuccess(liveData: LiveData<String>, tag: String) {
        super.onSuccess(liveData, tag)
        when (tag) {
            Constants.GET_SQUAD_MEMBER_LIST -> {
                val responseJson = liveData.value
                Log.d("Response", "LiveData value: $responseJson")

                if (responseJson != null) {
                    try {
                        val response = GsonFactory.getConfiguredGson()?.fromJson(responseJson, SquadMemberDetailsResponse::class.java)
                        if (response?.status == true) {
                            val members = response.data.members

                            if (members.isNotEmpty()) {
                                val pendingMembers = members.filter { it.invite_status == "accepted" }.toMutableList()

                                if (pendingMembers.isNotEmpty()) {
                                    setAdapter(pendingMembers)
                                } else {
                                    binding.tvNoListFound.text = getString(R.string.no_squad_members_found)
                                    binding.tvNoListFound.visibility = View.VISIBLE
                                }
                            } else {
                                binding.tvNoListFound.text = getString(R.string.no_squad_members_found)
                                binding.tvNoListFound.visibility = View.VISIBLE
                            }
                        } else {
                            dockActivity?.showErrorMessage("Something Went Wrong")
                        }
                    } catch (e: Exception) {
                        val genericMsgResponse = GsonFactory.getConfiguredGson()
                            ?.fromJson(responseJson, ErrorResponseEnt::class.java)
                        dockActivity?.showErrorMessage(genericMsgResponse?.error.toString())
                        Log.i("Error", e.message.toString())
                    }
                } else {
                    Log.e("Error", "LiveData value is null")
                    dockActivity?.showErrorMessage("No response from server")
                }
            }

            Constants.REMOVE_SQUAD_MEMBER -> {
                val responseJson = liveData.value
                Log.d("Response", "LiveData value: $responseJson")

                if (responseJson != null) {
                    try {
                        val response = GsonFactory.getConfiguredGson()?.fromJson(responseJson, RemoveMemberResponse::class.java)
                        if (response?.status == true) {
                            dockActivity?.showSuccessMessage(response.message)
                        } else {
                            dockActivity?.showErrorMessage("Failed to remove member")
                        }
                    } catch (e: Exception) {
                        val genericMsgResponse = GsonFactory.getConfiguredGson()
                            ?.fromJson(responseJson, ErrorResponseEnt::class.java)
                        dockActivity?.showErrorMessage(genericMsgResponse?.error.toString())
                        Log.i("Error", e.message.toString())
                    }
                } else {
                    Log.e("Error", "LiveData value is null")
                    dockActivity?.showErrorMessage("No response from server")
                }
            }
        }
    }

    private fun setAdapter(members: MutableList<SquadMemberDetailsResponse.SquadData.Member>) {
        adapter = SquadMemberListAdapter(members, requireContext(), preferenceHelper = prefHelper,object : SquadMemberListAdapter.OnItemClickListener {
            override fun onItemClick(item: SquadMemberDetailsResponse.SquadData.Member) {
                item?.let {
                    if (item.selected) {
                        userListForServer.add(it.member_id)
                    } else {
                        userListForServer.remove(it.member_id)
                    }
                }
            }
        })
        // Set up the RecyclerView

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        if(squadAccess){
            // Attach ItemTouchHelper to RecyclerView
            val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
            itemTouchHelper.attachToRecyclerView(binding.recyclerView)
        }else{
        }
    }

    private fun updateAdapterList(newList: MutableList<SquadMemberDetailsResponse.SquadData.Member>) {
        adapter?.updateData(newList)
    }

    private fun onItemActionSuccess(position: Int) {
        adapter?.let {
            if (position >= 0 && position < it.itemCount) {
                it.removeItem(position)
            }
        }
    }

    override fun ResponseFailure(message: String?, tag: String?) {
        Log.e("ResponseFailure", "Failed to load squad members: $message")
        binding.tvNoListFound.text = getString(R.string.no_squad_members_found)
        binding.tvNoListFound.visibility = View.VISIBLE
    }

    override fun setTitleBar(titleBar: TitleBar) {
        titleBar.showBackButton()
        titleBar.subHeading = getString(R.string.squad_members)
        titleBar.hidePassioBtn()
    }

    @SuppressLint("ResourceAsColor")
    private val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

        private var deleteIcon: Drawable? = null
        private var background: ColorDrawable? = null
        private var backgroundColor: Int = R.color.red
        private val swipeThreshold: Float = 0.1f // Swipe threshold to trigger background drawing

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            showConfirmationDialog(position)
        }

        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            val itemView = viewHolder.itemView
            val context = viewHolder.itemView.context // Safe context access

            // Initialize drawable and background color if null
            if (deleteIcon == null || background == null) {
                deleteIcon = ContextCompat.getDrawable(context, R.drawable.deleteicon)
                backgroundColor = ContextCompat.getColor(context, R.color.red)
                background = ColorDrawable(backgroundColor)
            }

            // Check if swipe distance exceeds threshold
            val isSwipeBeyondThreshold = Math.abs(dX) > itemView.width * swipeThreshold

            // Draw red background only if swiped beyond threshold
            if (dX < 0 && isSwipeBeyondThreshold) { // Swiping left and beyond threshold
                background?.setBounds(
                    itemView.right + dX.toInt(),
                    itemView.top,
                    itemView.right,
                    itemView.bottom
                )
                background?.draw(c)

                // Draw delete icon
                deleteIcon?.let { icon ->
                    val iconMargin = (itemView.height - icon.intrinsicHeight) / 2
                    val iconTop = itemView.top + iconMargin
                    val iconBottom = iconTop + icon.intrinsicHeight

                    icon.setBounds(
                        itemView.right - iconMargin - icon.intrinsicWidth,
                        iconTop,
                        itemView.right - iconMargin,
                        iconBottom
                    )
                    icon.draw(c)
                }
            }

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }

        private fun showConfirmationDialog(position: Int) {
            context?.let {
                AlertDialog.Builder(it)
                    .setTitle("Confirm Delete")
                    .setMessage("Are you sure you want to delete this item?")
                    .setPositiveButton("Yes") { _, _ ->
                        // Proceed with deletion
                        handleDelete(position)
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        // Undo the swipe
                        adapter?.notifyItemChanged(position)
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            }
        }

        private fun handleDelete(position: Int) {
            val member = adapter?.items?.get(position)
            member?.let {
                // Add the member ID to the list for removal
                userListForServer.add(it.member_id)

                // Remove the item from the adapter
                adapter?.removeItem(position)

                // Call the API to remove the member
                callInvitationApi(Constants.REMOVE_SQUAD_MEMBER, "")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
