package com.hotworx.ui.fragments.BrivoFragments.BrivoViewPagerFragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import com.hotworx.R
import com.hotworx.databinding.FragmentAllDataBinding
import com.hotworx.databinding.FragmentNearbyDataBinding
import com.hotworx.interfaces.OnClickItemListener
import com.hotworx.interfaces.OnClickStringTypeListener

import com.hotworx.models.BrivoDataModels.BrivoLocation.Site

import com.hotworx.ui.adapters.LocationAdapter.SiteSelectionAdapter
import com.hotworx.ui.fragments.BaseFragment
import com.hotworx.ui.fragments.BrivoFragments.AccessDoorFragment
import com.hotworx.models.BrivoDataModels.BrivoLocation.Data
class NearbyDataFragment : BaseFragment(), OnClickItemListener {
    lateinit var binding: FragmentNearbyDataBinding
    private lateinit var siteSelectionAdapter: SiteSelectionAdapter
    private lateinit var  getNearbyBranchesData: Data

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNearbyDataBinding.inflate(layoutInflater)
        if (getNearbyBranchesData.sites.isNotEmpty()) {
            setLocationAdapter(ArrayList(getNearbyBranchesData.sites))
            initSearchView(ArrayList(getNearbyBranchesData.sites))
        }else{
            setUIVisibilities()
        }

        return binding.root
    }

    private fun setUIVisibilities(){
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

    fun setData(sessions: Data) {
        getNearbyBranchesData = sessions
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
//        when (type) {
//            "From_Site_Adapter" -> {
//                var args = Bundle()
//                args.putString("site_id", value)
//                var accessDoorFragment = AccessDoorFragment()
//                accessDoorFragment.setArguments(args)
//                dockActivity.replaceDockableFragment(accessDoorFragment)
//            }
//        }
//    }

    override fun <T> onItemClick(data: T, type: String) {
        when (type) {
            "From_Site_Adapter" -> {
                val getSiteData = data as Site
                val setBranchData: Data = Data(
                    accountId = getNearbyBranchesData.accountId,
                    accountName = getNearbyBranchesData.accountName,
                    bleAuthTimeFrame = getNearbyBranchesData.bleAuthTimeFrame,
                    bleCredential = getNearbyBranchesData.bleCredential,
                    enablePassTransfer = getNearbyBranchesData.enablePassTransfer,
                    enabled = getNearbyBranchesData.enabled,
                    firstName = getNearbyBranchesData.firstName,
                    lastName = getNearbyBranchesData.lastName,
                    pass = getNearbyBranchesData.pass,
                    site = getSiteData,
                    userImage = getNearbyBranchesData.userImage
                )

                Log.d( "onItemClick: ",setBranchData.toString())
            }
        }
    }

}