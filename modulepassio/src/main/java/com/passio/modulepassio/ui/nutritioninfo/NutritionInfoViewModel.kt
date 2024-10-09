package com.passio.modulepassio.ui.nutritioninfo

import com.passio.modulepassio.ui.model.MicroNutrient
import androidx.lifecycle.LiveData
import com.passio.modulepassio.ui.base.BaseViewModel
import com.passio.modulepassio.ui.model.FoodRecord
import com.passio.modulepassio.ui.util.SingleLiveEvent

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