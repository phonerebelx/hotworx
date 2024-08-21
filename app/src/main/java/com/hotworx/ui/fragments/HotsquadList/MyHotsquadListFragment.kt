package com.hotworx.ui.fragments.HotsquadList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.hotworx.R
import com.hotworx.Singletons.ApiHeaderSingleton.apiHeader
import com.hotworx.activities.DockActivity
import com.hotworx.databinding.FragmentMyHotsquadListBinding
import com.hotworx.global.WebServiceConstants
import com.hotworx.models.HotsquadList.Hotsquad
import com.hotworx.models.HotsquadList.HotsquadListModel
import com.hotworx.models.NotificationHistory.NotificationHistoryModel
import com.hotworx.models.NotificationHistory.NotificationRead.NotificationReadModel
import com.hotworx.retrofit.GsonFactory
import com.hotworx.ui.adapters.HotsquadListAdapter.SquadListAdapter
import com.hotworx.ui.adapters.NotificationListAdapter
import com.hotworx.ui.fragments.BaseFragment
import com.hotworx.ui.views.TitleBar

class MyHotsquadListFragment : BaseFragment(), SquadListAdapter.OnItemClickListener {
    private var _binding: FragmentMyHotsquadListBinding? = null
    private val binding get() = _binding!!
    private lateinit var hotsquadListModel: HotsquadListModel
    private var adapter: SquadListAdapter? = null
    private var dashboardShare: String = ""
    private var recordId: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyHotsquadListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve the squad ID from the fragment arguments
        arguments?.let {
            dashboardShare = it.getString("Dashboard_share") ?: ""
            recordId = it.getString("RecordId") ?: ""
            Log.d("squadAccess",dashboardShare.toString())
            Log.d("RecordId",recordId.toString())
        }

        if(dashboardShare == "dashboardShare"){
            binding.createSquad.visibility = View.GONE
        }else{
            binding.createSquad.visibility = View.VISIBLE
            binding.createSquad.setOnClickListener{
                val addListFragment = AddListFragment()
                dockActivity.replaceDockableFragment(addListFragment)
                val transaction = fragmentManager?.beginTransaction()
                // Optionally add to back stack
                transaction?.addToBackStack(null)
            }
        }

        getSquadList()

        // Ensure hotsquadListModel is initialized with an empty list to avoid the UninitializedPropertyAccessException
        hotsquadListModel = HotsquadListModel(data = emptyList(),status =false,message = "")

        setAdapter(squadList = hotsquadListModel.data)
    }

    override fun onItemClick(item: Hotsquad) {
        // Handle item click here
//        Log.d("MyHotsquadListFragment", "Item clicked: ${item.title}")
    }

    override fun setTitleBar(titleBar: TitleBar) {
        titleBar.showBackButton()
        titleBar.subHeading = getString(R.string.hotsquad_list)
    }

    private fun getSquadList() {
        getServiceHelper().enqueueCall(
            getWebService().getHotsquadList(
                apiHeader(
                    requireContext()
                )
            ), WebServiceConstants.GET_SQUAD_LIST, true
        )
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
                    Log.d("EmptyList", "List is empty, redirecting to CreateHotsquadFragment")
                    val createHotsquadFragment = CreateHotsquadFragment()
                    dockActivity.replaceDockableFragment(createHotsquadFragment)
                    val transaction = fragmentManager?.beginTransaction()
                    transaction?.remove(this)
                } else {
                    Log.d("NonEmptyList", "List is not empty, showing items")
                    binding.tvNoListFound.visibility = View.GONE
                    setAdapter(hotsquadListModel.data)
                }
            }
            else -> {
                // Handle other cases if necessary
                Log.d("ResponseSuccessss", "Unhandled Tag: $Tag")
            }
        }
    }

    override fun ResponseFailure(message: String?, tag: String?) {
       if(hotsquadListModel.data.isNullOrEmpty()){
           Log.d("EmptyListttt", "List is empty, redirecting to CreateHotsquadFragment")
           val createHotsquadFragment = CreateHotsquadFragment()
           dockActivity.replaceDockableFragment(createHotsquadFragment)
           val transaction = fragmentManager?.beginTransaction()
           transaction?.remove(this)
//           binding.tvNoListFound.text = "No Squad List Found"
//           binding.tvNoListFound.visibility = View.VISIBLE
       }else{}
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
