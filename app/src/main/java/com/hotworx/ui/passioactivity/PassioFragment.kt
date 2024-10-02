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
import com.hotworx.Singletons.ApiHeaderSingleton
import com.hotworx.Singletons.ApiHeaderSingleton.apiHeader
import com.hotworx.databinding.FragmentPassioBinding
import com.hotworx.global.Constants
import com.hotworx.global.WebServiceConstants
import com.hotworx.models.HotsquadList.Passio.FoodEntry
import com.hotworx.models.HotsquadList.Passio.PostPassioResponse
import com.hotworx.models.HotsquadList.Passio.postPassioRequest
import com.hotworx.retrofit.GsonFactory
import com.hotworx.ui.fragments.BaseFragment
import com.hotworx.ui.views.TitleBar
import com.passio.modulepassio.NutritionUIModule
import com.passio.modulepassio.domain.diary.DiaryUseCase
import com.passio.modulepassio.domain.mealplan.MealPlanUseCase
import com.passio.modulepassio.interfaces.DeletePassioDataCallback
import com.passio.modulepassio.interfaces.PassioDataCallback
import com.passio.modulepassio.interfaces.PostPassioDataCallback
import com.passio.modulepassio.models.HotsquadList.Passio.DeleteMealData
import com.passio.modulepassio.ui.model.FoodRecord
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PassioFragment : BaseFragment(), PassioDataCallback ,PostPassioDataCallback,DeletePassioDataCallback{
    private var _binding: FragmentPassioBinding? = null
    private val binding get() = _binding
    private lateinit var passioList: com.passio.modulepassio.models.HotsquadList.Passio.GetPassioResponse
    private lateinit var recordList: com.passio.modulepassio.models.HotsquadList.Passio.PostPassioResponse
    private lateinit var deleteList: com.passio.modulepassio.models.HotsquadList.Passio.DeleteMealData
    private lateinit var records: List<FoodRecord>
    private lateinit var recordsData: FoodRecord
    private var isApiCallInProgress = false // Flag to prevent multiple calls

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("PassioFragmenttttt", "Fragment attached")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPassioBinding.inflate(inflater, container, false)
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
        DiaryUseCase.setPassioDataCallback(this)
        DiaryUseCase.deletePassioDataCallback(this)
        MealPlanUseCase.postPassioDataCallback(this)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val logsDiary = DiaryUseCase.getLogsForDay(Date())
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
        if (!isAdded || _binding == null) return // Add safeguard check
        Log.d("PassioFragment", "Response received for tag: $tag")
        // Parse the response based on the tag
        if (tag == WebServiceConstants.GET_PASSIO_LIST) {
            val passioData = GsonFactory.getConfiguredGson().fromJson(result, com.passio.modulepassio.models.HotsquadList.Passio.GetPassioResponse::class.java)

            if (passioData != null) {
                onPassioDataSuccess(passioData)
            } else {
                onPassioDataError("Received empty response")
            }
        }
    }

    override fun ResponseFailure(message: String?, tag: String?) {
        if (tag == WebServiceConstants.GET_PASSIO_LIST) {
            onPassioDataError(message ?: "Unknown error")
        }
    }

    override fun onPassioDataSuccess(passioList: com.passio.modulepassio.models.HotsquadList.Passio.GetPassioResponse) {
        this.passioList = passioList
        Log.d("ParentFragment", "Passio data received: $passioList")
        DiaryUseCase.onPassioDataReceived(passioList)
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
                        val response = GsonFactory.getConfiguredGson()?.fromJson(responseJson, PostPassioResponse::class.java)!!
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
                        val response = GsonFactory.getConfiguredGson()?.fromJson(responseJson, DeleteMealData::class.java)!!
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
        Log.d("ParentFragmentMeal", "Passio data received: $records")

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

    override fun onPassioDataSuccess(recordList: com.passio.modulepassio.models.HotsquadList.Passio.PostPassioResponse) {
        this.recordList = recordList
        Log.d("recordList", "Record data received: $recordList")
        MealPlanUseCase.onPassioDataPost("","",records)
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
    override fun onDeletePassioData(uuid: String, food_entry_date: String ,recordsData: FoodRecord) {
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
        DiaryUseCase.onPassioDataDelete("","",deleteList)
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
