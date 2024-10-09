package com.hotworx.ui.passioactivity

import ai.passio.passiosdk.core.config.PassioConfiguration
import ai.passio.passiosdk.core.config.PassioMode
import ai.passio.passiosdk.passiofood.PassioSDK
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import com.example.passiomodulenew.NutritionUIModule
import com.example.passiomodulenew.Passio.DeleteMealData
import com.example.passiomodulenew.Passio.GetPassioResponse
import com.example.passiomodulenew.domain.diary.DiaryUseCase
import com.example.passiomodulenew.domain.mealplan.MealPlanUseCase
import com.example.passiomodulenew.interfaces.DeletePassioDataCallback
import com.example.passiomodulenew.interfaces.PassioDataCallback
import com.example.passiomodulenew.interfaces.PostPassioDataCallback
import com.example.passiomodulenew.ui.model.FoodRecord
import com.hotworx.Singletons.ApiHeaderSingleton
import com.hotworx.Singletons.ApiHeaderSingleton.apiHeader
import com.hotworx.databinding.FragmentNewPassioBinding
import com.hotworx.global.Constants
import com.hotworx.global.WebServiceConstants
import com.hotworx.models.HotsquadList.Passio.FoodEntry
import com.hotworx.models.HotsquadList.Passio.PostPassioResponse
import com.hotworx.models.HotsquadList.Passio.postPassioRequest
import com.hotworx.retrofit.GsonFactory
import com.hotworx.ui.fragments.BaseFragment
import com.hotworx.ui.views.TitleBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NewPassioFragment : BaseFragment(), PassioDataCallback, PostPassioDataCallback,
    DeletePassioDataCallback {
    private var _binding: FragmentNewPassioBinding? = null
    private val binding get() = _binding
    private lateinit var passioList: GetPassioResponse
    private lateinit var recordList: com.example.passiomodulenew.Passio.PostPassioResponse
    private lateinit var deleteList: DeleteMealData
    private lateinit var records: List<FoodRecord>
    private lateinit var recordsData: FoodRecord
    private var isApiCallInProgress = false // Flag to prevent multiple calls

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("PassioFragmenttttt", "Fragment attached")
    }

//    private val passioConnector: PassioHotsquadConnector = PassioHotsquadConnector().apply {
////        initialize()
//        setPassioDataCallback(this@PassioFragment)  // Set the callback
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewPassioBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val passioConfiguration = PassioConfiguration(
            this.requireContext(),
            "pHAFLe95rgGnHjSZwkZIBrY0pyqXchBZymTtp4gX4hoZ"
        ).apply {
            sdkDownloadsModels = true
            debugMode = -333
        }

        PassioSDK.instance.configure(passioConfiguration) { passioStatus ->
            Log.d("HHHH", passioStatus.toString())
            when (passioStatus.mode) {
                PassioMode.NOT_READY -> onSDKError("Not ready")
                PassioMode.FAILED_TO_CONFIGURE -> onSDKError(getString(passioStatus.error!!.errorRes))
                PassioMode.IS_READY_FOR_DETECTION -> onSDKReady()
                PassioMode.IS_BEING_CONFIGURED -> {
                }

                PassioMode.IS_DOWNLOADING_MODELS -> {}
            }
        }

        // Set the callback before calling DiaryUseCase
//        passioConnector
        DiaryUseCase.setPassioDataCallback(this)
//        PassioHotsquadConnector().setPassioDataCallback(this)
        DiaryUseCase.deletePassioDataCallback(this)
        MealPlanUseCase.postPassioDataCallback(this)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val logsDiary = DiaryUseCase.getLogsForDay(Date())
