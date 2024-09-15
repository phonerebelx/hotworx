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
import com.hotworx.activities.DockActivity
import com.hotworx.databinding.FragmentMyHotsquadListBinding
import com.hotworx.global.Constants
import com.hotworx.global.WebServiceConstants
import com.hotworx.helpers.Utils
import com.hotworx.models.HotsquadList.Hotsquad
import com.hotworx.models.HotsquadList.HotsquadListModel
import com.hotworx.models.UserData.getUserData
import com.hotworx.retrofit.GsonFactory
import com.hotworx.ui.adapters.HotsquadListAdapter.SquadListAdapter
import com.hotworx.ui.fragments.BaseFragment
import com.hotworx.ui.views.TitleBar
import com.passio.modulepassio.Singletons.ApiHeaderSingleton.apiHeader

class MyHotsquadListFragment : BaseFragment(), SquadListAdapter.OnItemClickListener {
    private var _binding: FragmentMyHotsquadListBinding? = null
    private val binding get() = _binding!!
    private lateinit var hotsquadListModel: HotsquadListModel
    private var adapter: SquadListAdapter? = null
    private var dashboardShare: String = ""
    private var recordId: String = ""
    private val unreadNotifications = MutableLiveData<String>()

    fun getUnreadNotifications(): LiveData<String> {
        return unreadNotifications
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyHotsquadListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setImageOrData()

        // Retrieve the squad ID from the fragment arguments
        arguments?.let {
            dashboardShare = it.getString("Dashboard_share") ?: ""
            recordId = it.getString("RecordId") ?: ""
            Log.d("squadAccess",dashboardShare.toString())
            Log.d("RecordId",recordId.toString())
        }

        if(dashboardShare == "dashboardShare"){
            binding.btnCreateSquad.visibility = View.GONE
            binding.newUser.visibility = View.GONE
            binding.requestLayout.visibility = View.GONE
        }else{
            binding.btnCreateSquad.visibility = View.VISIBLE
            binding.requestLayout.visibility = View.VISIBLE
            binding.btnCreateSquad.setOnClickListener{
                val addListFragment = AddListFragment()
                dockActivity.replaceDockableFragment(addListFragment)
            }
        }

        getSquadList()

        // Ensure hotsquadListModel is initialized with an empty list to avoid the UninitializedPropertyAccessException
        hotsquadListModel = HotsquadListModel(data = emptyList(),status =false,message = "")

        binding.btnCreateHotsquad.setOnClickListener{
            val addListFragment = AddListFragment()
            dockActivity.replaceDockableFragment(addListFragment)
        }

        binding.cvProperty1.setOnClickListener{
            val sessionProfileSummaryFragment = SessionProfileSummaryFragment()
            dockActivity.replaceDockableFragment(sessionProfileSummaryFragment)
        }

        setAdapter(squadList = hotsquadListModel.data)

        binding.pendingSessions.setOnClickListener{
            val recieverPendingRequestFragment = RecieverPendingRequestFragment()
            dockActivity.replaceDockableFragment(recieverPendingRequestFragment)
        }

        callApi(Constants.PROFILE_API_CALLING)
    }

    private fun getUserDetail(): ArrayList<String> {
        val userName = prefHelper.loginData.full_name.split(" ")

        val arrayString = ArrayList<String>()
        var firstName = userName[0]
        var lastName = if (userName.size > 1) {
            if (userName.size > 2 && userName[1].isEmpty()) {
                userName[2]
            } else {
                userName[1]
            }
        } else {
            ""
        }

        val firstFullName = firstName
        val lastFullName = lastName
        if (firstName.isNotEmpty()) firstName = firstName[0].toString()
        if (lastName.isNotEmpty()) lastName = lastName[0].toString()

        val fullName = firstName + lastName

        arrayString.add(fullName)
        arrayString.add(firstName)
        arrayString.add(lastName)
        arrayString.add(firstFullName)
        arrayString.add(lastFullName)
        return arrayString
    }

