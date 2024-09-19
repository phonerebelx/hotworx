package com.passio.modulepassio.domain.mealplan

import com.passio.modulepassio.data.Repository
import com.passio.modulepassio.ui.model.FoodRecord
import com.passio.modulepassio.ui.model.MealLabel
import com.passio.modulepassio.ui.util.dateToTimestamp
import ai.passio.passiosdk.passiofood.PassioFoodDataInfo
import ai.passio.passiosdk.passiofood.PassioMealTime
import ai.passio.passiosdk.passiofood.data.measurement.Grams
import ai.passio.passiosdk.passiofood.data.model.PassioAdvisorFoodInfo
import ai.passio.passiosdk.passiofood.data.model.PassioMealPlanItem
import ai.passio.passiosdk.passiofood.data.model.PassioSpeechRecognitionModel
import android.util.Log
import com.passio.modulepassio.domain.diary.DiaryUseCase
import com.passio.modulepassio.interfaces.PassioDataCallback
import com.passio.modulepassio.interfaces.PostPassioDataCallback
import com.passio.modulepassio.models.HotsquadList.Passio.GetPassioResponse
import java.util.Date

object MealPlanUseCase {

    private val repository = Repository.getInstance()
    private var callback: PostPassioDataCallback? = null

    fun postPassioDataCallback(callback: PostPassioDataCallback) {
        this.callback = callback
    }

    suspend fun getFoodRecord(
        passioFoodDataInfo: PassioFoodDataInfo,
        passioMealTime: PassioMealTime,
        weighGrams: Double? = null
    ): FoodRecord? {
        val foodItem = repository.fetchPassioFoodItem(passioFoodDataInfo, weighGrams) ?: return null

        val nutritionPreview = passioFoodDataInfo.nutritionPreview
        val foodRecord = FoodRecord(foodItem)
        foodRecord.mealLabel = MealLabel.stringToMealLabel(passioMealTime.mealName)
        if (weighGrams == null || weighGrams == 0.0) {
            if (foodRecord.setSelectedUnit(nutritionPreview.servingUnit)) {
                val quantity = nutritionPreview.servingQuantity
                foodRecord.setSelectedQuantity(quantity)
            } else {
                val weight = nutritionPreview.weightQuantity
                if (foodRecord.setSelectedUnit(Grams.unitName)) {
                    foodRecord.setSelectedQuantity(weight)
                }
            }
        }
        return foodRecord
    }

    suspend fun getFoodRecord(passioMealPlanItem: PassioMealPlanItem): FoodRecord? {
        return getFoodRecord(passioMealPlanItem.meal, passioMealPlanItem.mealTime)
    }

    suspend fun getFoodRecords(passioMealPlanItems: List<PassioMealPlanItem>): List<FoodRecord> {
        return passioMealPlanItems.mapNotNull { passioMealPlanItem ->
            getFoodRecord(passioMealPlanItem)
        }
    }

    suspend fun getFoodRecords(
        passioMealPlanItems: List<PassioAdvisorFoodInfo>,
        passioMealTime: PassioMealTime
    ): List<FoodRecord> {
        return passioMealPlanItems.mapNotNull { passioMealPlanItem ->
            getFoodRecord(
                passioMealPlanItem.foodDataInfo!!,
                passioMealTime,
                passioMealPlanItem.weightGrams
            )
        }
    }

    suspend fun getFoodRecordsFromSpeech(
        passioMealPlanItems: List<PassioSpeechRecognitionModel>,
        passioMealTime: PassioMealTime
    ): List<FoodRecord> {
        return passioMealPlanItems.mapNotNull { passioMealPlanItem ->
            getFoodRecord(
                passioMealPlanItem.advisorInfo.foodDataInfo!!,
                passioMealPlanItem.mealTime ?: passioMealTime,
                passioMealPlanItem.advisorInfo.weightGrams
            )?.apply {
                create(dateToTimestamp(passioMealPlanItem.date, "yyyy-MM-dd"))
            }
        }
    }

    suspend fun logFoodRecord(record: FoodRecord): Boolean {
        record.create(record.createdAtTime() ?: Date().time)
        return repository.logFoodRecord(record)
    }

    suspend fun logFoodRecords(records: List<FoodRecord>): Boolean {
        //post api
        val date = Date()

        Log.d("logFoodRecords", records.toString())

        // Notify callback with the list of records
        callback?.onPostPassioData(records)

        records.forEach { record ->
            record.create(record.createdAtTime() ?: Date().time)
        }

//        return repository.logFoodRecords(records)
        return try {
            val result = repository.logFoodRecords(records)
            // Notify callback of success
            if (result) {
                callback?.onPassioDataSuccess(records)
            } else {
                // Notify callback of failure if the result is not successful
                callback?.onPassioDataError("Failed to post records")
            }
            result
        } catch (e: Exception) {
            // Notify callback of error
            callback?.onPassioDataError(e.message ?: "Unknown error")
            false
        }
    }

    // This method will be called from the parent once the API data is available
    fun onPassioDataPost(records: List<FoodRecord>) {
        if (records.isNotEmpty()) {
            Log.d("MealPlanUseCaseeee", "Passio data received: $records")
            // Process the data
        } else {
            Log.d("MealPlanUseCaseeee", "Received empty Passio data")
        }
    }
}