//                val logsDiary = DiaryUseCase.getLogsForLast30Days()
                val deleteDiary = DiaryUseCase.deleteRecord(FoodRecord())
                val logsMeal = MealPlanUseCase.logFoodRecord(FoodRecord())
                // Switch to the main thread for UI updates
                withContext(Dispatchers.Main) {
                    // Check if the fragment is still attached and binding is available before updating UI
                    if (isAdded && binding != null) {
                        binding?.textView?.text = "Logs received: ${logsDiary.size}"
                        Log.e("PassioFragmentTExtData", "Logs received: ${logsDiary.size}")
                        Log.e("PassioLogMeal", "Logs received: ${logsMeal}")
                        Log.e("DeleteeeeLogMeal", "Logs received: ${deleteDiary}")
                    } else {
                        Log.e("PassioFragment", "Fragment is not attached, skipping UI update")
                    }
                }
            } catch (e: Exception) {
                // Handle error on the main thread
                withContext(Dispatchers.Main) {
                    // Ensure the fragment is attached before updating UI on error
                    if (isAdded && binding != null) {
                        binding?.textView?.text = "Error: ${e.message}"
                        Log.e("PassioFragmentTExtData", "Error: ${e.message}")
                    }
                }
            }
        }

    }

    private fun onSDKError(error: String) {
        binding?.textView?.text = "ERROR: $error"
        Log.e("SDKKKKERRORRR", "ERROR: $error")
    }

    private fun onSDKReady() {
        NutritionUIModule.launch(this.requireContext())
        Log.d("ytytytuy", "jhkjhkhlkhlkllhlk")
//        requireActivity().finish()
    }

    override fun setTitleBar(titleBar: TitleBar) {
        super.setTitleBar(titleBar)

        titleBar.showBackButton()
    }

    override fun onFetchPassioData(day: Date) {
        if (!isAdded || context == null) {
            Log.e("PassioFragment", "Fragment is not attached, skipping API call")
            return
        }
        val formattedDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(day)
        Log.d("PassioFragment", "Formatted date for API: $formattedDate")

        getServiceHelper().enqueueCall(
            getWebService().getPassioData(apiHeader(requireContext()), formattedDate),
            WebServiceConstants.GET_PASSIO_LIST,
            true
        )
    }

    override fun ResponseSuccess(result: String?, tag: String?) {
        if (!isAdded || _binding == null) return
        Log.d("PassioFragment", "Response received for tag: $tag")
        if (tag == WebServiceConstants.GET_PASSIO_LIST) {

            val passioData =
                GsonFactory.getConfiguredGson().fromJson(result, GetPassioResponse::class.java)

//            if (passioData != null && passioData.isNotEmpty()) {
//                onPassioDataSuccess(passioData)
//                Log.d("Success1DiaryUseCaseGetResponseSuccess", "Callback to fetch passio data for day: $passioData")
//            } else {
//                Log.d("PassioFragment", "Received empty response, showing empty message")
////                showEmptyMessage()
//                onPassioDataError("Received empty response")
//            }
            if (passioList.isNotEmpty()) {
                Log.d("PassioFragment", "Received non-empty Passio data")
                DiaryUseCase.onPassioDataReceived(passioList) // Pass the API data to the use case.
            } else {
                Log.d("PassioFragment", "Received empty Passio data")
                DiaryUseCase.onPassioDataReceived(GetPassioResponse()) // Pass an empty instance of GetPassioResponse
            }
        }
    }

    override fun ResponseFailure(message: String?, tag: String?) {
        if (tag == WebServiceConstants.GET_PASSIO_LIST) {
            onPassioDataError(message ?: "Unknown error")

        }
    }

    override fun onPassioDataSuccess(passioList: GetPassioResponse) {
        this.passioList = passioList
        if (passioList.isNotEmpty()) {
//            PassioHotsquadConnector().onPassioDataReceived(passioList)
            DiaryUseCase.onPassioDataReceived(passioList)
        } else {
            Log.d("PassioFragmentSuccess", "Received empty Passio data, not fetching local data")
        }
//        if (passioList.isNotEmpty()) {
//            Log.d("PassioFragmentSuccess", "Received non-empty Passio data")
//
//            // Map the passioList to a list of FoodRecord
//            apiPassioList = passioList.map { passioItem ->
//                FoodRecord().apply {
//                    id = passioItem.id
//                    name = passioItem.name
//                    additionalData = passioItem.details // Assuming this maps to additionalData; adjust as necessary
//                    iconId = passioItem.iconId
//                }
//            }
//
//            Log.d("PassioFragmentGETT", "API data set successfully: $apiPassioList")
//            DiaryUseCase.onPassioDataReceived(passioList) // Pass the API data to the use case.
//        } else {
//            Log.d("PassioFragmentGETTLOCALLL", "Received empty Passio data")
//            DiaryUseCase.onPassioDataReceived(GetPassioResponse()) // Pass an empty instance.
//        }
    }

    override fun onPassioDataError(error: String) {
        if (::passioList.isInitialized) {
            Log.d("DiaryUseCase", "Received Passio data from parent: $passioList")
        } else {
            Log.d("DiaryUseCase", "passioList is not initialized. Error: $error")
        }
        Log.e("DiaryUseCase", "Server issue: $error")
    }

    //POST API

    override fun onSuccess(liveData: LiveData<String>, tag: String) {
        super.onSuccess(liveData, tag)
        when (tag) {
            Constants.POST_PASSIO_RECORD -> {
                val responseJson = liveData.value
                Log.d("Response", "LiveData value: $responseJson")

                if (responseJson != null) {
                    try {
                        val response = GsonFactory.getConfiguredGson()
                            ?.fromJson(responseJson, PostPassioResponse::class.java)!!
                        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
                        val currentDate = Date()
                        val formattedDate = dateFormat.format(currentDate)
                        if (response != null) {
                            onPassioDataSuccess(recordList)
                        } else {
                            onPassioDataError("Received empty response")
                        }
                    } catch (e: Exception) {
                        dockActivity?.showErrorMessage(e.message.toString())
                        Log.i("Error", e.message.toString())
                    }
                } else {
                    Log.e("Error", "LiveData value is null")
                    dockActivity?.showErrorMessage("No response from server")
                }
            }

            Constants.DELETE_PASSIO_RECORD -> {
                val responseJson = liveData.value
                Log.d("Response", "LiveData value: $responseJson")

                if (responseJson != null) {
                    try {
                        val response = GsonFactory.getConfiguredGson()
                            ?.fromJson(responseJson, DeleteMealData::class.java)!!
                        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
                        val currentDate = Date()
                        val formattedDate = dateFormat.format(currentDate)
                        if (response != null) {
                            deleteDataSuccess(deleteList)
                        } else {
                            deleteDataError("Received empty response")
                        }
                    } catch (e: Exception) {
                        dockActivity?.showErrorMessage(e.message.toString())
                        Log.i("Error", e.message.toString())
                    }
                } else {
                    Log.e("Error", "LiveData value is null")
                    dockActivity?.showErrorMessage("No response from server")
                }
            }
        }
    }

    override fun onPostPassioData(date: String, url: String, records: List<FoodRecord>) {
        if (!isAdded || context == null) {
            Log.e("PassioFragment", "Fragment is not attached, skipping API call")
            return
        }
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val currentDate = Date()
        val formattedDate = dateFormat.format(currentDate)
        this.records = records
        Log.d("ParentFragmentMeal", "Passio data received post: $records")

        val foodEntries = records.map { record ->
            FoodEntry(
                food_entry_date = formattedDate,  // Use the formatted current date
                "",           // Replace with actual URL if available
                food_list = listOf(record)
            )
        }

        // Create the request object
        val request = postPassioRequest(food_entries = foodEntries)

        // Make the API call
        getServiceHelper()?.enqueueCallExtended(
            getWebService()?.postPassioData(
                ApiHeaderSingleton.apiHeader(requireContext()),
                request
            ), Constants.POST_PASSIO_RECORD, true
        )
    }

    override fun onPassioDataSuccess(recordList: com.example.passiomodulenew.Passio.PostPassioResponse) {
        this.recordList = recordList
        Log.d("recordList", "Record data received: $recordList")
        MealPlanUseCase.onPassioDataPost("", "", records)
    }

    override fun Error(error: String) {
        if (::recordList.isInitialized) {
            Log.d("RecordListtt", "Received Passio data from parent: $recordList")
        } else {
            Log.d("RecordListtt", "passioList is not initialized. Error: $error")
        }
        Log.e("RecordListtt", "Server issue: $error")
    }

    //Delete Passio Record
    override fun onDeletePassioData(
        uuid: String,
        food_entry_date: String,
        recordsData: FoodRecord
    ) {
        if (!isAdded || context == null) {
            Log.e("DeletePassioFragment", "Fragment is not attached, skipping API call")
            return
        }
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val currentDate = Date()
        val formattedDate = dateFormat.format(currentDate)
        this.recordsData = recordsData

        // Make the API call
        getServiceHelper()?.enqueueCallExtended(
            getWebService()?.deletePassioData(
                ApiHeaderSingleton.apiHeader(requireContext()),
                recordsData.uuid,
                formattedDate
            ), Constants.DELETE_PASSIO_RECORD, true
        )
    }

    override fun deleteDataSuccess(deleteList: DeleteMealData) {
        this.deleteList = deleteList
        Log.d("DeleteList", "Record data received: $deleteList")
        DiaryUseCase.onPassioDataDelete("", "", deleteList)
    }

    override fun deleteDataError(error: String) {
        if (::deleteList.isInitialized) {
            Log.d("deleteListtttttt", "Received Passio data from parent: $deleteList")
        } else {
            Log.d("deleteListtttttt", "passioList is not initialized. Error: $error")
        }
        Log.e("deleteListtttttt", "Server issue: $error")
    }
}