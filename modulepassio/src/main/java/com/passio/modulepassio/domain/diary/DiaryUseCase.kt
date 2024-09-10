package com.passio.modulepassio.domain.diary

import android.view.View
import com.passio.modulepassio.data.Repository
import com.passio.modulepassio.ui.model.FoodRecord
import java.util.Date

object DiaryUseCase{

    private val repository = Repository.getInstance()

//    override fun ResponseSuccess(result: String?, tag: String?) {
//        if (!isAdded) return
//
//        if (result != null && tag == WebServiceConstants.GET_PASSIO_LIST) {
//        } else {
//        }
//    }

    suspend fun getLogsForDay(day: Date): List<FoodRecord> {
//        if(true){
//            //Get Api
//            return repository.getLogsForDay(day)
//        }else{
//            return repository.getLogsForDay(day)
//        }
//        return if (shouldFetchFromApi()) {
//            fetchPassioData(day)
//            repository.getLogsForDay(day)
//        } else {
//            repository.getLogsForDay(day)
//        }
        return repository.getLogsForDay(day)
    }

//    private fun fetchPassioData(day: Date) {
//        val request = getPassioRequest(day.toString())
//        getServiceHelper().enqueueCall(
//            getWebService().getPassioData(
//                apiHeader(requireContext()),
//                request
//            ),
//            WebServiceConstants.GET_PASSIO_LIST,
//            true
//        )
//    }
//
//    private fun shouldFetchFromApi(): Boolean {
//        return true
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