    private fun setImageOrData() {
        if (prefHelper.imagePath != null) {
            binding.tvfirstLastName.visibility = View.GONE
            binding.cvImageCard.visibility = View.VISIBLE

            Glide.with(requireContext())
                .load(prefHelper.imagePath)
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
                        binding.tvfirstLastName.text = "${getUserDetail()[1]}${getUserDetail()[2]}"
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
                .into(binding.userImage)
        } else {
            binding.cvImageCard.visibility = View.GONE
            binding.tvfirstLastName.visibility = View.VISIBLE
            binding.tvfirstLastName.text = "${getUserDetail()[1]}${getUserDetail()[2]}"
        }
    }

    override fun onItemClick(item: Hotsquad) {
        // Handle item click here
//        Log.d("MyHotsquadListFragment", "Item clicked: ${item.title}")
    }

    override fun setTitleBar(titleBar: TitleBar) {
        super.setTitleBar(titleBar)
        titleBar.showBackButton()
        titleBar.subHeading = getString(R.string.hotsquad_list)
        titleBar.hidePassioBtn()
        titleBar.showTitleBar()
        getUnreadNotifications().observe(viewLifecycleOwner) { unreadNotificationValue ->
            titleBar.showNotificationBtn(unreadNotificationValue)
            if (unreadNotificationValue == "0") {
                titleBar.hideNotificationText()
            }
        }
    }

    private fun getSquadList() {
        if(dashboardShare == "dashboardShare"){
            getServiceHelper().enqueueCall(
                getWebService().getHotsquadList(
                    apiHeader(requireContext()),
                    recordId
                ), WebServiceConstants.GET_SQUAD_LIST, true
            )
        }else{
            getServiceHelper().enqueueCall(
                getWebService().getHotsquadList(
                    apiHeader(requireContext()),
                    ""
                ), WebServiceConstants.GET_SQUAD_LIST, true
            )
        }
    }

    private fun setAdapter(squadList: List<Hotsquad>) {
        if(dashboardShare == "dashboardShare"){
            adapter = SquadListAdapter(squadList, this, activity as? DockActivity,dashboardShare,recordId)
            binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.recyclerView.adapter = adapter
        }else{
            adapter = SquadListAdapter(squadList, this, activity as? DockActivity,"","")
            binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.recyclerView.adapter = adapter
        }
    }

    override fun ResponseSuccess(result: String?, Tag: String?) {
        when (Tag) {
            WebServiceConstants.GET_SQUAD_LIST -> {
                Log.d("ResponseSuccess", "ResponseSuccess called with result: $result")
                hotsquadListModel = GsonFactory.getConfiguredGson().fromJson(result, HotsquadListModel::class.java)

                if (hotsquadListModel.data.isNullOrEmpty()) {
                    binding.btnCreateSquad.visibility = View.GONE
                    if(dashboardShare == "dashboardShare"){
                        binding.newUser.visibility = View.GONE
                        binding.tvNoListFound.visibility = View.VISIBLE
                    }else{
                        binding.newUser.visibility = View.VISIBLE
                    }
                } else {
                    Log.d("NonEmptyList", "List is not empty, showing items")
                    binding.tvNoListFound.visibility = View.GONE
                    binding.newUser.visibility = View.GONE
                    setAdapter(hotsquadListModel.data)
                }
            }else -> {
                // Handle other cases if necessary
                Log.d("ResponseSuccessss", "Unhandled Tag: $Tag")
            }
        }
    }

    override fun ResponseFailure(message: String?, tag: String?) {
        Log.e("ResponseFailure", "Failed to load squad: $message")
        binding.tvNoListFound.text = getString(R.string.no_squad_found)
        binding.tvNoListFound.visibility = View.VISIBLE
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
                    if(userData.data[0].data.hotsquad_pending_invites == "0"){
                        binding.flView.visibility = View.GONE
                    }else{
                        binding.flView.visibility = View.VISIBLE
                        binding.tvPendingNo.text = userData.data[0].data.hotsquad_pending_invites
                    }

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

    private fun scrollToNotification(listId: String) {
        binding.recyclerView.post {
            val layoutManager = binding.recyclerView.layoutManager as LinearLayoutManager
            val position: Int = adapter?.getPositionById(listId) ?: -1
            if (position != -1) {
                layoutManager.scrollToPositionWithOffset(position, 0)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // unbinder.unbind();
    }
}
