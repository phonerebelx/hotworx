package com.passio.modulepassio.domain.diary

import android.util.Log
import com.passio.modulepassio.data.Repository
import com.passio.modulepassio.interfaces.DeletePassioDataCallback
import com.passio.modulepassio.interfaces.PassioDataCallback
import com.passio.modulepassio.models.HotsquadList.Passio.DeleteMealData
import com.passio.modulepassio.models.HotsquadList.Passio.GetPassioResponse
import com.passio.modulepassio.ui.model.FoodRecord
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DiaryUseCase{

    private var callback: PassioDataCallback? = null
    private var callbackdelete: DeletePassioDataCallback? = null
    private val repository = Repository.getInstance()
    private var Status:Boolean = false

    fun setPassioDataCallback(callback: PassioDataCallback) {
        this.callback = callback
    }

    fun deletePassioDataCallback(callbackdelete: DeletePassioDataCallback) {
        this.callbackdelete = callbackdelete
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
        //Delete api
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val currentDate = Date()
        val formattedDate = dateFormat.format(currentDate)

        callbackdelete?.onDeletePassioData(foodRecord.uuid.toString(),formattedDate,foodRecord)

        return repository.deleteFoodRecord(record = foodRecord)
    }

    // This method will be called from the parent once the API data is available
    fun onPassioDataDelete(uuid:String,food_entry_date:String,deleteList: DeleteMealData) {
        if (deleteList.data.isNotEmpty()) {
            Log.d("DiaryDeletee", "Passio data received: $deleteList")
            // Process the data
        } else {
            Log.d("DiaryDeleteeElse", "Received empty Passio data")
        }
    }

    suspend fun fetchAdherence(): List<Long> {
        return repository.fetchAdherence()
    }
}