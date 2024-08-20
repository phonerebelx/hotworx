package com.hotworx.ui.fragments.HotsquadList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.hotworx.R
import com.hotworx.Singletons.ApiHeaderSingleton
import com.hotworx.Singletons.ApiHeaderSingleton.apiHeader
import com.hotworx.activities.DockActivity
import com.hotworx.databinding.FragmentSquadMemberDetailBinding
import com.hotworx.global.Constants
import com.hotworx.global.WebServiceConstants
import com.hotworx.models.ErrorResponseEnt
import com.hotworx.models.HotsquadList.SquadMemberDetailsResponse
import com.hotworx.models.HotsquadList.squadMemberDetailRequest
import com.hotworx.retrofit.GsonFactory
import com.hotworx.ui.adapters.HotsquadListAdapter.SquadMemberListAdapter
import com.hotworx.ui.fragments.BaseFragment
import com.hotworx.ui.views.TitleBar

class SquadMemberDetailFragment : BaseFragment(), SquadMemberListAdapter.OnItemClickListener {

    private var _binding: FragmentSquadMemberDetailBinding? = null
    private val binding get() = _binding!!
    private var adapter: SquadMemberListAdapter? = null
    private var squadId: String = ""
    private lateinit var memberListModel: SquadMemberDetailsResponse.SquadData

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSquadMemberDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve the squad ID from the fragment arguments
        arguments?.let {
            squadId = it.getString("squad_id") ?: ""
            Log.d("SquadID", squadId)
        }

        callInvitationApi(Constants.GET_SQUAD_MEMBER_LIST, "")
    }

    override fun onItemClick(item: SquadMemberDetailsResponse.SquadData.Member) {
        // Handle member item click event here
    }

    private fun fetchSquadMembers() {
        if (squadId.isNotEmpty()) {
            val request = squadMemberDetailRequest(squadId)

            getServiceHelper().enqueueCall(
                getWebService().getSquadDetail(
                    apiHeader(requireContext()),
                    request
                ), WebServiceConstants.GET_SQUAD_MEMBER_LIST, true
            )
        } else {
            Log.e("SquadMemberDetailFragment", "Squad ID is empty")
        }
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
                        val response = GsonFactory.getConfiguredGson()?.fromJson(responseJson, SquadMemberDetailsResponse::class.java)!!
                        if (response.status) {
                            binding.tvNoListFound.visibility = View.GONE
                            setAdapter(response.data.members)
                        } else {
                            dockActivity?.showErrorMessage("Something Went Wrong")
                        }
                    } catch (e: Exception) {
                        val genericMsgResponse = GsonFactory.getConfiguredGson()
                            ?.fromJson(responseJson, ErrorResponseEnt::class.java)!!
                        dockActivity?.showErrorMessage(genericMsgResponse.error.toString())
                        Log.i("Error", e.message.toString())
                    }
                } else {
                    Log.e("Error", "LiveData value is null")
                    dockActivity?.showErrorMessage("No response from server")
                }
            }
        }
    }

    private fun setAdapter(members: List<SquadMemberDetailsResponse.SquadData.Member>) {
        adapter = SquadMemberListAdapter(members, this, activity as? DockActivity)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    override fun ResponseFailure(message: String?, tag: String?) {
        Log.e("ResponseFailure", "Failed to load squad members: $message")
        binding.tvNoListFound.text = getString(R.string.no_squad_members_found)
        binding.tvNoListFound.visibility = View.VISIBLE
    }

    override fun setTitleBar(titleBar: TitleBar) {
        titleBar.showBackButton()
        titleBar.subHeading = getString(R.string.squad_members)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}