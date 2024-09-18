package com.passio.modulepassio.interfaces

import android.util.Log
import com.google.android.gms.common.api.Response
import com.passio.modulepassio.Singletons.ApiHeaderSingleton
import com.passio.modulepassio.Singletons.ApiHeaderSingleton.apiHeader
import com.passio.modulepassio.domain.diary.DiaryUseCase.getServiceHelper
import com.passio.modulepassio.domain.diary.DiaryUseCase.getWebService
import com.passio.modulepassio.domain.diary.DiaryUseCase.requireContext
import com.passio.modulepassio.ui.util.Constant
import okhttp3.ResponseBody
import java.text.SimpleDateFormat
import java.util.Locale

class RepositoryPassio {
    
    companion object {
        private var INSTANCE: RepositoryPassio? = null
        fun getInstance(): RepositoryPassio {
            if (INSTANCE == null) {
                INSTANCE = RepositoryPassio()
            }
            return INSTANCE!!
        }
    }

    suspend fun getPassioData(date: String) {
        val formattedDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(date)
        Log.d("PassioFragment", "Formatted date for API: $formattedDate")
        getServiceHelper().enqueueCall(
            getWebService().getPassioData(apiHeader(requireContext()), "20240918"),
            Constant.WebServiceConstants.GET_PASSIO_LIST, // Assuming you have a constant for this API call
            true
        )
    }
}