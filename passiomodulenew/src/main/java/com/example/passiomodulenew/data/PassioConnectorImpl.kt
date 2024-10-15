package com.example.passiomodulenew.data

import android.util.Log
import android.view.WindowInsetsAnimation
import com.example.passiomodulenew.DietrixBaseURL.RetrofitClient
import com.example.passiomodulenew.Passio.GetPassioResponse.GetFoodRecord
import com.example.passiomodulenew.interfaces.PassioDataCallback
import com.example.passiomodulenew.ui.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class PassioConnectorImpl : PassioConnector {

    private var callback: PassioDataCallback? = null

    fun setPassioDataCallback(callback: PassioDataCallback) {
        this.callback = callback
    }

    override fun initialize() {
        // Initialize any required resources or state here
    }

    override suspend fun updateRecord(foodRecord: FoodRecord): Boolean {
        // Implement the logic to update a single food record
        return true // Replace with actual implementation
    }

    override suspend fun updateRecords(foodRecords: List<FoodRecord>): Boolean {
        // Implement the logic to update multiple food records
        return true // Replace with actual implementation
    }

    override suspend fun deleteRecord(uuid: String): Boolean {
        // Implement the logic to delete a record by its UUID
        return true // Replace with actual implementation
    }

    override suspend fun fetchDayRecords(day: Date): List<FoodRecord> {
//        callback?.onFetchPassioData(day)
//        getFoodDetails(day)
        Log.d("fetchFoodFromCon",day.toString())
        return emptyList() // Replace with actual implementation
    }

    override suspend fun fetchLogsRecords(startDate: Date, endDate: Date): List<FoodRecord> {
        // Implement the logic to fetch logs between two dates
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val currentDate = Date()
        val formattedDate = dateFormat.format(currentDate)

//        callback?.onFetchPassioData(startDate)
        return emptyList() // Replace with actual implementation
    }

    override fun onPassioDataReceived(passioData: GetFoodRecord?) {
        if (passioData!!.isNotEmpty()) {
            Log.d("DiaryUseCaseeee success gte", "Passio data received get: $passioData")

        } else {
            Log.d("DiaryUseCaseeee failed get", "Received null Passio data")

        }
    }

//    private fun getFoodDetails(date: Date) {
//        // Format the date to the required string format
//        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//        val formattedDate = dateFormat.format(date)
//
//        RetrofitClient.instance.getFoodRecord(formattedDate).enqueue(object : Callback<GetFoodRecord> {
//            override fun onResponse(call: Call<GetFoodRecord>, response: Response<GetFoodRecord>) {
//                if (response.isSuccessful) {
//                    val foodRecord = response.body()
//                    // Handle the user data here
//                    foodRecord?.let {
////                        println("User: ${it.}, Email: ${it.email}")
//                        Log.d("GettingData", foodRecord[0].uuid)
//                    }
//                } else {
//                    // Handle error
//                    println("Error: ${response.code()}")
//                }
//            }
//            override fun onFailure(call: Call<GetFoodRecord>, t: Throwable) {
//                // Handle failure
//                println("Failure: ${t.message}")
//            }
//        })
//    }

    override suspend fun updateFavorite(foodRecord: FoodRecord) {
        // Implement the logic to update a favorite food record
    }

    override suspend fun deleteFavorite(foodRecord: FoodRecord) {
        // Implement the logic to delete a favorite food record
    }

    override suspend fun fetchFavorites(): List<FoodRecord> {
        // Implement the logic to fetch all favorite food records
        return emptyList() // Replace with actual implementation
    }

    override suspend fun fetchAdherence(): List<Long> {
        // Implement the logic to fetch adherence data
        return emptyList() // Replace with actual implementation
    }

    override suspend fun fetchUserProfile(): UserProfile {
        // Implement the logic to fetch the user profile
        return UserProfile() // Replace with actual implementation
    }

    override suspend fun updateUserProfile(userProfile: UserProfile): Boolean {
        // Implement the logic to update the user profile
        return true // Replace with actual implementation
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

    override suspend fun saveCustomFood(foodRecord: FoodRecord): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun fetchCustomFoods(): List<FoodRecord> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchCustomFoods(searchQuery: String): List<FoodRecord> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchCustomFood(uuid: String): FoodRecord? {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCustomFood(uuid: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getCustomFoodUsingBarcode(barcode: String): FoodRecord? {
        TODO("Not yet implemented")
    }

    override suspend fun saveRecipe(foodRecord: FoodRecord): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun fetchRecipe(uuid: String): FoodRecord? {
        TODO("Not yet implemented")
    }

    override suspend fun fetchRecipes(): List<FoodRecord> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchRecipes(searchQuery: String): List<FoodRecord> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteRecipe(uuid: String): Boolean {
        TODO("Not yet implemented")
    }

}
