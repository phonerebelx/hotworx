package com.hotworx.ui.fragments.BusinessCard

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.github.alexzhirkevich.customqrgenerator.QrData
import com.github.alexzhirkevich.customqrgenerator.vector.QrCodeDrawable
import com.github.alexzhirkevich.customqrgenerator.vector.createQrVectorOptions
import com.github.alexzhirkevich.customqrgenerator.vector.style.QrVectorBallShape
import com.github.alexzhirkevich.customqrgenerator.vector.style.QrVectorColor
import com.github.alexzhirkevich.customqrgenerator.vector.style.QrVectorFrameShape
import com.github.alexzhirkevich.customqrgenerator.vector.style.QrVectorLogoPadding
import com.github.alexzhirkevich.customqrgenerator.vector.style.QrVectorLogoShape
import com.github.alexzhirkevich.customqrgenerator.vector.style.QrVectorPixelShape
import com.hotworx.Extensions.shareLink
import com.hotworx.R
import com.hotworx.Singletons.ApiHeaderSingleton
import com.hotworx.databinding.FragmentBusinessCardBinding
import com.hotworx.databinding.FragmentRefferalDetailBinding
import com.hotworx.global.Constants
import com.hotworx.helpers.Utils
import com.hotworx.interfaces.OnClickItemListener
import com.hotworx.models.BusinessCard.BusinessCardModel
import com.hotworx.models.ComposeModel.MyReferrals.MyReferralDataModel
import com.hotworx.models.ComposeModel.RefferalDetailModel.AmbassadorReferralDataModel
import com.hotworx.models.ComposeModel.RefferalDetailModel.Data
import com.hotworx.models.ComposeModel.RefferalDetailModel.URLsetModel.UrlDataMode
import com.hotworx.models.ReferralQr.ReferralQrDataModel
import com.hotworx.retrofit.GsonFactory
import com.hotworx.ui.adapters.ViewPager.ViewPagerAdapter
import com.hotworx.ui.dialog.ReferalRedeemBalance.RedeemBalanceDialogFragment
import com.hotworx.ui.dialog.ReferralLocation.ReferralLocationDialogFragment
import com.hotworx.ui.dialog.ReferralQr.QrDialogFragment
import com.hotworx.ui.fragments.BaseFragment
import com.hotworx.ui.fragments.MyReferrals.MyReferralFragment
import com.hotworx.ui.views.TitleBar

class BusinessCardFragment : BaseFragment(), OnClickItemListener {
    lateinit var binding: FragmentBusinessCardBinding
    lateinit var getBusinessCardModel: BusinessCardModel
    lateinit var generateQrBitmap: Bitmap
    lateinit var buy_url: String
    lateinit var trial_url: String
    lateinit var selected_location_name: String
    lateinit var referralData: Data
    lateinit var referralQrDataModel: ReferralQrDataModel
    val args = Bundle()
    var selectedUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBusinessCardBinding.inflate(inflater, container, false)
        setOnClickListener()
        setImageOrData()
        callApi(Constants.GET_EMPLOYEE_REFERRAL, "")

        binding.checkBox.isChecked = true

