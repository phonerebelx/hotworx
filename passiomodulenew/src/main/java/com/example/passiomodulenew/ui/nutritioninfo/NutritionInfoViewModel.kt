package com.example.passiomodulenew.ui.nutritioninfo

import com.example.passiomodulenew.ui.model.MicroNutrient
import androidx.lifecycle.LiveData
import com.example.passiomodulenew.ui.base.BaseViewModel
import com.example.passiomodulenew.ui.model.FoodRecord
import com.example.passiomodulenew.ui.util.SingleLiveEvent

class NutritionInfoViewModel : BaseViewModel() {

    private val _logsLD = SingleLiveEvent<List<MicroNutrient>>()
    val logsLD: LiveData<List<MicroNutrient>> get() = _logsLD


    private val _foodRecord = SingleLiveEvent<FoodRecord>()
    val foodRecord: LiveData<FoodRecord> get() = _foodRecord

    fun setFoodRecord(foodRecord: FoodRecord) {
        _foodRecord.postValue(foodRecord)
        _logsLD.postValue(MicroNutrient.nutrientsFromFoodRecords(listOf(foodRecord)))
    }

}