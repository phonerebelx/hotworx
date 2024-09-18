package com.passio.modulepassio.domain.diary

import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import com.hotworx.models.HotsquadList.Passio.getPassioRequest
import com.passio.modulepassio.Singletons.ApiHeaderSingleton.apiHeader
import com.passio.modulepassio.data.Repository
import com.passio.modulepassio.ui.base.BaseFragment
import com.passio.modulepassio.ui.model.FoodRecord
import com.passio.modulepassio.ui.util.Constant
import java.util.Date

object DiaryUseCase{

    private val repository = Repository.getInstance()

    suspend fun getLogsForDay(day: Date): List<FoodRecord> {
//        fetchPassioData(day)
        return repository.getLogsForDay(day)
    }

//    private fun fetchPassioData(day: Date) {
//        val request = getPassioRequest(day.toString())
//        getServiceHelper().enqueueCall(
//            getWebService().getPassioData(
//                apiHeader(requireContext()),
//                request
//            ),
//            Constant.WebServiceConstants.GET_PASSIO_LIST,
//            true
//        )
//    }

    suspend fun getLogsForWeek(day: Date): List<FoodRecord> {
        return repository.getLogsForWeek(day)
    }

    suspend fun getLogsForMonth(day: Date): List<FoodRecord> {
        return repository.getLogsForMonth(day)
    }
    suspend fun getLogsForLast30Days(): List<FoodRecord> {
        return repository.getLogsForLast30Days()
    }

    suspend fun deleteRecord(foodRecord: FoodRecord): Boolean {
        return repository.deleteFoodRecord(record = foodRecord)
    }

    suspend fun fetchAdherence(): List<Long> {
        return repository.fetchAdherence()
    }
}