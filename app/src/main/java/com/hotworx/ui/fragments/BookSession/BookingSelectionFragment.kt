package com.hotworx.ui.fragments.BookSession

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.hotworx.R
import com.hotworx.Singletons.ApiHeaderSingleton
import com.hotworx.global.Constants
import com.hotworx.helpers.ServiceHelper
import com.hotworx.helpers.Utils
import com.hotworx.interfaces.BookingConfirmationDialogClickListener
import com.hotworx.interfaces.GetStringOnClickListener
import com.hotworx.interfaces.OnClickItemListener
import com.hotworx.interfaces.OnClickTypeListener
import com.hotworx.interfaces.OnItemClickInterface
import com.hotworx.micsAdapter.SpinnerAdapter
import com.hotworx.models.ErrorResponseEnt
import com.hotworx.models.SessionBookingModel.FinalSessionBookingModel.*
import com.hotworx.models.SessionBookingModel.FinalSessionBookingModel.AddCardDialog.AddNewCardDialogFragment
import com.hotworx.models.SessionBookingModel.FinalSessionBookingModel.BookSessionWebModel.WebViewUrlModel
import com.hotworx.models.SessionBookingModel.FinalSessionBookingModel.ReConformBookingDialog.ReconformBookingDialogFragment
import com.hotworx.models.SessionBookingModel.FrequentlyLocation
import com.hotworx.models.SessionBookingModel.Location
import com.hotworx.retrofit.GsonFactory
import com.hotworx.ui.adapters.DatePicker.DataPickerAdapter
import com.hotworx.ui.adapters.ShowSlots.ShowSlotAdapter
import com.hotworx.ui.dialog.ReferralQr.QrDialogFragment
import com.hotworx.ui.dialog.SessionBooking.SessionBookingDialogFragment
import com.hotworx.ui.dialog.SessionBooking.SessionViewAppointmentDialogFragment
import com.hotworx.ui.fragments.BaseFragment
import com.hotworx.ui.fragments.BookSession.WebView.WebViewFragment
import com.hotworx.ui.fragments.HomeFragment
import com.hotworx.ui.views.TitleBar
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class BookingSelectionFragment : BaseFragment(), OnItemClickInterface, OnClickTypeListener, OnClickItemListener, BookingConfirmationDialogClickListener {

    private lateinit var acbBySessionType: AppCompatButton
    private lateinit var acbByTime: AppCompatButton
    private lateinit var rvDateSelector: RecyclerView
    private lateinit var rvShowSlot: RecyclerView
    private lateinit var acpSlotSpinner: Spinner
    private lateinit var tvLocation: TextView
    private lateinit var tvSelectSession: TextView
    private lateinit var tvSelectDate: TextView
    private lateinit var Location_Id_All: String
    private lateinit var postLevelTwoDataMode: PostLevelTwoDataMode
    private lateinit var getLevelTwoDataModel: GetLevelTwoDataModel
    private lateinit var getShowSlotDataModel: GetShowSlotDataModel
    private lateinit var getWebViewUrlModel: WebViewUrlModel
    private lateinit var dataPickerAdapter: DataPickerAdapter
    private lateinit var showSlotAdapter: ShowSlotAdapter
    private lateinit var sessionViewAppointmentDialogFragment: SessionViewAppointmentDialogFragment
    private lateinit var getDateDataFromAdapter: String
    private lateinit var postShowSlotDataModel: PostShowSlotDataModel
    private lateinit var getShowSlotDataModelItem: GetShowSlotDataModelItem
    private lateinit var postBookSessionDataModel: PostBookSessionDataModel
    private lateinit var showAppointmentDataModel: ShowAppointmentDataModel
    private lateinit var linearLayoutCompat6: LinearLayoutCompat
    private lateinit var linearLayoutCompat7: LinearLayoutCompat
    private var selectedSpinnerSlot: String = ""
    private var locationName: String = ""
    private var message_popup: Boolean? = null
    private val viewTypeArray: ArrayList<String> = arrayListOf("by_session_type", "by_time")

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_booking_selection, container, false)
        // Inflate the layout for this fragment

        acbBySessionType = root.findViewById(R.id.acbBySessionType)
        acbByTime = root.findViewById(R.id.acbByTime)
        rvDateSelector = root.findViewById(R.id.rvDateSelector)
        rvShowSlot = root.findViewById(R.id.rvShowSlot)
        acpSlotSpinner = root.findViewById(R.id.acpSlotSpinner)
        tvLocation = root.findViewById(R.id.tvLocation)
        tvSelectSession = root.findViewById(R.id.tvSelectSession)
        tvSelectDate = root.findViewById(R.id.tvSelectDate)
        linearLayoutCompat6 = root.findViewById(R.id.linearLayoutCompat6)
        linearLayoutCompat7 = root.findViewById(R.id.linearLayoutCompat7)

        val args = arguments
        if (args != null) {

            val Location_Id = args.getParcelable<Location>("Location_Id")
            val Frequent_Location_Id =
                args.getParcelable<Location>("Frequent_Location_Id")
            if (Location_Id == null) {
                Location_Id_All = Frequent_Location_Id!!.location_id
                tvLocation.text = Frequent_Location_Id.location_name
                locationName = Frequent_Location_Id.location_name
            } else {
                Location_Id_All = Location_Id.location_id
                tvLocation.text = Location_Id.location_name
                locationName = Location_Id.location_name
            }
            dataPickerAdapter = DataPickerAdapter(requireContext(), this)
            setDataForSessionModel("", viewTypeArray[0])
            callApi(Constants.GETBOOKINGSESSION, "")
            setOnCLickListener()
        }
        return root
    }

    private fun setDataForSessionModel(selected_date: String, view_type: String) {
        postLevelTwoDataMode = PostLevelTwoDataMode(
            Location_Id_All,
            selected_date,
            view_type
        )

    }

    private fun showBookingConfirmationDialog() {
        showAppointmentDataModel = ShowAppointmentDataModel(
            session = postBookSessionDataModel.session_type,
            time = postBookSessionDataModel.time_slot,
            location = getLevelTwoDataModel.location_details.location_name,
            date = getDateDataFromAdapter,
            duration = postBookSessionDataModel.duration
        )

        sessionViewAppointmentDialogFragment = SessionViewAppointmentDialogFragment(this)
        sessionViewAppointmentDialogFragment.showAppointmentDataModel = showAppointmentDataModel
        sessionViewAppointmentDialogFragment.show(childFragmentManager, Constants.SessionViewAppointmentDialogFragment)
    }

    private fun callApi(type: String, checkCondition: String) {
        checkIsLoadingStart()
        when (type) {
            Constants.GETBOOKINGSESSION -> {
                getServiceHelper().enqueueCallExtended(
                    getWebService().getLevelTwo_v2(
                        ApiHeaderSingleton.apiHeader(requireContext()),
                        postLevelTwoDataMode.selected_location_id,
                        postLevelTwoDataMode.selected_date,
                        postLevelTwoDataMode.view_type
                    ), Constants.GETBOOKINGSESSION, true
                )
            }

            Constants.GETSHOWSLOTS -> {
                postShowSlotDataModel = PostShowSlotDataModel(
                    postLevelTwoDataMode.selected_date,
                    postLevelTwoDataMode.selected_location_id,
                    postLevelTwoDataMode.view_type,
                    selectedSpinnerSlot,
                    selectedSpinnerSlot
                )
                getServiceHelper().enqueueCallExtended(
                    getWebService().showSlots(
                        ApiHeaderSingleton.apiHeader(requireContext()),
                        postShowSlotDataModel.selected_date,
                        postShowSlotDataModel.selected_location_id,
                        postShowSlotDataModel.view_type,
                        postShowSlotDataModel.selected_time,
                        postShowSlotDataModel.session_type
                    ), Constants.GETSHOWSLOTS, true
                )
            }

        }
    }

    private fun callBookSessionApi() {
        getServiceHelper().enqueueCallExtended(
            getWebService().bookSession_v2(ApiHeaderSingleton.apiHeader(requireContext()),
                postBookSessionDataModel.sauna_no,
                postBookSessionDataModel.time_slot,
                postBookSessionDataModel.booking_date,
                postBookSessionDataModel.session_type,
                postBookSessionDataModel.selected_location_id,
                message_popup), Constants.BOOKSESSION, true
        )
    }

    override fun onSuccess(liveData: LiveData<String>, tag: String) {
        super.onSuccess(liveData, tag)
        when (tag) {
            Constants.GETBOOKINGSESSION -> {
                try {
                    getLevelTwoDataModel = GsonFactory.getConfiguredGson()
                        ?.fromJson(liveData.value, GetLevelTwoDataModel::class.java)!!

                    if (!dataPickerAdapter.isAdapterLoad) {
                        setAdapter(getLevelTwoDataModel.dates)
                    }
                    setSpinner(getLevelTwoDataModel.list)
                } catch (e: Exception) {

                    dockActivity?.showErrorMessage(e.message.toString())

                }
            }
            Constants.GETSHOWSLOTS -> {
                try {
                    getShowSlotDataModel = GsonFactory.getConfiguredGson()
                        ?.fromJson(liveData.value, GetShowSlotDataModel::class.java)!!
                   postLevelTwoDataMode.view_type

                    if (postLevelTwoDataMode.view_type == "by_session_type"){
                        linearLayoutCompat6.visibility = View.INVISIBLE
                        linearLayoutCompat7.visibility = View.VISIBLE
                    }
                    else{
                        linearLayoutCompat6.visibility = View.VISIBLE
                        linearLayoutCompat7.visibility =View.INVISIBLE
                    }
                    setShowSlotAdapter(getShowSlotDataModel,postLevelTwoDataMode.view_type)

                } catch (e: Exception) {
                    dockActivity?.showErrorMessage(e.message.toString())

                }
            }

            Constants.BOOKSESSION -> {
                try {
                    getWebViewUrlModel = GsonFactory.getConfiguredGson()
                        ?.fromJson(liveData.value, WebViewUrlModel::class.java)!!

                    if (getWebViewUrlModel.payment_status == null && getWebViewUrlModel.add_card_url != null && getWebViewUrlModel.message_popup == null){
                        val webViewDialogFragment = WebViewFragment(locationName, getWebViewUrlModel.add_card_url.toString(),this,myDockActivity)
                        webViewDialogFragment.show(
                            parentFragmentManager,
                            webViewDialogFragment.tag
                        )
                    }
                    else if (getWebViewUrlModel.payment_status == null && getWebViewUrlModel.add_card_url != null && (getWebViewUrlModel.message_popup != null && getWebViewUrlModel.message_popup == true)){
                        initCardUpdateDialog()
                    }

                    else if ((getWebViewUrlModel.payment_status != null && getWebViewUrlModel.payment_status == false) && getWebViewUrlModel.add_card_url != null){
                        initReconformDialog(getWebViewUrlModel.text ?: "Not Found")
                    }
                    else if ((getWebViewUrlModel.payment_status != null && getWebViewUrlModel.payment_status == true) && getWebViewUrlModel.add_card_url == null) {
                        showBookingConfirmationDialog()
                    }

                } catch (e: java.lang.Exception) {
                    dockActivity?.showErrorMessage(e.message.toString())
                }
            }
        }

        checkIsLoadingEnd()
    }

    override fun onFailure(message: String, tag: String) {

        when (tag) {
            Constants.GETSHOWSLOTS -> {
                myDockActivity?.showErrorMessage(message)
                Log.i("xxError", "Error")
            }
            Constants.BOOKSESSION -> {
                myDockActivity?.showErrorMessage(message)
                Log.i("xxError", "Error")
            }
        }
    }

    private fun setSpinner(slotArrayList: ArrayList<ListData>) {
        val slotNameArray = arrayListOf<String>()
        slotArrayList.forEach {
            slotNameArray.add(it.value)
        }
        val slotAdapter = SpinnerAdapter(requireContext(), slotNameArray)
        acpSlotSpinner.adapter = slotAdapter
        acpSlotSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedSpinnerSlot = slotNameArray[position]
                tvSelectSession.text = selectedSpinnerSlot
                slotArrayList.forEach {
                    if (selectedSpinnerSlot == it.value) {
                        selectedSpinnerSlot = it.slot
                    }
                }
                tvSelectDate.text = getShortMonthNameFromDate(postLevelTwoDataMode.selected_date)
                callApi(Constants.GETSHOWSLOTS, postLevelTwoDataMode.view_type)
            }
        }
    }

    private fun setShowSlotAdapter(getShowSlotDataModel: ArrayList<GetShowSlotDataModelItem>,viewType: String) {
        if (getShowSlotDataModel.size > 0) {
            showSlotAdapter = ShowSlotAdapter(requireContext(), this)
            showSlotAdapter.setList(getShowSlotDataModel,viewType)
            rvShowSlot.adapter = showSlotAdapter
        }
    }

    private fun setAdapter(getDataArray: ArrayList<String>) {
        if (getDataArray.size > 0) {
            dataPickerAdapter.setList(getDataArray)
            dataPickerAdapter.isAdapterLoad = true
            rvDateSelector.adapter = dataPickerAdapter
        }
    }

    private fun setOnCLickListener() {
        acbByTime.setOnClickListener {
            acbByTime.setBackgroundResource(R.drawable.multi_background_for_btn)
            acbByTime.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite))
            acbBySessionType.setBackgroundResource(R.drawable.white_background)
            acbBySessionType.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorBlack
                )
            )
            setDataForSessionModel(postLevelTwoDataMode.selected_date, viewTypeArray[1])
            callApi(Constants.GETBOOKINGSESSION, "")
        }

        acbBySessionType.setOnClickListener {
            acbByTime.setBackgroundResource(R.drawable.white_background)
            acbByTime.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorBlack))
            acbBySessionType.setBackgroundResource(R.drawable.multi_background_for_btn)
            acbBySessionType.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorWhite
                )
            )
            setDataForSessionModel(postLevelTwoDataMode.selected_date, viewTypeArray[0])
            callApi(Constants.GETBOOKINGSESSION, "")
        }
    }

    override fun onItemClick(value: String?) {

        getDateDataFromAdapter = value!!
        setDataForSessionModel(getDateDataFromAdapter, postLevelTwoDataMode.view_type)
        callApi(Constants.GETBOOKINGSESSION, "")
    }

    override fun onItemClick(data: GetShowSlotDataModelItem, type: String) {
        when (type) {
            "FromShowSlot" -> {
                getShowSlotDataModelItem = data
                postBookSessionDataModel = PostBookSessionDataModel(
                    getShowSlotDataModelItem.suana_no,
                    getShowSlotDataModelItem.time_slot,
                    postLevelTwoDataMode.selected_date,
                    getShowSlotDataModelItem.session_name,
                    postLevelTwoDataMode.selected_location_id,
                    getShowSlotDataModelItem.duration
                )

                val sessionBookingDialogFragment = SessionBookingDialogFragment(this)
                sessionBookingDialogFragment.postBookSessionDataModel = postBookSessionDataModel
                sessionBookingDialogFragment.show(
                    childFragmentManager,
                    Constants.SessionBookingDialogFragment
                )
            }
            // I use this listener because it has type parameter
            "FROM_APPOINTMENT_DIALOG" -> {
                callApi(Constants.GETSHOWSLOTS, postLevelTwoDataMode.view_type)
            }
        }
    }

    override fun onConfirmBooking() {
        callBookSessionApi()
        message_popup = false
    }

    private fun initCardUpdateDialog() {
        val updateCardDialogFragment = AddNewCardDialogFragment(this)
        updateCardDialogFragment.getWebViewUrlModel = getWebViewUrlModel
        updateCardDialogFragment.show(
            parentFragmentManager,
            updateCardDialogFragment.tag
        )
    }

    private fun initReconformDialog(getText: String){
        val reConformDialogFragment = ReconformBookingDialogFragment(this)
        reConformDialogFragment.textString = getText
        reConformDialogFragment.show(
            parentFragmentManager,
            reConformDialogFragment.tag
        )
    }
    fun getShortMonthNameFromDate(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        val outputFormat = SimpleDateFormat("MMM", Locale.getDefault())
        return outputFormat.format(date).plus(" "+dateString.split("-").lastOrNull())
    }

    override fun setTitleBar(titleBar: TitleBar) {
        super.setTitleBar(titleBar)
//        titleBar.showMenuButton()
        titleBar.showBackButton()
        titleBar.subHeading = resources.getString(R.string.sessions)
        titleBar.contentDescription = getString(R.string.sessions)
    }

    override fun onBackPressed() {
        super.onBackPressed()

    }

    override fun <T> onItemClick(data: T, type: String) {

        when (type){
            "COME_FROM_RECONFORM_BOOKING_CANCEL_REQUEST" -> {

            }
            "COME_FROM_RECONFORM_BOOKING_CONTINUE_REQUEST" -> {
                val webViewDialogFragment = WebViewFragment(locationName, getWebViewUrlModel.add_card_url.toString(),this,myDockActivity)
                webViewDialogFragment.show(
                    parentFragmentManager,
                    webViewDialogFragment.tag
                )
            }
            "COME_FROM_RECONFORM_BOOKING_CONTINUE_REQUEST_CHECK_CARD" -> {
                val webViewDialogFragment = WebViewFragment(locationName, getWebViewUrlModel.add_card_url.toString(),this,myDockActivity)
                webViewDialogFragment.show(
                    parentFragmentManager,
                    webViewDialogFragment.tag
                )
            }

            "COME_FROM_RECONFORM_BOOKING_CANCEL_REQUEST_CHECK_CARD" -> {
                message_popup = false
                callBookSessionApi()
            }
        }
    }
}