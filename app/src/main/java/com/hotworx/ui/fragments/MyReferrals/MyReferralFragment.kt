package com.hotworx.ui.fragments.MyReferrals


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import com.hotworx.Extensions.shareLink
import com.hotworx.R
import com.passio.modulepassio.Singletons.ApiHeaderSingleton
import com.hotworx.databinding.FragmentMyReferralBinding
import com.hotworx.global.Constants
import com.hotworx.helpers.Utils
import com.hotworx.models.ComposeModel.MyReferrals.Data
import com.hotworx.models.ComposeModel.MyReferrals.MyReferralDataModel
import com.hotworx.models.ComposeModel.MyReferrals.ReferralDetail
import com.hotworx.retrofit.GsonFactory
import com.hotworx.ui.adapters.MyReferral.MyReferralAdapter
import com.hotworx.ui.fragments.BaseFragment


class MyReferralFragment : BaseFragment() {

    lateinit var binding: FragmentMyReferralBinding
    lateinit var myReferralAdapter: MyReferralAdapter
    lateinit var details: MyReferralDataModel
    lateinit var args: Bundle
    lateinit var getLocationDetail: com.hotworx.models.ComposeModel.RefferalDetailModel.Data
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMyReferralBinding.inflate(inflater, container, false)

        try {
            args = requireArguments()
            getLocationDetail = args.getParcelable<com.hotworx.models.ComposeModel.RefferalDetailModel.Data>("Location_Model")!!
            Log.d("onCreateView: ",args.toString())
        } catch (e: Exception) {

            myDockActivity.showErrorMessage("Data not found")

        }
        SetonClickListener()
        callApi(Constants.GET_LEAD_AMASADOR, "")

        return binding.root
    }

    private fun callApi(type: String, data: String) {
        when (type) {
            Constants.GET_LEAD_AMASADOR ->
                getServiceHelper().enqueueCallExtended(
                    getWebService().getleadAmbassador(
                        ApiHeaderSingleton.apiHeader(requireContext())
                    ), Constants.GET_LEAD_AMASADOR, true
                )
        }
    }


    override fun onSuccess(liveData: LiveData<String>, tag: String) {
        super.onSuccess(liveData, tag)
        when (tag) {
            Constants.GET_LEAD_AMASADOR -> {
                try {
                    details = GsonFactory.getConfiguredGson()
                        .fromJson<MyReferralDataModel>(
                            liveData.value,
                            MyReferralDataModel::class.java
                        )

                    initSearchView(details.data)
                } catch (e: Exception) {
                    Utils.customToast(requireContext(), resources.getString(R.string.error_failure))
                }
            }
        }
    }

    override fun onFailure(message: String, tag: String) {
        if (Constants.GET_LEAD_AMASADOR == tag) {
            myDockActivity.showErrorMessage(message)
        }
    }

    private fun initRecyclerView(referralData: List<ReferralDetail>) {
        if (referralData.size > 0) {
            binding.tvNoDataFound.visibility = View.GONE
            binding.rvLocationSender.visibility = View.VISIBLE
            myReferralAdapter = MyReferralAdapter(requireContext())
            myReferralAdapter.setList(referralData)
            binding.rvLocationSender.adapter = myReferralAdapter
        } else {
            binding.rvLocationSender.visibility = View.GONE
            binding.tvNoDataFound.visibility = View.VISIBLE
        }
    }




    private fun initSearchView(list: List<Data>) {

        if (::getLocationDetail.isInitialized) {

            var referralDetails = listOf<ReferralDetail>()

            list.forEach {
                if (it.location_code == getLocationDetail.location_code) {
                    referralDetails = it.referral_details
                }
            }

            when (referralDetails.size){

                0 -> {
                    binding.ivfirstLastName.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.hotworx_logo_7))
                }
                1 -> {
                    binding.ivfirstLastName.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.hotworx_logo_1))
                }
                2 -> {
                    binding.ivfirstLastName.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.hotworx_logo_2))
                }
                3 -> {
                    binding.ivfirstLastName.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.hotworx_logo_3))
                }
                4 -> {
                    binding.ivfirstLastName.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.hotworx_logo_4))
                }
                5 -> {
                    binding.ivfirstLastName.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.hotworx_logo_5))
                }
                in 6..Int.MAX_VALUE -> {
                    binding.ivfirstLastName.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.hotworx_logo_6))
                }
            }

            initRecyclerView(referralDetails)
        }

    }

    private fun SetonClickListener(){
        binding.btnShareLink.setOnClickListener {
        if (::getLocationDetail.isInitialized){
            context?.shareLink(getLocationDetail.trail_url)
        }
        }
    }

}