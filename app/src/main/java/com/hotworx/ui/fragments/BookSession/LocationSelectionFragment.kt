package com.hotworx.ui.fragments.BookSession

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.isGone
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.hotworx.R
import com.passio.modulepassio.Singletons.ApiHeaderSingleton
import com.hotworx.global.Constants
import com.hotworx.interfaces.OnClickItemListener
import com.hotworx.interfaces.OnClickStringTypeListener
import com.hotworx.interfaces.OnItemClickInterface
import com.hotworx.models.ErrorResponseEnt
import com.hotworx.models.SessionBookingModel.Location
import com.hotworx.models.SessionBookingModel.SessionBookingDataModel
import com.hotworx.retrofit.GsonFactory
import com.hotworx.ui.adapters.FrequentlyBooked.FrequentBookingAdapter
import com.hotworx.ui.adapters.LocationAdapter.LocationSelectionAdapter
import com.hotworx.ui.dialog.BookSession.LocationFeeUpdateDialogFragment
import com.hotworx.ui.fragments.BaseFragment
import com.hotworx.ui.views.TitleBar

class LocationSelectionFragment(var is_reciprocal_allowed: String) : BaseFragment(), OnItemClickInterface, OnClickStringTypeListener,
    OnClickItemListener {
    private lateinit var rvCountryBooking: RecyclerView
    private lateinit var frequentlyBookingAdapter: FrequentBookingAdapter
    private lateinit var locationBookingAdapter: LocationSelectionAdapter
    private lateinit var sessionBookingDataModel: SessionBookingDataModel
    private lateinit var frequentlyBookingArrayList: ArrayList<String>
    private lateinit var acpLocationSpinner: Spinner
    private lateinit var getLocationDetail: Location
    private lateinit var getFrequentLocationDetail: Location
    private lateinit var rvLocationSelector: RecyclerView
    private lateinit var etLocation: AppCompatEditText



    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.activity_book_session_fragment, container, false)
        rvCountryBooking = root.findViewById(R.id.rvCountryBooking)
        rvLocationSelector = root.findViewById(R.id.rvLocationSelector)
        etLocation = root.findViewById(R.id.etLocation)
