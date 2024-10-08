package com.example.passiomodulenew.domain.search

import com.example.passiomodulenew.data.Repository
import com.example.passiomodulenew.ui.model.FoodRecord
import com.example.passiomodulenew.ui.model.MealLabel
import com.example.passiomodulenew.ui.model.copy
import ai.passio.passiosdk.passiofood.PassioFoodDataInfo
import android.util.Log
import java.util.Date

object EditFoodUseCase {

    private val repository = Repository.getInstance()

    suspend fun getFoodRecord(searchResult: PassioFoodDataInfo): FoodRecord? {
        val foodItem = repository.fetchPassioFoodItem(searchResult) ?: return null
        return FoodRecord(foodItem)
    }

    suspend fun logFoodRecord(record: FoodRecord, isEditMode: Boolean): Boolean {
        Log.d("logFoodRecord", "before=== uuid ${record.uuid}")

        /*  if (!isEditMode && record.isCustomFood()) {
              record.copy().create(record.createdAtTime() ?: Date().time)
          } else if (!isEditMode && record.isRecipe()) {
              record.copy().create(record.createdAtTime() ?: Date().time)
          } else if (!isEditMode) {
              record.create(record.createdAtTime() ?: Date().time)
          } else if (record.createdAtTime() == null) {
              record.create(Date().time)
          }*/

        val foodRecord = if (!isEditMode && record.isCustomFood()) {
            record.copy()
        } else if (!isEditMode && record.isRecipe()) {
            record.copy()
        } else if (!isEditMode) {
            record.copy()
        } else {
            record
        }

        foodRecord.create(foodRecord.createdAtTime() ?: Date().time)
        if (foodRecord.mealLabel == null) {
            foodRecord.mealLabel = MealLabel.dateToMealLabel(foodRecord.createdAtTime()!!)
        }
        Log.d("logFoodRecord", "after=== uuid ${foodRecord.uuid}")
        return repository.logFoodRecord(foodRecord)
    }

    suspend fun deleteRecord(uuid: String): Boolean {
        return repository.deleteFoodRecord(uuid)
    }
}