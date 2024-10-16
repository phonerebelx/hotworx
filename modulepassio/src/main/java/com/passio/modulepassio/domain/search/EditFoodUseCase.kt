package com.passio.modulepassio.domain.search

import com.passio.modulepassio.data.Repository
import com.passio.modulepassio.ui.model.FoodRecord
import ai.passio.passiosdk.passiofood.PassioFoodDataInfo
import android.util.Log
import com.passio.modulepassio.interfaces.PostPassioDataCallback
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object EditFoodUseCase {

    private val repository = Repository.getInstance()
    private var callback: PostPassioDataCallback? = null

    fun postPassioDataCallback(callback: PostPassioDataCallback) {
        this.callback = callback
    }
    suspend fun getFoodRecord(searchResult: PassioFoodDataInfo): FoodRecord? {
        val foodItem = repository.fetchPassioFoodItem(searchResult) ?: return null
        return FoodRecord(foodItem)
    }


    suspend fun logFoodRecord(record: FoodRecord, isEditMode: Boolean): Boolean {
        Log.d("logFoodRecord", "before=== uuid ${record.uuid}")
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val currentDate = Date()
        val formattedDate = dateFormat.format(currentDate)
        var recordList = ArrayList<FoodRecord>()
        recordList.add(record)
        callback?.onPostPassioData(formattedDate,"",recordList)
        if (!isEditMode) {
            record.create(record.createdAtTime() ?: Date().time)
        } else if (record.createdAtTime() == null) {
            record.create(Date().time)
        }
        Log.d("logFoodRecord", "after=== uuid ${record.uuid}")
        return repository.logFoodRecord(record)
    }
}