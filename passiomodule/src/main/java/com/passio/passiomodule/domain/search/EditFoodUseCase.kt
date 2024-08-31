package com.passio.passiomodule.domain.search

import com.passio.passiomodule.data.Repository
import com.passio.passiomodule.ui.model.FoodRecord
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
        if (!isEditMode) {
            record.create(record.createdAtTime() ?: Date().time)
        } else if (record.createdAtTime() == null) {
            record.create(Date().time)
        }
        Log.d("logFoodRecord", "after=== uuid ${record.uuid}")
        return repository.logFoodRecord(record)
    }
}