package com.hotworx.ui.fragments.BrivoFragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.viewpager.widget.ViewPager
import com.brivo.sdk.BrivoSDK
import com.google.android.material.tabs.TabLayout
import com.hotworx.R
import com.hotworx.Singletons.ApiHeaderSingleton.apiHeader
import com.hotworx.databinding.FragmentBrivoUserSiteBinding
import com.hotworx.global.Constants
import com.hotworx.helpers.Utils
import com.hotworx.models.BrivoDataModels.AccessBranchesDataModel

import com.hotworx.models.BrivoDataModels.DataNearBy
import com.hotworx.retrofit.GsonFactory
import com.hotworx.ui.adapters.ViewPagerAdapter
import com.hotworx.ui.fragments.BaseFragment
import com.hotworx.ui.fragments.BrivoFragments.BrivoViewPagerFragments.AllDataFragment
import com.hotworx.ui.fragments.BrivoFragments.BrivoViewPagerFragments.NearbyDataFragment
import com.brivo.sdk.access.BrivoSDKAccess
import com.brivo.sdk.model.BrivoConfiguration
import com.brivo.sdk.onair.model.BrivoSelectedAccessPoint
import com.hotworx.models.BrivoDataModels.BrivoLocation.Data
import com.hotworx.models.BrivoDataModels.BrivoLocation.GetBrancheDataModel

import com.hotworx.models.BrivoDataModels.BrivoLocation.Site
import com.hotworx.retrofit.WebService
import org.json.JSONObject
import java.io.InputStream

class BrivoUserSiteFragment : BaseFragment() {
    lateinit var binding: FragmentBrivoUserSiteBinding
    lateinit var getBrivoBranchData: AccessBranchesDataModel
    lateinit var getBrivoLocationData: GetBrancheDataModel

    lateinit var getBrivoSDKAccess: BrivoSDKAccess
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBrivoUserSiteBinding.inflate(layoutInflater)


        callApi(Constants.BRIVODATACALLING)






        return binding.root
    }

    private fun callApi(type: String) {
        when (type) {

            Constants.BRIVODATACALLING
            -> getServiceHelper().enqueueCallExtended(
                getWebService().getBrivoLocation(
                    apiHeader(requireContext())
                ), Constants.BRIVODATACALLING, true
            )


        }
    }




    override fun onSuccess(liveData: LiveData<String>, tag: String) {
        super.onSuccess(liveData, tag)
        if (!isAdded) {
            return
        }
        if (Constants.BRIVODATACALLING == tag) {
            try {
                getBrivoLocationData = GsonFactory.getConfiguredGson()
                    .fromJson<GetBrancheDataModel>(
                        liveData.value,
                        GetBrancheDataModel::class.java
                    )

                setUpViewPager(
                    getBrivoLocationData.data,
                    getBrivoLocationData.data
                )

            } catch (e: Exception) {
                Log.d("catchException", e.message.toString())
                Utils.customToast(requireContext(), resources.getString(R.string.error_failure))
            }
        }
    }


    override fun onFailure(message: String, tag: String) {

        when (tag) {
            Constants.BRIVODATACALLING -> {
                myDockActivity.showErrorMessage(message)
                Log.i("xxError", "Error")
            }
        }
    }


    private fun setUpViewPager(
        getAllBranchesData: Data,
        getNearByBranchesData: Data
    ) {
//        if (!isAdded) return
        binding.viewPager.adapter = ViewPagerAdapter(childFragmentManager)
        addTabsMain(binding.viewPager, getAllBranchesData, getNearByBranchesData)
        binding.tabsMain.setupWithViewPager(binding.viewPager)
        binding.viewPager.offscreenPageLimit = 2

        binding.tabsMain.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
    }

    private fun addTabsMain(
        viewPager: ViewPager,
        getAllBranchesData:Data,
        getNearByBranchesData: Data
    ) {
        if (viewPager.adapter != null && viewPager.adapter!!.count == 2) {
            (viewPager.adapter!!.instantiateItem(viewPager, 0) as AllDataFragment).setData(
                getAllBranchesData
            )
            (viewPager.adapter!!.instantiateItem(viewPager, 1) as NearbyDataFragment).setData(
                getNearByBranchesData as Data
            )
            viewPager.adapter!!.notifyDataSetChanged()
        } else {
            val adapter =
                ViewPagerAdapter(childFragmentManager) //viewPager.getAdapter() instanceof ViewPagerAdapter ? (ViewPagerAdapter) viewPager.getAdapter() : new ViewPagerAdapter(getChildFragmentManager());
            val allSites = AllDataFragment()

            allSites.setData(getAllBranchesData)
            adapter.addFrag(allSites, "All Sites")
            val nearbySites = NearbyDataFragment()
            nearbySites.setData(getNearByBranchesData as Data)
            adapter.addFrag(nearbySites, "Nearby Sites")
            adapter.notifyDataSetChanged()
            viewPager.adapter = adapter
        }
    }

    override fun onResume() {
        super.onResume()
        callApi(Constants.BRIVODATACALLING)
    }
}