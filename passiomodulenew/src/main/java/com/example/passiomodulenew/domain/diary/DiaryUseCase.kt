package com.example.passiomodulenew.domain.diary

import android.util.Log
import com.example.passiomodulenew.Passio.DeleteMealData
import com.example.passiomodulenew.Passio.GetPassioResponse.GetFoodRecord
import com.example.passiomodulenew.data.Repository
import com.example.passiomodulenew.interfaces.DeletePassioDataCallback
import com.example.passiomodulenew.interfaces.PassioDataCallback
import com.example.passiomodulenew.ui.model.FoodRecord
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DiaryUseCase {

    private val repository = Repository.getInstance()

    private var callback: PassioDataCallback? = null
    private var callbackdelete: DeletePassioDataCallback? = null

    fun setPassioDataCallback(callback: PassioDataCallback) {
        this.callback = callback
    }

    fun deletePassioDataCallback(callbackdelete: DeletePassioDataCallback) {
        this.callbackdelete = callbackdelete
    }

    suspend fun getLogsForDay(day: Date): List<FoodRecord> {
        callback?.onFetchPassioData(day)

        return repository.getLogsForDay(day)
    }

    suspend fun getLogsForWeek(day: Date): List<FoodRecord> {

        callback?.onFetchPassioData(day)

        return repository.getLogsForDay(day)
    }

    // This method will be called from the parent once the API data is available
    fun onPassioDataReceived(passioList: GetFoodRecord) {
        if (passioList.isNotEmpty()) {
            Log.d("DiaryUseCaseeee success gte", "Passio data received get: $passioList")

        } else {
            Log.d("DiaryUseCaseeee failed get", "Received null Passio data")

        }
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

        return repository.deleteFoodRecord(foodRecord.uuid)
    }

    // This method will be called from the parent once the API data is available
    fun onPassioDataDelete(uuid:String,food_entry_date:String,deleteList: DeleteMealData) {
        if (deleteList.data.isNotEmpty()) {
            Log.d("DiaryDeletee", "Passio data received delete: $deleteList")
            // Process the data
        } else {
            Log.d("DiaryDeleteeElse", "Received empty Passio data")
        }
    }

    suspend fun fetchAdherence(): List<Long> {
        return repository.fetchAdherence()
    }
}