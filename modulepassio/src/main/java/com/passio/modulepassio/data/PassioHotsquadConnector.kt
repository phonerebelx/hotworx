package com.passio.modulepassio.data

import android.util.Log
import com.passio.modulepassio.interfaces.PassioDataCallback
import com.passio.modulepassio.models.HotsquadList.Passio.GetPassioResponse
import com.passio.modulepassio.ui.model.FoodRecord
import com.passio.modulepassio.ui.model.UserProfile
import com.passio.modulepassio.ui.model.WaterRecord
import com.passio.modulepassio.ui.model.WeightRecord
import java.text.SimpleDateFormat
import java.util.*

class PassioHotsquadConnector: PassioConnector {

    private var callback: PassioDataCallback? = null
    private val repository = Repository.getInstance()

    override fun initialize() {
        TODO("Not yet implemented")
    }

    fun setPassioDataCallback(callback: PassioDataCallback) {
        this.callback = callback
    }

    override suspend fun updateRecord(foodRecord: FoodRecord): Boolean {
        TODO("Not yet implemented")
        //Post
    }

    override suspend fun updateRecords(foodRecords: List<FoodRecord>): Boolean {
        TODO("Not yet implemented")
        //Post
    }

    override suspend fun deleteRecord(foodRecord: FoodRecord): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun fetchDayRecords(day: Date): List<FoodRecord> {
        TODO("Not yet implemented")

        // Notify the callback that data fetching is about to happen
        callback?.onFetchPassioData(day)

        // Simulate data fetching
        val records = repository.getLogsForDay(day)
        Log.d("jbsfjksjkbf",records.toString())
        // Notify that data has been received
//        callback?.onPassioDataSuccess(records)

        return records
    }

    override suspend fun fetchLogsRecords(startDate: Date, endDate: Date): List<FoodRecord> {
        //Get api
        TODO("Not yet implemented")
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val currentDate = Date()
        val formattedDate = dateFormat.format(currentDate)
        // Notify the callback that data fetching is about to happen
        callback?.onFetchPassioData(currentDate)

        // Simulate data fetching
        val records = repository.getLogsForDay(currentDate)
        Log.d("jbsfjksjkbf",records.toString())
        // Notify that data has been received
//        callback?.onPassioDataSuccess(records)

        return records
    }

    // This method will be called from the parent once the API data is available
    fun onPassioDataReceived(passioList: GetPassioResponse) {
        if (passioList.isNotEmpty()) {
            Log.d("DiaryUseCaseeee success gte", "Passio data received get: $passioList")

        } else {
            Log.d("DiaryUseCaseeee failed get", "Received null Passio data")

        }
    }

    override suspend fun updateFavorite(foodRecord: FoodRecord) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFavorite(foodRecord: FoodRecord) {
        TODO("Not yet implemented")
    }

    override suspend fun fetchFavorites(): List<FoodRecord> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchAdherence(): List<Long> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchUserProfile(): UserProfile {
        TODO("Not yet implemented")
    }

    override suspend fun updateUserProfile(userProfile: UserProfile): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun updateWeightRecord(weightRecord: WeightRecord): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun removeWeightRecord(weightRecord: WeightRecord): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun fetchWeightRecords(startDate: Date, endDate: Date): List<WeightRecord> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchLatestWeightRecord(): WeightRecord? {
        TODO("Not yet implemented")
    }

    override suspend fun updateWaterRecord(waterRecord: WaterRecord): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun removeWaterRecord(waterRecord: WaterRecord): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun fetchWaterRecords(startDate: Date, endDate: Date): List<WaterRecord> {
        TODO("Not yet implemented")
    }


}
