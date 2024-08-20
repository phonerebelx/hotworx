package com.hotworx.ui.fragments.HotsquadList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.hotworx.R
import com.hotworx.Singletons.ApiHeaderSingleton.apiHeader
import com.hotworx.activities.DockActivity
import com.hotworx.databinding.FragmentMyHotsquadListBinding
import com.hotworx.databinding.FragmentPendingInvitesBinding
import com.hotworx.global.WebServiceConstants
import com.hotworx.models.HotsquadList.Hotsquad
import com.hotworx.models.HotsquadList.HotsquadListModel
import com.hotworx.models.HotsquadList.PendingInvitationResponse
import com.hotworx.retrofit.GsonFactory
import com.hotworx.ui.adapters.HotsquadListAdapter.PendingRequestAdapter
import com.hotworx.ui.adapters.HotsquadListAdapter.SquadListAdapter
import com.hotworx.ui.fragments.BaseFragment
import com.hotworx.ui.views.TitleBar

class PendingInvitesFragment : BaseFragment(), PendingRequestAdapter.OnItemClickListener  {

    private var _binding: FragmentPendingInvitesBinding? = null
    private val binding get() = _binding!!
    private lateinit var pendingRequestModel: PendingInvitationResponse
    private var adapter: PendingRequestAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPendingInvitesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getPendingRequestList()

        // Ensure PendingListModel is initialized with an empty list to avoid the UninitializedPropertyAccessException
        pendingRequestModel = PendingInvitationResponse(status =false,message = "",data = emptyList())
        setAdapter(pendingRequestModel.data)
    }

    private fun getPendingRequestList() {
        getServiceHelper().enqueueCall(
            getWebService().getPendingRequestList(
                apiHeader(
                    requireContext()
                )
            ), WebServiceConstants.GET_PENDING_REQUEST_LIST, true
        )
    }

    override fun setTitleBar(titleBar: TitleBar) {
        titleBar.showBackButton()
        titleBar.subHeading = getString(R.string.pending_invite)
    }

    private fun setAdapter(pendingList: List<PendingInvitationResponse.SquadData>) {
        adapter = PendingRequestAdapter(pendingList, this, activity as? DockActivity)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    override fun ResponseFailure(message: String?, tag: String?) {
        binding.tvNoListFound.text = "No Squad List Found"
        binding.tvNoListFound.visibility = View.VISIBLE
    }

    override fun ResponseSuccess(result: String?, Tag: String?) {
        pendingRequestModel = GsonFactory.getConfiguredGson().fromJson(result, PendingInvitationResponse::class.java)

        if (!pendingRequestModel.data.isNullOrEmpty()) {
            binding.tvNoListFound.visibility = View.GONE
            setAdapter(pendingRequestModel.data!!)
        } else {
            binding.tvNoListFound.visibility = View.VISIBLE
            binding.tvNoListFound.text = "No Squad List Found"
        }
    }

    override fun onItemClick(item: PendingInvitationResponse.SquadData) {
        TODO("Not yet implemented")
    }

    private fun scrollToNotification(listId: String) {
        binding.recyclerView.post {
            val layoutManager = binding.recyclerView.layoutManager as LinearLayoutManager
            val position: Int = adapter?.getPositionById(listId) ?: -1
            if (position != -1) {
                layoutManager.scrollToPositionWithOffset(position, 0)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}