        // Set up checkbox listener to update URL dynamically
        binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
            selectedUrl = if (isChecked) {
                trial_url
            } else {
                buy_url
            }
            // Update the QR code and referral data based on checkbox selection
            updateReferralData(selectedUrl)
        }

        return binding.root
    }

    private fun <T> callApi(type: String, data: T) {
        when (type) {
            Constants.GET_EMPLOYEE_REFERRAL ->
                getServiceHelper().enqueueCallExtended(
                    getWebService().getEmployeeReferralInfo(
                        ApiHeaderSingleton.apiHeader(requireContext())
                    ), Constants.GET_EMPLOYEE_REFERRAL, true
                )
        }
    }

    private fun updateReferralData(selectedUrl: String) {
        Log.d("SelectedURL", "UpdateReferralData - Selected URL: $selectedUrl")

        // Update referralQrDataModel and referralData using the selected URL
        referralQrDataModel = ReferralQrDataModel(
            getBusinessCardModel.name_on_businesscard ?: "",
            getBusinessCardModel.employee_address ?: "",
            selectedUrl
        )
        referralData = Data(
            buy_url = buy_url,
            currency_symbol = "",
            lead_id = "",
            location_code = getBusinessCardModel.data?.get(0)?.location_code ?: "",
            location_name = getBusinessCardModel.data?.get(0)?.location_name ?: "",
            remaining_balance = 0,
            total_amount_used = 0,
            total_gift_amount = 0,
            trail_url = trial_url,
            redeemed_text = "",
            location_address = ""
        )

        // Set location details
        getBusinessCardModel.data?.get(0)?.let { locationData ->
            selected_location_name = locationData.location_name ?: ""
            setLocationText(locationData.location_name ?: "", locationData.location_address ?: "")
        }

        // Update the UTM text and QR code based on the selected URL
        val utmName = if (binding.checkBox.isChecked) "Trial URL" else "Buy URL"
        setUTMText(utmName)
        setQrCode(selectedUrl)

        // Pass updated data via args if necessary
        args.putParcelable("Location_Model", referralData)
    }

    override fun onSuccess(liveData: LiveData<String>, tag: String) {
        super.onSuccess(liveData, tag)
        when (tag) {
            Constants.GET_EMPLOYEE_REFERRAL -> {
                try {
                    getBusinessCardModel = GsonFactory.getConfiguredGson()
                        .fromJson<BusinessCardModel>(
                            liveData.value,
                            BusinessCardModel::class.java
                        )

                    // Handle UTM list
                    val utmList = getBusinessCardModel.data?.get(0)?.utm_list ?: emptyList()
                    utmList.forEach { utm ->
                        // Log each UTM URL
                        Log.d("url", "UTM Name: ${utm.name}, URL: ${utm.url}")
                        Log.d("Buy_URL", "UTM Name: ${utm.name}, URL: ${utm.buy_url}")
                        Log.d("TRAIL_URL", "UTM Name: ${utm.name}, URL: ${utm.trail_url}")
                    }

                    // Set the buy_url and trial_url from the first UTM list item
                    val firstUtm = utmList.getOrNull(0) ?: run {
                        Log.d("UTM List", "No UTM data available")
                        null
                    }
                    buy_url = firstUtm?.buy_url ?: ""
                    trial_url = firstUtm?.trail_url ?: ""

                    val defaultUrl = if (binding.checkBox.isChecked) {
                        trial_url
                    } else {
                        buy_url
                    }
                    selectedUrl = defaultUrl
                    Log.d("DefaultURL", "Default URL: $defaultUrl")
                    updateReferralData(defaultUrl)


                    // Set referralQrDataModel and referralData
                    referralQrDataModel = ReferralQrDataModel(
                        getBusinessCardModel.name_on_businesscard ?: "",
                        getBusinessCardModel.employee_address ?: "",
                        selectedUrl
                    )
                    referralData = Data(
                        buy_url = firstUtm?.buy_url ?: "",
                        currency_symbol = "",
                        lead_id = "",
                        location_code = getBusinessCardModel.data?.get(0)?.location_code ?: "",
                        location_name = getBusinessCardModel.data?.get(0)?.location_name ?: "",
                        remaining_balance = 0,
                        total_amount_used = 0,
                        total_gift_amount = 0,
                        trail_url = firstUtm?.trail_url ?: "",
                        redeemed_text = "",
                        location_address = ""
                    )

                    args.putParcelable("Location_Model", referralData)

                    // Set location details
                    getBusinessCardModel.data?.get(0)?.let { locationData ->
                        selected_location_name = locationData.location_name ?: ""
                        setLocationText(locationData.location_name ?: "", locationData.location_address ?: "")
                    }

                    // Set UTM text and QR code for the first UTM item
                    val utmName = firstUtm?.name ?: "UTM not found"
                    setUTMText(utmName)
                    setQrCode(selectedUrl)

                } catch (e: Exception) {
                    Utils.customToast(requireContext(), resources.getString(R.string.error_failure))
                }
            }
        }
    }

    override fun onFailure(message: String, tag: String) {
        if (Constants.GET_EMPLOYEE_REFERRAL == tag) {
            myDockActivity.showErrorMessage(message)
        }
    }

    private fun setOnClickListener() {
        binding.let {
            it.tvLocation.setOnClickListener {
                if (::getBusinessCardModel.isInitialized){
                    initReferralDialog()
                }else{
                    myDockActivity.showErrorMessage("Data not found")
                }
            }
            it.tvDropoffLocation.setOnClickListener{
                initUTMDialog()
            }

            it.cvMulti.setOnClickListener {
                callMyReferral()
            }
            it.ivQrCode.setOnClickListener {
                Log.d("setOnClickListener: ",referralQrDataModel.toString())
                initQrDialog(referralQrDataModel)
            }
        }
    }

    fun callMyReferral(){
        val myReferralFragment = MyReferralFragment()
        myReferralFragment.arguments = args
        myDockActivity.replaceDockableFragment(myReferralFragment)
    }

    private fun setUserDetail(email: String?, phone: String?){
        binding.tvUserName.text = getBusinessCardModel.name_on_businesscard
        binding.tvUserDetail.text = getBusinessCardModel.card_title
        binding.tvEmail.text = email ?: ""
        binding.tvPhone.text = phone ?: ""
    }
    private fun setImageOrData() {
        if (prefHelper.imagePath != null) {
            binding.tvfirstLastName.visibility = View.GONE
            binding.imgCircle.visibility = View.VISIBLE

            Glide.with(requireContext())
                .load(prefHelper.imagePath)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: com.bumptech.glide.request.target.Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {

                        binding.imgCircle.visibility = View.GONE
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
                .into(binding.imgIcon)
        } else {
            binding.imgCircle.visibility = View.GONE
            binding.tvfirstLastName.visibility = View.VISIBLE
            binding.tvfirstLastName.text = "${getUserDetail()[1]}${getUserDetail()[2]}"
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


    private fun initUTMDialog() {
        val referralData = ArrayList<Data>()
        if (getBusinessCardModel.data != null){
            getBusinessCardModel.data!!.forEach {
                if (it.location_name == selected_location_name) {
                  it.utm_list.forEach {
                    referralData.add(
                        Data(
                             "",
                            "",
                             "",
                             "",
                            it.name ?: "",
                            0,
                            0,
                            0,
                            it.url ?: "",
                            "",
                             ""
                        )

                    )
                }
                }
            }
                val referralLocationDialogFragment = ReferralLocationDialogFragment(this)
                referralLocationDialogFragment.referralData = referralData
                referralLocationDialogFragment.veriftyIsLocationOrNot = false
                referralLocationDialogFragment.show(
                    parentFragmentManager,
                    referralLocationDialogFragment.tag
                )
            }else{
                myDockActivity.showErrorMessage("Location not found")
            }
    }
    private fun initReferralDialog() {
        val referralData = ArrayList<Data>()
        if (getBusinessCardModel.data != null){
            getBusinessCardModel.data!!.forEach {
                referralData.add(
                    Data(
                        it.buy_url ?: "",
                        it.currency_symbol ?: "",
                        it.lead_id ?: "",
                        it.location_code ?: "",
                        it.location_name ?: "",
                        0,
                        0,
                        0,
                        it.trail_url ?: "",
                       "",
                        it.location_address ?: ""
                    )
                )
            }
            val referralLocationDialogFragment = ReferralLocationDialogFragment(this)
            referralLocationDialogFragment.referralData = referralData
            referralLocationDialogFragment.veriftyIsLocationOrNot = true
                referralLocationDialogFragment.show(
                    parentFragmentManager,
                    referralLocationDialogFragment.tag
                )
        }else{
            myDockActivity.showErrorMessage("Location not found")
        }
    }

    private fun setLocationText(location_name: String, location_address: String ) {
        binding.tvLocation.text = location_name
        binding.tvLocationName.text = location_name
        binding.tvAddressName.text = location_address
    }

    private fun setUTMText(utmName: String ) {
        binding.tvDropoffLocation.text = utmName
    }


    private fun generateQr(context: Context, url: String): Bitmap {
        val data = QrData.Url(url)
        val options = createQrVectorOptions {
            background {
                drawable = ContextCompat.getDrawable(context, R.drawable.white_background)
            }
            logo {
                drawable = ContextCompat.getDrawable(context, R.drawable.qr_logo_rounded)
                size = .24f
                padding = QrVectorLogoPadding.Natural(.2f)
                shape = QrVectorLogoShape.Circle
            }
            colors {
                dark = QrVectorColor.LinearGradient(
                    colors = listOf(
                        0f to ContextCompat.getColor(context, R.color.colorAccent),
                        1f to ContextCompat.getColor(context, R.color.colorRed)
                    ),
                    orientation = QrVectorColor.LinearGradient.Orientation.Vertical
                )
                ball = QrVectorColor.Solid(ContextCompat.getColor(context, R.color.colorRed))
                frame = QrVectorColor.LinearGradient(
                    colors = listOf(
                        0f to ContextCompat.getColor(context, R.color.colorAccent),
                        1f to ContextCompat.getColor(context, R.color.colorRed)
                    ),
                    orientation = QrVectorColor.LinearGradient.Orientation.Vertical
                )
            }
            shapes {
                darkPixel = QrVectorPixelShape.Circle(0.75f)
                ball = QrVectorBallShape.RoundCorners(.5f)
                frame = QrVectorFrameShape.RoundCorners(.5f)
            }
        }

        val qrCodeDrawable = QrCodeDrawable(data, options)
        val desiredWidth = 300
        val desiredHeight = 300
        val bitmap = Bitmap.createBitmap(desiredWidth, desiredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(ContextCompat.getColor(context, android.R.color.white))
        qrCodeDrawable.setBounds(0, 0, desiredWidth, desiredHeight)
        qrCodeDrawable.draw(canvas)
        return bitmap
    }

    private fun setQrCode( url: String) {
        val url = referralQrDataModel.url
        if (url.isNullOrEmpty()) {
            Log.d("QRCodeDialog", "URL received in dialog: $url")
            Utils.customToast(requireContext(), "URL is not available.")
            return
        }
        generateQrBitmap = generateQr(requireContext(), url)
        binding.ivQrCode.setImageBitmap(generateQrBitmap)
    }

    private fun initQrDialog(referralQrDataModel: ReferralQrDataModel) {
        referralQrDataModel.url = selectedUrl // Ensure selectedUrl is set here
        Log.d("QrDialog", "Referral URL: ${referralQrDataModel.url}")
        val qrDialogFragment = QrDialogFragment()
        qrDialogFragment.qrModel = referralQrDataModel
        qrDialogFragment.checkComeFromBusinessCard = true
        qrDialogFragment.setContext(requireContext())
        qrDialogFragment.dockActivity = dockActivity
        qrDialogFragment.show(
            parentFragmentManager,
            qrDialogFragment.tag
        )
    }

    //Not in a use
//    private fun sendUrl() {
//        if (::getBusinessCardModel.isInitialized && ::buy_url.isInitialized && ::trial_url.isInitialized){
//            val setUrlDataMode = getBusinessCardModel?.let { details ->
//                UrlDataMode(
//                    details.buy_text ?: "",
//                    buy_url,
//                    details.trail_text  ?: "",
//                    trial_url
//                )
//            }
//
//            if (setUrlDataMode != null) {
//                val message = """
//                ${setUrlDataMode.buy_text}
//                ${setUrlDataMode.buy_url}
//
//                ${setUrlDataMode.trail_text}
//                ${setUrlDataMode.trail_url}
//            """.trimIndent()
//                context?.shareLink(message)
//            } else {
//                Utils.customToast(requireContext(), "Something Went Wrong")
//            }
//        }else{
//            Utils.customToast(requireContext(), "Url not found")
//        }
//    }

    override fun <T> onItemClick(data: T, type: String) {
        val receivedData = data as com.hotworx.models.ComposeModel.RefferalDetailModel.Data

        when (type){
            "LOCATION_DETAIL" -> {
                setLocationText(receivedData.location_name, receivedData.location_address)
                selected_location_name = receivedData.location_name


                getBusinessCardModel.data!!.forEach {
                    if (it.location_name == selected_location_name) {
                       it.utm_list.let { array ->
                           setUTMText(array[0].name?: "")
                           setQrCode(array[0].url ?: "")
                           referralQrDataModel.url = selectedUrl

                       }
                        referralData.location_name = it.location_name
                        referralData.location_code = it.location_code!!
                        referralData.trail_url = selectedUrl
                        setUserDetail(it.location_email,it.location_phone)
                    }

                }
            }


            "UTM_DETAIL" -> {
                setUTMText(receivedData.location_name)
                setQrCode(selectedUrl)

                referralQrDataModel.url = selectedUrl
                    referralData.trail_url = selectedUrl

                getBusinessCardModel.data!!.forEach {loc ->
                    loc.utm_list.forEach {utm ->
                        if (utm.name == receivedData.location_name)    {
                            referralData.location_name = loc.location_name ?: ""
                            referralData.location_code = loc.location_code ?: ""

                        }
                    }
                }
            }
        }

        args.putParcelable("Location_Model", referralData)
    }

    override fun setTitleBar(titleBar: TitleBar) {
        super.setTitleBar(titleBar)
        titleBar.showBackButton()
    }
}