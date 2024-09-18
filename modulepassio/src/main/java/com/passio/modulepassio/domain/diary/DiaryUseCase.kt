package com.passio.modulepassio.domain.diary

import android.util.Log
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import com.hotworx.models.HotsquadList.Passio.getPassioRequest
import com.passio.modulepassio.Singletons.ApiHeaderSingleton.apiHeader
import com.passio.modulepassio.data.Repository
import com.passio.modulepassio.interfaces.PassioDataCallback
import com.passio.modulepassio.models.HotsquadList.Passio.GetPassioResponse
import com.passio.modulepassio.ui.base.BaseFragment
import com.passio.modulepassio.ui.model.FoodRecord
import com.passio.modulepassio.ui.util.Constant
import java.util.Date

object DiaryUseCase:com.passio.modulepassio.BaseFragment(){

    private var callback: PassioDataCallback? = null
    private val repository = Repository.getInstance()

    // Add the setPassioDataCallback method to set the callback
    fun setPassioDataCallback(callback: PassioDataCallback) {
        this.callback = callback
    }

    suspend fun getLogsForDay(day: Date): List<FoodRecord> {
        Log.d("DiaryUseCase", "Callback to fetch passio data for day: $day")
        callback?.onFetchPassioData(day)

        return repository.getLogsForDay(day)
    }

    // This method will be called from the parent once the API data is available
    fun onPassioDataReceived(passioList: GetPassioResponse) {
        if (passioList.isNotEmpty()) {
            Log.d("DiaryUseCaseeee", "Passio data received: $passioList")
            // Process the data
        } else {
            Log.d("DiaryUseCaseeee", "Received null Passio data")
        }
    }

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