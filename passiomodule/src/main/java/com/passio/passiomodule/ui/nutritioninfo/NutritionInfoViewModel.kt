package com.passio.passiomodule.ui.nutritioninfo

import com.passio.passiomodule.ui.base.BaseViewModel
import com.passio.passiomodule.ui.model.FoodRecord
import com.passio.passiomodule.ui.model.MicroNutrient
import com.passio.passiomodule.ui.util.SingleLiveEvent
import androidx.lifecycle.LiveData

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