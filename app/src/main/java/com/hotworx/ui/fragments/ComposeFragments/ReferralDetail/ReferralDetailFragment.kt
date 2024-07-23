package com.hotworx.ui.fragments.ComposeFragments.ReferralDetail

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.hotworx.Extensions.shareLink
import com.hotworx.R
import com.hotworx.Singletons.ApiHeaderSingleton
import com.hotworx.databinding.FragmentRefferalDetailBinding
import com.hotworx.global.Constants
import com.hotworx.helpers.Utils
import com.hotworx.interfaces.OnClickItemListener
import com.hotworx.models.ComposeModel.RefferalDetailModel.AmbassadorReferralDataModel
import com.hotworx.models.ComposeModel.RefferalDetailModel.Data
import com.hotworx.models.ComposeModel.RefferalDetailModel.URLsetModel.UrlDataMode
import com.hotworx.models.ReferralQr.ReferralQrDataModel
import com.hotworx.retrofit.GsonFactory
import com.hotworx.ui.dialog.RedeemInfo.RedeemInfoDialogFragment
import com.hotworx.ui.dialog.ReferalRedeemBalance.RedeemBalanceDialogFragment
import com.hotworx.ui.dialog.ReferralLocation.ReferralLocationDialogFragment
import com.hotworx.ui.dialog.ReferralQr.QrDialogFragment
import com.hotworx.ui.fragments.BaseFragment
import com.hotworx.ui.fragments.MyReferrals.MyReferralFragment
import com.hotworx.ui.views.TitleBar
import com.hotworx.viewmodel.ReferralDetailViewModel


class ReferralDetailFragment : BaseFragment(), OnClickItemListener, OnRefreshListener {

    private val viewModel: ReferralDetailViewModel by activityViewModels()
    lateinit var binding: FragmentRefferalDetailBinding
    lateinit var  redeemBalanceDialogFragment: RedeemBalanceDialogFragment
    lateinit var  redeemInfoDialogFragment: RedeemInfoDialogFragment
    lateinit var  ambassadorReferralDataModel: AmbassadorReferralDataModel

    val args = Bundle()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRefferalDetailBinding.inflate(inflater, container, false)

        binding.swipeContainer.setOnRefreshListener(this@ReferralDetailFragment)
        redeemBalanceDialogFragment = RedeemBalanceDialogFragment()
        redeemInfoDialogFragment = RedeemInfoDialogFragment()
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setOnClickListener()

