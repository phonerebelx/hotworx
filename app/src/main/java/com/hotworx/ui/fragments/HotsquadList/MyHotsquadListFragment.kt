package com.hotworx.ui.fragments.HotsquadList

import android.os.Bundle
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
import com.hotworx.retrofit.GsonFactory
import com.hotworx.ui.adapters.HotsquadListAdapter.SquadListAdapter
import com.hotworx.ui.fragments.BaseFragment
import com.hotworx.ui.views.TitleBar

class MyHotsquadListFragment : BaseFragment(), SquadListAdapter.OnItemClickListener {
    private var _binding: FragmentMyHotsquadListBinding? = null
    private val binding get() = _binding!!
    private lateinit var hotsquadListModel: HotsquadListModel
    private var adapter: SquadListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyHotsquadListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getSquadList()

        // Ensure hotsquadListModel is initialized with an empty list to avoid the UninitializedPropertyAccessException
        hotsquadListModel = HotsquadListModel(data = emptyList(),status =false,message = "")
        setAdapter(squadList = hotsquadListModel.data)

        binding.createSquad.setOnClickListener{
            val createHotsquadFragment = CreateHotsquadFragment()
            dockActivity.replaceDockableFragment(createHotsquadFragment)
        }
    }

    override fun onItemClick(item: Hotsquad) {
        // Handle item click here
//        Log.d("MyHotsquadListFragment", "Item clicked: ${item.title}")
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

    override fun setTitleBar(titleBar: TitleBar) {
        titleBar.showBackButton()
        titleBar.subHeading = getString(R.string.hotsquad_list)
    }

    private fun setAdapter(squadList: List<Hotsquad>) {
        adapter = SquadListAdapter(squadList, this, activity as? DockActivity)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    override fun ResponseFailure(message: String?, tag: String?) {
        binding.tvNoListFound.text = "No Squad List Found"
        binding.tvNoListFound.visibility = View.VISIBLE
    }

    override fun ResponseSuccess(result: String?, Tag: String?) {
        hotsquadListModel = GsonFactory.getConfiguredGson().fromJson(result, HotsquadListModel::class.java)

        if (!hotsquadListModel.data.isNullOrEmpty()) {
            binding.tvNoListFound.visibility = View.GONE
            setAdapter(hotsquadListModel.data)
        } else {
            val createHotsquadFragment = CreateHotsquadFragment()
            dockActivity.replaceDockableFragment(createHotsquadFragment)
//            binding.tvNoListFound.visibility = View.VISIBLE
//            binding.tvNoListFound.text = "No Squad List Found"
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
