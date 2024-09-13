package com.hotworx.ui.fragments.BrivoFragments.BrivoViewPagerFragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import com.hotworx.databinding.FragmentAllDataBinding
import com.hotworx.interfaces.OnClickItemListener
import com.hotworx.models.BrivoDataModels.BrivoLocation.Site
import com.hotworx.ui.adapters.LocationAdapter.SiteSelectionAdapter
import com.hotworx.ui.fragments.ActivityScreen.ActivityDetailFragment
import com.hotworx.ui.fragments.BaseFragment
import com.hotworx.ui.fragments.BrivoFragments.AccessDoorFragment
import com.hotworx.models.BrivoDataModels.BrivoLocation.Data

class AllDataFragment : BaseFragment(), OnClickItemListener {

    lateinit var binding: FragmentAllDataBinding
    private lateinit var siteSelectionAdapter: SiteSelectionAdapter
    private lateinit var getAllBranchesData: Data

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAllDataBinding.inflate(layoutInflater)
        if (getAllBranchesData.sites.isNotEmpty()) {
            setLocationAdapter(ArrayList(getAllBranchesData.sites))
            initSearchView(ArrayList(getAllBranchesData.sites))
        } else {
            setUIVisibilities()
        }
        return binding.root
    }


    private fun setUIVisibilities() {
        binding.etSites.visibility = View.GONE
        binding.rvLocationSelector.visibility = View.GONE
        binding.tvDataNotFound.visibility = View.VISIBLE
    }


    private fun setLocationAdapter(locationArray: ArrayList<Site>) {
        if (locationArray.size > 0) {
            siteSelectionAdapter = SiteSelectionAdapter(requireContext(), this)
            siteSelectionAdapter.setList(locationArray)
            binding.rvLocationSelector.adapter = siteSelectionAdapter
        }
    }

    fun setData(sessions: com.hotworx.models.BrivoDataModels.BrivoLocation.Data) {
        getAllBranchesData = sessions


    }

    private fun initSearchView(list: ArrayList<Site>) {

        binding.etSites.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                if (binding.etSites.text!!.isNotEmpty()) {
                    val filteredList = arrayListOf<Site>()
                    list.forEach {
                        if (it.siteName.lowercase()
                                .startsWith(binding.etSites.text.toString().lowercase())
                        ) {
                            filteredList.add(it)
                        }
                    }
                    if (filteredList.isNotEmpty()) {
                        binding.rvLocationSelector.isGone = false
                        setLocationAdapter(filteredList)
//                        locationBookingAdapter.setList(filteredList)
//                        rvLocationSelector.adapter = locationBookingAdapter
                    } else {
                        binding.rvLocationSelector.isGone = true
                    }
                } else {
                    binding.rvLocationSelector.isGone = false
                    setLocationAdapter(list)
//                    locationBookingAdapter.setList(list)
//                    rvLocationSelector.adapter = locationBookingAdapter
                }
            }
        }
        )
    }

//    override fun onItemClick(value: String, type: String) {
//
//        when (type) {
//            "From_Site_Adapter" -> {
//
//                var args = Bundle()
//                args.putString("site_id", value)
//                var accessDoorFragment = AccessDoorFragment()
//                accessDoorFragment.setArguments(args)
//                dockActivity.replaceDockableFragment(accessDoorFragment)
//            }
//        }
//
//    }

    override fun <T> onItemClick(data: T, type: String) {
        when (type) {
            "From_Site_Adapter" -> {
                val getSiteData = data as Site
               val setBranchData: Data = Data(
                   accountId = getAllBranchesData.accountId,
                   accountName = getAllBranchesData.accountName,
                   bleAuthTimeFrame = getAllBranchesData.bleAuthTimeFrame,
                   bleCredential = getAllBranchesData.bleCredential,
                   enablePassTransfer = getAllBranchesData.enablePassTransfer,
                   enabled = getAllBranchesData.enabled,
                   firstName = getAllBranchesData.firstName,
                   lastName = getAllBranchesData.lastName,
                   pass = getAllBranchesData.pass,
                   site = getSiteData,
                   userImage = getAllBranchesData.userImage
               )

                var args = Bundle()
                args.putParcelable("site_data", setBranchData)
                var accessDoorFragment = AccessDoorFragment()
                accessDoorFragment.setArguments(args)
                Log.d("accessDoorFragment: ",args.getParcelable<Data>("site_data").toString())
                dockActivity.replaceDockableFragment(accessDoorFragment)
            }
        }
    }
}