        if (viewModel.referralDetails.value == null) {
            callApi(Constants.REFFERAL_DETAIL, "")
        } else {
            setImageOrData(viewModel.referralDetails.value!!)

        }

    }
    private fun setOnClickListener() {
        binding.let {

            it.ivRedeemInfo.setOnClickListener{
                initRedeemInfo()
            }
            it.btnRedeem.setOnClickListener {
                initDialog()
            }

            it.cvCard1.setOnClickListener {


                val url = viewModel.selectedLocation.value?.trail_url
                if (url != null) {
                    val referralQrDataModel: ReferralQrDataModel = ReferralQrDataModel(
                        name =" ${getUserDetail()[getUserDetail().size-2]} ${getUserDetail()[getUserDetail().size-1]}",
                        email = prefHelper.loginData.email.toString(),
                        url = url
                    )
                    initQrDialog(referralQrDataModel)
                }else{
                    dockActivity.showErrorMessage("Location not found")
                }

            }
            it.cvCard2.setOnClickListener {
                sendUrl()
            }

            it.cvCard3.setOnClickListener {

                val myReferralFragment = MyReferralFragment()
                myReferralFragment.arguments = args
                myDockActivity.replaceDockableFragment(myReferralFragment)
            }

            it.tvLocation.setOnClickListener {
                if (viewModel.referralDetails.value != null) {
                    initReferralDialog()
                }
            }
        }
    }

    private fun setupObservers() {
        viewModel.referralDetails.observe(
            viewLifecycleOwner,
            Observer { details: AmbassadorReferralDataModel? ->
                redeemInfoDialogFragment.redeemed_text = details!!.redeemed_info_text ?: " "
                setImageOrData(details)

            })

        viewModel.selectedLocation.observe(viewLifecycleOwner, Observer { location: Data? ->
            if (location != null) {

                setLocationText(location.location_name)

                if (::ambassadorReferralDataModel.isInitialized){
                    ambassadorReferralDataModel.data.forEach {
                        if (it.location_name == location.location_name){
                            setCurrency(it.currency_symbol,it.remaining_balance.toString())
                        }
                    }
                }
                redeemBalanceDialogFragment.data = location
                args.putParcelable("Location_Model", location)


            }
        })
    }

    private fun callApi(type: String, data: String) {
        when (type) {
            Constants.REFFERAL_DETAIL ->
                getServiceHelper().enqueueCallExtended(
                    getWebService().getRefferalScreen(
                        ApiHeaderSingleton.apiHeader(requireContext())
                    ), Constants.REFFERAL_DETAIL, true
                )
        }
    }
    private fun setImageOrData(details: AmbassadorReferralDataModel?) {
        if (prefHelper.imagePath != null) {
            binding.tvfirstLastName.visibility = View.GONE
            binding.cvImageCard.visibility = View.VISIBLE

            Glide.with(requireContext())
                .load(prefHelper.imagePath)
                .listener(object : RequestListener<Drawable> {
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
                .into(binding.profileImage)
        } else {
            binding.cvImageCard.visibility = View.GONE
            binding.tvfirstLastName.visibility = View.VISIBLE
            binding.tvfirstLastName.text = "${getUserDetail()[1]}${getUserDetail()[2]}"
        }

        binding.tvFirstName.text = getUserDetail()[3]
        binding.tvLastName.text = getUserDetail()[4]
    }
    private fun setCurrency(currency_Symbol: String,remaining_balance: String) {

            binding.tvAccountNo.text =
                "${currency_Symbol}${remaining_balance}"
       }
    private fun setLocationText(location: String?) {
        binding.tvLocation.text = location
    }
    override fun onSuccess(liveData: LiveData<String>, tag: String) {
        super.onSuccess(liveData, tag)
        when (tag) {
            Constants.REFFERAL_DETAIL -> {
                try {
                    ambassadorReferralDataModel = GsonFactory.getConfiguredGson()
                        .fromJson<AmbassadorReferralDataModel>(
                            liveData.value,
                            AmbassadorReferralDataModel::class.java
                        )
                    redeemInfoDialogFragment.redeemed_text = ambassadorReferralDataModel.redeemed_info_text ?: " "
                    if (ambassadorReferralDataModel.data.isNotEmpty()) setCurrency(ambassadorReferralDataModel.data[0].currency_symbol,ambassadorReferralDataModel.data[0].remaining_balance.toString())
                    viewModel.setReferralDetails(ambassadorReferralDataModel)
                    if (ambassadorReferralDataModel.data.isNotEmpty()) {
                        redeemBalanceDialogFragment.data = ambassadorReferralDataModel.data[0]
                        viewModel.setSelectedLocation(
                            ambassadorReferralDataModel.data[0]

                        )
                        args.putParcelable("Location_Model", ambassadorReferralDataModel.data[0])
                    }
                } catch (e: Exception) {
                    Log.d("onSuException: ",e.message.toString() )
                    Utils.customToast(requireContext(), resources.getString(R.string.error_failure))
                }
            }
        }
    }
    override fun onFailure(message: String, tag: String) {
        if (Constants.REFFERAL_DETAIL == tag) {
            binding.swipeContainer.isRefreshing = false
            myDockActivity.showErrorMessage(message)
        }
    }
    private fun sendUrl() {
        val setUrlDataMode = viewModel.referralDetails.value?.let { details ->
            UrlDataMode(
                details.buy_text,
                viewModel.selectedLocation.value!!.buy_url,
                details.trail_text,
                viewModel.selectedLocation.value!!.trail_url
            )
        }

        if (setUrlDataMode != null) {
            val message = """
                ${setUrlDataMode.buy_text}
                ${setUrlDataMode.buy_url}

                ${setUrlDataMode.trail_text}
                ${setUrlDataMode.trail_url}
            """.trimIndent()
            context?.shareLink(message)
        } else {
            Utils.customToast(requireContext(), "Something Went Wrong")
        }
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


    private fun initDialog(){

        redeemBalanceDialogFragment.show(
            childFragmentManager,
            Constants.BarGraphDialogFragment
        )
    }

    private fun initReferralDialog() {
        val referralLocationDialogFragment = ReferralLocationDialogFragment(this)
        referralLocationDialogFragment.referralData = viewModel.referralDetails.value!!.data
        referralLocationDialogFragment.show(
            parentFragmentManager,
            referralLocationDialogFragment.tag
        )
    }
    private fun initQrDialog(referralQrDataModel: ReferralQrDataModel) {
        val qrDialogFragment = QrDialogFragment()
        qrDialogFragment.setContext(requireContext())
        qrDialogFragment.dockActivity = dockActivity
        qrDialogFragment.qrModel = referralQrDataModel
        qrDialogFragment.show(
            parentFragmentManager,
            qrDialogFragment.tag
        )
    }


    private fun initRedeemInfo(){
        redeemInfoDialogFragment.show(
            childFragmentManager,
            Constants.BarGraphDialogFragment
        )
    }
    override fun <T> onItemClick(data: T, type: String) {
        val receivedData = data as Data
        redeemBalanceDialogFragment.data = receivedData
        viewModel.setSelectedLocation(receivedData)

    }
    override fun onRefresh() {
        binding.swipeContainer.isRefreshing = false
        callApi(Constants.REFFERAL_DETAIL, "")
    }
    override fun setTitleBar(titleBar: TitleBar) {
        super.setTitleBar(titleBar)
        titleBar.showBackButton()
    }

}
