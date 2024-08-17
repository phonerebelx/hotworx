package com.hotworx.ui.fragments.VIDeviceManagement

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.lifecycle.LiveData
import com.hotworx.R
import com.hotworx.Singletons.ApiHeaderSingleton
import com.hotworx.databinding.FragmentConformRegistrationBinding
import com.hotworx.global.Constants
import com.hotworx.helpers.UtilsHelpers
import com.hotworx.helpers.UtilsHelpers.Companion.getDeviceId
import com.hotworx.micsAdapter.SpinnerAdapter
import com.hotworx.models.ErrorResponseEnt
import com.hotworx.models.ViModel.GetAllLocation.Data
import com.hotworx.models.ViModel.GetAllLocation.GetAllLocationModel
import com.hotworx.models.ViModel.GetAllLocation.SaunaDetail
import com.hotworx.models.ViModel.Registration.RegisterLocationResponseModel
import com.hotworx.models.ViModel.Registration.SetRegisterLocationModel
import com.hotworx.models.ViModel.Unregistraion.SetUnRegisterLocationModel
import com.hotworx.retrofit.GsonFactory
import com.hotworx.ui.fragments.BaseFragment
import com.hotworx.ui.fragments.HomeFragment
import com.hotworx.ui.views.TitleBar

class ConformRegistrationFragment : BaseFragment() {

    lateinit var binding: FragmentConformRegistrationBinding
    lateinit var getAllLocation: GetAllLocationModel
    lateinit var registerLocationModel: SetRegisterLocationModel
    lateinit var unRegisterLocationModel: SetUnRegisterLocationModel
    private var service = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConformRegistrationBinding.inflate(inflater, container, false)
        val args = arguments
        if (args != null) {
            service = args.getString("register_service") ?: "Register"
        }
        setOnClickListener()
        setText()
        callApi("list_location_suana", "")

