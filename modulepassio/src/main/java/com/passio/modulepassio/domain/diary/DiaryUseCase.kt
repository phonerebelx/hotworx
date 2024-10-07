package com.passio.modulepassio.domain.diary

import android.util.Log
import com.passio.modulepassio.data.PassioHotsquadConnector
import com.passio.modulepassio.data.Repository
import com.passio.modulepassio.interfaces.DeletePassioDataCallback
import com.passio.modulepassio.interfaces.PassioDataCallback
import com.passio.modulepassio.models.HotsquadList.Passio.DeleteMealData
import com.passio.modulepassio.models.HotsquadList.Passio.GetPassioResponse
import com.passio.modulepassio.models.HotsquadList.Passio.Ingredient
import com.passio.modulepassio.ui.model.FoodRecord
import com.passio.modulepassio.ui.model.FoodRecordIngredient
import com.passio.modulepassio.ui.util.getBefore30Days
import kotlinx.coroutines.delay
import org.joda.time.DateTime
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DiaryUseCase{

    private var callback: PassioDataCallback? = null
    private var callbackdelete: DeletePassioDataCallback? = null
    private val repository = Repository.getInstance()
    var apiPassioList: List<FoodRecord> = emptyList()
    fun setPassioDataCallback(callback: PassioDataCallback) {
        this.callback = callback
    }

    fun deletePassioDataCallback(callbackdelete: DeletePassioDataCallback) {
        this.callbackdelete = callbackdelete
    }

    suspend fun getLogsForDay(day: Date): List<FoodRecord> {
        Log.d("DiaryUseCase", "Callback to fetch passio data for day: $day")
        callback?.onFetchPassioData(day)
//        return repository.getLogsForDay(day)
        return if (apiPassioList.isNotEmpty()) {
            Log.d("DiaryUseCaseSucessss", "Using API data for logs")
            apiPassioList
        } else {
            Log.d("DiaryUseCaseLocallll", "API data not available, falling back to local data")
            repository.getLogsForDay(day) // This would only be used as a fallback.
        }
    }

    fun onPassioDataReceived(passioList: GetPassioResponse) {
        apiPassioList = passioList.toFoodRecordList()
        if (passioList.isNotEmpty()) {
            Log.d("DiaryUseCaseeeesuccessGet", "Passio data received get: $passioList")

        } else {
            Log.d("DiaryUseCaseeeefailedGet", "Received null Passio data")

        }
    }

    // Extension function to convert GetPassioResponse to List<FoodRecord>
    fun GetPassioResponse.toFoodRecordList(): List<FoodRecord> {
        return this.map { passioItem ->
            FoodRecord().apply {
                id = passioItem.id
                name = passioItem.name
                additionalData = passioItem.details // Assuming this maps to additionalData; adjust as necessary
                iconId = passioItem.iconId
                // Add any other necessary fields from passioItem to FoodRecord
            }
        }
    }

    suspend fun getLogsForWeek(day: Date): List<FoodRecord> {
        return repository.getLogsForWeek(day)
    }

    suspend fun getLogsForMonth(day: Date): List<FoodRecord> {
        return repository.getLogsForMonth(day)
    }
    suspend fun getLogsForLast30Days(): List<FoodRecord> {
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val currentDate = Date()
        val formattedDate = dateFormat.format(currentDate)
//        callback?.onFetchPassioData(currentDate)
        return repository.getLogsForLast30Days()
//        return repository.getLogsForDay(currentDate)
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