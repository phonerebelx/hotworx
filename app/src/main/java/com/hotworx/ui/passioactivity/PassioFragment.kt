package com.hotworx.ui.passioactivity

import ai.passio.passiosdk.core.config.PassioConfiguration
import ai.passio.passiosdk.core.config.PassioMode
import ai.passio.passiosdk.passiofood.PassioSDK
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hotworx.R
import com.hotworx.databinding.FragmentAddListBinding
import com.hotworx.databinding.FragmentPassioBinding
import com.hotworx.global.WebServiceConstants
import com.hotworx.ui.fragments.BaseFragment
import com.passio.modulepassio.NutritionUIModule
import com.passio.modulepassio.domain.diary.DiaryUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

class PassioFragment : BaseFragment(){
    private var _binding: FragmentPassioBinding? = null
    private val binding get() = _binding
    private lateinit var textView: TextView

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
    }

    private fun onSDKError(error: String) {
        textView.text = "ERROR: $error"
    }

    private fun onSDKReady() {
        NutritionUIModule.launch(this.requireContext())
        Log.d("ytytytuy","jhkjhkhlkhlkllhlk")
        requireActivity().finish()
    }

//    override fun ResponseSuccess(result: String?, Tag: String?) {
//        when (Tag) {
//            WebServiceConstants.GET_SQUAD_LIST -> {
//                hotsquadListModel = GsonFactory.getConfiguredGson().fromJson(result, HotsquadListModel::class.java)
//
//                if (hotsquadListModel.data.isNullOrEmpty()) {
//
//                } else {
//                    // Call the child module's function (e.g., DiaryUseCase.getLogsForDay) automatically
//                    CoroutineScope(Dispatchers.IO).launch {
//                        try {
//                            val day = Date() // Example, you can adjust this to the correct date
//                            val foodLogs = DiaryUseCase.getLogsForDay(day)
//
//                            // Update UI with the result (foodLogs) on the main thread
//                            withContext(Dispatchers.Main) {
//                                Log.d("FoodLogs", "Fetched food logs: $foodLogs")
//                                // Do something with the fetched food logs if needed
//                            }
//                        } catch (e: Exception) {
//                            Log.e("APIError", "Error fetching logs: ${e.message}")
//                        }
//                    }
//                }
//            }
//            else -> {
//                Log.d("ResponseSuccessss", "Unhandled Tag: $Tag")
//            }
//        }
//    }
}