package com.hotworx

import android.util.Log
import com.hotworx.models.HotsquadList.Passio.GetPassioModel
import com.passio.modulepassio.Singletons.ApiHeaderSingleton.apiHeader
import com.passio.modulepassio.domain.diary.DiaryUseCase.getWebService
import com.passio.modulepassio.domain.diary.DiaryUseCase.requireContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PassioService {

    suspend fun fetchPassioData(day: Date): GetPassioModel? {
        return try {
            // You can adjust this to send the appropriate date
            val formattedDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(day)
            val response = getWebService().getPassioData(
                apiHeader(requireContext())
                , formattedDate)

//            if (response.isSuccessful) {
//                response.body()?.let {
//                    return it
//                }
//            }
            null
        } catch (e: Exception) {
            Log.e("APIError", "Error fetching Passio data: ${e.message}")
            null
        }
    }
}