//        acpLocationSpinner = root.findViewById(R.id.acpLocationSpinner)

        callApi(Constants.GETBOOKINGLOCATION, "")
        Log.d("jbjbjkxasb",is_reciprocal_allowed)
        return root
    }


    private fun callApi(type: String, data: String) {
        when (type) {
            Constants.GETBOOKINGLOCATION -> {
               if (is_reciprocal_allowed == "yes"){
                getServiceHelper().enqueueCallExtended(
                    getWebService().getBookingLocations_v2(
                        ApiHeaderSingleton.apiHeader(requireContext())
                    ), Constants.GETBOOKINGLOCATION, true
                )
               } else {
                   getServiceHelper().enqueueCallExtended(
                       getWebService().getBookingLocations(
                           ApiHeaderSingleton.apiHeader(requireContext())
                       ), Constants.GETBOOKINGLOCATION, true
                   )
               }
            }
        }
    }


    override fun onSuccess(liveData: LiveData<String>, tag: String) {
        super.onSuccess(liveData, tag)
        when (tag) {
            Constants.GETBOOKINGLOCATION -> {
                try {
                    sessionBookingDataModel = GsonFactory.getConfiguredGson()
                        ?.fromJson(liveData.value, SessionBookingDataModel::class.java)!!
//                    frequentlyBookingArrayList = arrayListOf()
//                    sessionBookingDataModel.frequently_locations.forEach {
//                        frequentlyBookingArrayList.add(it.location_name)
//                    }

                    setFrequentLocationAdapter(sessionBookingDataModel.frequently_locations)
                    setLocationAdapter(sessionBookingDataModel.locations)
                    if (etLocation.text.toString() != ""){
                        etLocation.text?.clear()
                    }
                    initSearchView(sessionBookingDataModel.locations)

                } catch (e: Exception) {
                    GsonFactory.getConfiguredGson()?.fromJson(liveData.value, ErrorResponseEnt::class.java)?.let { errorResponseEnt ->
                        dockActivity?.showErrorMessage(errorResponseEnt.error)
                    }
                }
            }
        }
    }

    private fun moveToNextFragment(location: Location,type: String) {
        val args = Bundle()
        when (type) {
            "Location" -> {
                args.putParcelable("Location_Id", location)
            }
            "Frequent Location" -> {
                args.putParcelable("Frequent_Location_Id", location)
            }
        }

        val bookingSelectionFragment = BookingSelectionFragment(is_reciprocal_allowed = this.is_reciprocal_allowed)
        bookingSelectionFragment.arguments = args
        dockActivity.replaceDockableFragment(bookingSelectionFragment)
    }


    private fun selectLocationForNextFragment(getLocation: String, type: String) {

        when (type) {
            "Location" -> {
                if (getLocation.isNotEmpty()) {
                    sessionBookingDataModel.locations.forEach {
                        if (getLocation == it.location_name) {
                            getLocationDetail = it
                            return@forEach // Return early once found
                        }
                    }
                }

                if (!::getLocationDetail.isInitialized) {
                     dockActivity?.showErrorMessage("Location not found")
                    return
                }
            }

            "Frequent Location" -> {
                if (getLocation.isNotEmpty()) {
                    sessionBookingDataModel.frequently_locations.forEach {
                        if (getLocation == it.location_name) {
                            getFrequentLocationDetail = it
                            return@forEach
                        }
                    }

                }

                if (!::getFrequentLocationDetail.isInitialized) {
                    dockActivity?.showErrorMessage("Frequent Location not found")
                    return
                }
            }
        }


        if (type == "Location") {
            if (getLocationDetail.location_tier != "Standard" &&
                (getLocationDetail.location_tier == "Premium" || getLocationDetail.location_tier == "Elite")) {
                initExtraPayDialog(getLocationDetail, "Location")
            } else {
                moveToNextFragment(getLocationDetail, type)
            }
        } else if (type == "Frequent Location") {
            if (getFrequentLocationDetail.location_tier != "Standard" &&
                (getFrequentLocationDetail.location_tier == "Premium" || getFrequentLocationDetail.location_tier == "Elite")) {
                initExtraPayDialog(getFrequentLocationDetail, "Frequent Location")
            } else {
                moveToNextFragment(getFrequentLocationDetail, type)
            }
        }
    }


    private fun setFrequentLocationAdapter(frequestLocationCode: ArrayList<Location>) {
        if (frequestLocationCode.size > 0) {
            frequentlyBookingAdapter = FrequentBookingAdapter(requireContext(), this)
            frequentlyBookingAdapter.setList(frequestLocationCode)
            rvCountryBooking.adapter = frequentlyBookingAdapter
        }
    }

    private fun setLocationAdapter(locationArray: ArrayList<Location>) {
        if (locationArray.size > 0) {
            locationBookingAdapter = LocationSelectionAdapter(requireContext(), this,is_reciprocal_allowed)
            locationBookingAdapter.setList(locationArray)
            rvLocationSelector.adapter = locationBookingAdapter
        }
    }


    private fun initSearchView(list: ArrayList<Location>) {

        etLocation.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                if (etLocation.text!!.isNotEmpty()) {
                    val filteredList = arrayListOf<Location>()
                    list.forEach {
                        if (it.location_name.lowercase()
                                .startsWith(etLocation.text.toString().lowercase())
                        ) {
                            filteredList.add(it)
                        }
                    }

                    if (filteredList.isNotEmpty()) {
                        rvLocationSelector.isGone = false
                        setLocationAdapter(filteredList)
//                        locationBookingAdapter.setList(filteredList)
//                        rvLocationSelector.adapter = locationBookingAdapter
                    } else {
                        rvLocationSelector.isGone = true
                    }
                } else {
                    rvLocationSelector.isGone = false
                    setLocationAdapter(list)
//                    locationBookingAdapter.setList(list)
//                    rvLocationSelector.adapter = locationBookingAdapter
                }
            }
        }
        )
    }


    private fun initExtraPayDialog(getLocationDetail: Location,type: String){
        val locationFeeUpdateDialogFragment = LocationFeeUpdateDialogFragment()

        locationFeeUpdateDialogFragment.setCustomOnClickStringTypeListener(this)
        locationFeeUpdateDialogFragment.dockActivity = myDockActivity
        locationFeeUpdateDialogFragment.getLocationDetail = getLocationDetail
        locationFeeUpdateDialogFragment.type = type

        locationFeeUpdateDialogFragment.show(
            parentFragmentManager,
            locationFeeUpdateDialogFragment.tag
        )

    }

    override fun onItemClick(value: String?) {
        selectLocationForNextFragment(value!!, "Frequent Location")
    }

    override fun setTitleBar(titleBar: TitleBar) {
        super.setTitleBar(titleBar)
        titleBar.showBackButton()
        titleBar.subHeading = resources.getString(R.string.sessions)
        titleBar.contentDescription = getString(R.string.sessions)
    }

    override fun onItemClick(value: String, type: String) {
        when (type) {
            "From_Location_Adapter" -> {
                selectLocationForNextFragment(value, "Location")
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

    }

    override fun <T> onItemClick(data: T, type: String) {
        val data = data as Location
        when (type){
            "COME_FROM_DIALOG_FRAGMENT" -> {
                moveToNextFragment(data, data.type!!)
            }
        }
    }

}