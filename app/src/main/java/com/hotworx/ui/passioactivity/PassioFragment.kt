package com.hotworx.ui.passioactivity

import ai.passio.passiosdk.core.config.PassioConfiguration
import ai.passio.passiosdk.core.config.PassioMode
import ai.passio.passiosdk.passiofood.PassioSDK
import android.content.Context
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
import com.hotworx.models.HotsquadList.HotsquadListModel
import com.hotworx.models.HotsquadList.Passio.GetPassioModel
import com.hotworx.retrofit.GsonFactory
import com.hotworx.ui.fragments.BaseFragment
import com.hotworx.ui.views.TitleBar
import com.passio.modulepassio.NutritionUIModule
import com.passio.modulepassio.Singletons.ApiHeaderSingleton.apiHeader
import com.passio.modulepassio.domain.diary.DiaryUseCase
import com.passio.modulepassio.interfaces.PassioDataCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PassioFragment : BaseFragment(), PassioDataCallback {
    private var _binding: FragmentPassioBinding? = null
    private val binding get() = _binding
    private lateinit var passioList: com.passio.modulepassio.models.HotsquadList.Passio.GetPassioResponse

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

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val logs = DiaryUseCase.getLogsForDay(Date())
                // Switch to the main thread for UI updates
                withContext(Dispatchers.Main) {
                    // Check if the fragment is still attached and binding is available before updating UI
                    if (isAdded && binding != null) {
                        binding?.textView?.text = "Logs received: ${logs.size}"
                        Log.e("PassioFragmentTExtData", "Logs received: ${logs.size}")
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
}