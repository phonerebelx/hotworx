package com.example.passiomodulenew.data

import com.example.passiomodulenew.ui.model.FoodRecord
import com.example.passiomodulenew.ui.model.UserProfile
import com.example.passiomodulenew.ui.model.WaterRecord
import com.example.passiomodulenew.ui.model.WeightRecord
import java.util.*

interface PassioConnector {

    fun initialize()

    suspend fun updateRecord(foodRecord: FoodRecord): Boolean

    suspend fun updateRecords(foodRecords: List<FoodRecord>): Boolean

    suspend fun deleteRecord(uuid: String): Boolean

    suspend fun fetchDayRecords(day: Date): List<FoodRecord>

    suspend fun fetchLogsRecords(startDate: Date, endDate: Date): List<FoodRecord>

    suspend fun updateFavorite(foodRecord: FoodRecord)

    suspend fun deleteFavorite(foodRecord: FoodRecord)

    suspend fun fetchFavorites(): List<FoodRecord>

    suspend fun fetchAdherence(): List<Long>

    suspend fun fetchUserProfile(): UserProfile

    suspend fun updateUserProfile(userProfile: UserProfile): Boolean

    suspend fun updateWeightRecord(weightRecord: WeightRecord): Boolean

    suspend fun removeWeightRecord(weightRecord: WeightRecord): Boolean

    suspend fun fetchWeightRecords(startDate: Date, endDate: Date): List<WeightRecord>

    suspend fun fetchLatestWeightRecord(): WeightRecord?

    suspend fun updateWaterRecord(waterRecord: WaterRecord): Boolean

    suspend fun removeWaterRecord(waterRecord: WaterRecord): Boolean

    suspend fun fetchWaterRecords(startDate: Date, endDate: Date): List<WaterRecord>

    suspend fun saveCustomFood(foodRecord: FoodRecord): Boolean

    suspend fun fetchCustomFoods(): List<FoodRecord>
    suspend fun fetchCustomFoods(searchQuery: String): List<FoodRecord>

    suspend fun fetchCustomFood(uuid: String): FoodRecord?

    suspend fun deleteCustomFood(uuid: String): Boolean

    suspend fun getCustomFoodUsingBarcode(barcode: String): FoodRecord?

    suspend fun saveRecipe(foodRecord: FoodRecord): Boolean

    suspend fun fetchRecipe(uuid: String): FoodRecord?

    suspend fun fetchRecipes(): List<FoodRecord>

    suspend fun fetchRecipes(searchQuery: String): List<FoodRecord>

    suspend fun deleteRecipe(uuid: String): Boolean
}