        return binding.root
    }


    private fun <T> callApi(type: String, data: T) {
        when (type) {
            "list_location_suana" -> {
                getServiceHelper().enqueueCallExtended(
                    getWebService().getListLocationSuana(
                        ApiHeaderSingleton.apiHeader(requireContext())
                    ), "list_location_suana", true
                )
            }

            "register_device" -> {
                getServiceHelper().enqueueCallExtended(
                    getWebService().registerDevice(
                        ApiHeaderSingleton.apiHeader(requireContext()),
                        arrayListOf(data as SetRegisterLocationModel)
                    ), "register_device", true
                )
            }

            "unRegister_device" -> {
                getServiceHelper().enqueueCallExtended(
                    getWebService().unRegisterDevice(
                        ApiHeaderSingleton.apiHeader(requireContext()),
                        arrayListOf(data as SetUnRegisterLocationModel)
                    ), "unRegister_device", true
                )
            }
        }
    }


    override fun onSuccess(liveData: LiveData<String>, tag: String) {
        super.onSuccess(liveData, tag)
        when (tag) {
            "list_location_suana" -> {
                try {
                    getAllLocation = GsonFactory.getConfiguredGson()
                        ?.fromJson(liveData.value, GetAllLocationModel::class.java)!!

                    setSpinner(getAllLocation, "For_Location")
                } catch (e: Exception) {
                    GsonFactory.getConfiguredGson()
                        ?.fromJson(liveData.value, ErrorResponseEnt::class.java)
                        ?.let { errorResponseEnt ->
                            dockActivity?.showErrorMessage(errorResponseEnt.error)
                        }
                }
            }

            "register_device", "unRegister_device" -> {
                val registerLocationResponseModel = GsonFactory.getConfiguredGson()
                    ?.fromJson(liveData.value, RegisterLocationResponseModel::class.java)!!

                if (registerLocationResponseModel.status == "1") {
                    dockActivity.showSuccessMessage(registerLocationResponseModel.desc)
                    dockActivity.replaceDockableFragment(HomeFragment(), Constants.HomeFragment)
                } else {
                    dockActivity.showErrorMessage(registerLocationResponseModel.desc)
                }

            }
        }
    }

    private fun setSpinner(
        getAllLocation: GetAllLocationModel,
        type: String,
        selectedLocation: String = ""
    ) {

        when (type) {
            "For_Location" -> {
                //set all location
                val locationItems = arrayListOf<String>()

                getAllLocation.data.forEach { location ->
                    locationItems.add(location.name)
                }
                val slotAdapter = SpinnerAdapter(requireContext(), locationItems, "Select Location")
                binding.acpLocationSpinner.adapter = slotAdapter
                binding.acpLocationSpinner.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            if (position == 0 && locationItems.isNotEmpty()) {
                                setSpinner(getAllLocation, "For_Suana")
                            } else if (locationItems.isNotEmpty()) {
                                setSpinner(getAllLocation, "For_Suana", locationItems[position - 1])
                            }
                        }
                    }
            }

            "For_Suana" -> {
                val suanaItems = arrayListOf<String>()
                var locationId: String = ""
                getAllLocation.data.forEach { location ->
                    if (location.name == selectedLocation) {
                        locationId = location.location_id.toString()
                        location.sauna_details.forEach { suana ->
                            suanaItems.add(suana.sauna_no.toString())
                        }
                    }
                }


                val suanaAdapter = SpinnerAdapter(requireContext(), suanaItems, "Select Suana")
                binding.acpSuanaSpinner.adapter = suanaAdapter
                binding.acpSuanaSpinner.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            if (position == 0 && suanaItems.isNotEmpty()) {

                                if (service == "Register") {
                                    registerLocationModel = SetRegisterLocationModel(
                                        getDeviceId(requireContext()),
                                        "",
                                        locationId
                                    )
                                    Log.d("onItemSelected: ", registerLocationModel.toString())
                                } else {
                                    unRegisterLocationModel = SetUnRegisterLocationModel(
                                        "",
                                        locationId
                                    )
                                    Log.d("onItemSelected: ", unRegisterLocationModel.toString())
                                }


                            } else if (suanaItems.isNotEmpty()) {

                                if (service == "Register") {
                                    registerLocationModel = SetRegisterLocationModel(
                                        getDeviceId(requireContext()),
                                        suanaItems[position - 1],
                                        locationId
                                    )
                                    Log.d("onItemSelected: ", registerLocationModel.toString())

                                } else {
                                    unRegisterLocationModel = SetUnRegisterLocationModel(
                                        suanaItems[position - 1],
                                        locationId
                                    )

                                    Log.d("onItemSelected: ", unRegisterLocationModel.toString())

                                }

                            }

                        }

                    }

            }
        }
    }


    private fun setOnClickListener() {
        binding.btnRegister.setOnClickListener {
            if (service == "Register") {
                if (!::registerLocationModel.isInitialized) {
                    dockActivity.showErrorMessage("Unable to register device")
                } else {
                    callApi("register_device", registerLocationModel)
                }
            } else {

                if (!::unRegisterLocationModel.isInitialized) {
                    dockActivity.showErrorMessage("Unable to unregister device")
                } else {
                    callApi("unRegister_device", unRegisterLocationModel)
                }
            }

        }
    }


    private fun setText() {
        if (service == "Register") {
            binding.tvRegister.text = getString(R.string.register_device)
            binding.tvRegisterContext.text = getString(R.string.register_device_context)
            binding.btnRegister.text = "REGISTER"
        } else {
            binding.tvRegister.text = getString(R.string.unregister_device)
            binding.tvRegisterContext.text = getString(R.string.unregister_device_context)
            binding.btnRegister.text = "UNREGISTER"
        }

    }


    override fun setTitleBar(titleBar: TitleBar) {
        super.setTitleBar(titleBar)
        titleBar.showBackButton()
        titleBar.hideNotificationBtn();
    }


}