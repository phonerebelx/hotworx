package com.hotworx.ui.fragments.HotsquadList

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.hotworx.R
import com.hotworx.Singletons.ApiHeaderSingleton
import com.hotworx.Singletons.ApiHeaderSingleton.apiHeader
import com.hotworx.databinding.FragmentSessionProfileSummaryBinding
import com.hotworx.global.Constants
import com.hotworx.global.WebServiceConstants
import com.hotworx.helpers.Utils
import com.hotworx.models.HotsquadList.Session.UserActivitiesResponse
import com.hotworx.models.UserData.getUserData
import com.hotworx.retrofit.GsonFactory
import com.hotworx.ui.adapters.HotsquadListAdapter.Sessions.SessionProfileHighlightAdapter
import com.hotworx.ui.adapters.HotsquadListAdapter.Sessions.SessionProfileMemberAdapter
import com.hotworx.ui.fragments.BaseFragment
import com.hotworx.ui.views.TitleBar

class SessionProfileSummaryFragment : BaseFragment(){

    private var _binding: FragmentSessionProfileSummaryBinding? = null
    private val binding get() = _binding!!
    private lateinit var summaryProfile: UserActivitiesResponse
    private val highlightList = mutableListOf<UserActivitiesResponse.UserData.Highlight>()
    private val activityList = mutableListOf<UserActivitiesResponse.UserData.Activity>()
    private var adapter: SessionProfileHighlightAdapter? = null
    private var adapterActivity: SessionProfileMemberAdapter? = null

    private val unreadNotifications = MutableLiveData<String>()

    fun getUnreadNotifications(): LiveData<String> {
        return unreadNotifications
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSessionProfileSummaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        callInvitationApi(WebServiceConstants.GET_SESSION_PROFILE, "")
        callApi(Constants.PROFILE_API_CALLING)
        setAdapter(highlightList)
        setActivityAdapter(activityList)
    }

    private fun callInvitationApi(type: String, data: String) {
        getServiceHelper()?.enqueueCall(
            getWebService()?.getSessionUserProfile(
                ApiHeaderSingleton.apiHeader(requireContext()),
            ),  WebServiceConstants.GET_SESSION_PROFILE, true
        )
    }

    override fun ResponseSuccess(result: String?, tag: String?) {
        if (!isAdded) return // Safeguard to prevent updates if fragment is not added

        summaryProfile = GsonFactory.getConfiguredGson().fromJson(result, UserActivitiesResponse::class.java)

        binding.tvName.text = summaryProfile.data.profile_info.name
        binding.tvPhone.text = summaryProfile.data.profile_info.email
        binding.tvEmail.text = summaryProfile.data.profile_info.phone

        if (summaryProfile.data.profile_info.image!= null) {
            binding.tvfirstLastName.visibility = View.GONE
            binding.cvImageCard.visibility = View.VISIBLE

            Glide.with(requireContext())
                .load(summaryProfile.data.profile_info.image)
                .listener(object : RequestListener<Drawable> {
                    @SuppressLint("SetTextI18n")
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: com.bumptech.glide.request.target.Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {

                        binding.cvImageCard.visibility = View.GONE
                        binding.tvfirstLastName.visibility = View.VISIBLE
                        binding.tvfirstLastName.text = getUserInitials(summaryProfile.data.profile_info.name)
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: com.bumptech.glide.request.target.Target<Drawable>?,
                        dataSource: com.bumptech.glide.load.DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {

                        return false
                    }
                })
                .into(binding.imgIcon)
        } else {
            binding.cvImageCard.visibility = View.GONE
            binding.tvfirstLastName.visibility = View.VISIBLE
            binding.tvfirstLastName.text = getUserInitials(summaryProfile.data.profile_info.name)
        }
        // Update highlights if present
        if (!summaryProfile.data.highlights.isNullOrEmpty()) {
            highlightList.clear()
            highlightList.addAll(summaryProfile.data.highlights)
            adapter?.notifyDataSetChanged()
        }

        // Update activities if present
        if (!summaryProfile.data.activities.isNullOrEmpty()) {
            activityList.clear()
            activityList.addAll(summaryProfile.data.activities)
            adapterActivity?.notifyDataSetChanged()
        }
    }

    // Helper function to get user initials
    private fun getUserInitials(fullName: String?): String {
        val nameParts = fullName?.split(" ") ?: return ""
        val firstNameInitial = nameParts.getOrNull(0)?.firstOrNull()?.toString() ?: ""
        val lastNameInitial = nameParts.getOrNull(1)?.firstOrNull()?.toString() ?: ""
        return firstNameInitial + lastNameInitial
    }

    override fun ResponseFailure(message: String?, tag: String?) {
        Log.e("ResponseFailure", "Failed to load squad members: $message")
    }

    private fun setAdapter(members: MutableList<UserActivitiesResponse.UserData.Highlight>) {
        adapter = SessionProfileHighlightAdapter(members, requireContext(),object : SessionProfileHighlightAdapter.OnItemClickListener {
            override fun onItemClick(item: UserActivitiesResponse.UserData.Highlight, position: Int) {
                Log.d("testing","testing data")
            }
        })
        binding.recyclerViewHighlight.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewHighlight.adapter = adapter
    }

    private fun setActivityAdapter(members: MutableList<UserActivitiesResponse.UserData.Activity>) {
        adapterActivity = SessionProfileMemberAdapter(members, requireContext(),object : SessionProfileMemberAdapter.OnItemClickListener {
            override fun onItemClick(item: UserActivitiesResponse.UserData.Activity, position: Int) {
                Log.d("testing","testing dataaaaaaaa")
            }
        })
        binding.recyclerViewActivity.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewActivity.adapter = adapterActivity
    }

    private fun callApi(type: String) {
        when (type) {
            Constants.PROFILE_API_CALLING -> getServiceHelper().enqueueCallExtended(
                getWebService().viewProfile(
                    apiHeader(requireContext())
                ), Constants.PROFILE_API_CALLING, true
            )
        }
    }

    override fun onSuccess(liveData: LiveData<String>, tag: String) {
        super.onSuccess(liveData, tag)
        when (tag) {
            Constants.PROFILE_API_CALLING -> {
                try {
                    val userData = GsonFactory.getConfiguredGson().fromJson(liveData.value, getUserData::class.java)
                    if (userData.data != null && !userData.data.isEmpty() && userData.data.get(0).data != null && userData.data.get(0).data.unread_notifications != null){
                        unreadNotifications.setValue(userData.data[0].data.unread_notifications!!)
                    } else {
                        unreadNotifications.setValue("0")
                    }
                } catch (e: Exception) {
                    Utils.customToast(requireContext(), resources.getString(R.string.error_failure))
                }
            }
        }
    }

    override fun setTitleBar(titleBar: TitleBar) {
        super.setTitleBar(titleBar)
        titleBar.showBackButton()
        titleBar.showTitleBar()
        getUnreadNotifications().observe(viewLifecycleOwner) { unreadNotificationValue ->
            titleBar.showNotificationBtn(unreadNotificationValue)
            if (unreadNotificationValue == "0") {
                titleBar.hideNotificationText()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
