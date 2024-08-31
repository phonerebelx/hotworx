package com.passio.passiomodule.ui.edit

import com.passio.passiomodule.data.ResultWrapper
import com.passio.passiomodule.domain.search.EditFoodUseCase
import com.passio.passiomodule.ui.base.BaseViewModel
import com.passio.passiomodule.ui.model.FoodRecord
import com.passio.passiomodule.ui.model.FoodRecordIngredient
import com.passio.passiomodule.ui.model.MealLabel
import com.passio.passiomodule.ui.util.SingleLiveEvent
import ai.passio.passiosdk.passiofood.PassioFoodDataInfo
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditFoodViewModel : BaseViewModel() {

    private val useCase = EditFoodUseCase

    private val _editFoodModelLD = MutableLiveData<EditFoodModel>()
    val editFoodModelLD: LiveData<EditFoodModel> get() = _editFoodModelLD
    private val _internalUpdate = SingleLiveEvent<Pair<FoodRecord, EditFoodFragment.UpdateOrigin>>()
    val internalUpdate: LiveData<Pair<FoodRecord, EditFoodFragment.UpdateOrigin>> get() = _internalUpdate
    private var isEditMode = false


    private val _resultLogFood = MutableLiveData<ResultWrapper<FoodRecord>>()
    val resultLogFood: LiveData<ResultWrapper<FoodRecord>> get() = _resultLogFood

    private lateinit var foodRecord: FoodRecord
    private var ingredientIndex = -1

    fun setEditMode(isEditMode: Boolean) {
        this.isEditMode = isEditMode
    }

    fun setFoodRecord(foodRecord: FoodRecord) {
        Log.d("logFoodRecord", "tttt=== uuid ${foodRecord.uuid}")
        this.foodRecord = foodRecord
        val model = EditFoodModel(foodRecord, true, true)
        _editFoodModelLD.postValue(model)
    }

    fun getFoodRecord(searchResult: PassioFoodDataInfo) {
        viewModelScope.launch {
            val fr = useCase.getFoodRecord(searchResult)
            val model = EditFoodModel(fr, true, true)
            _editFoodModelLD.postValue(model)
            if (fr != null) {
                foodRecord = fr
            }
        }
    }

    fun getFoodRecordForIngredient(ingredient: FoodRecordIngredient, ingredientIndex: Int) {
        this.ingredientIndex = ingredientIndex
        val ingredientRecord = FoodRecord(ingredient)
        val model = EditFoodModel(ingredientRecord, false, true)
        _editFoodModelLD.postValue(model)
        this.foodRecord = ingredientRecord
    }

    fun updateServingQuantity(value: Double, origin: EditFoodFragment.UpdateOrigin) {
        foodRecord.setSelectedQuantity(value)
        _internalUpdate.postValue(foodRecord to origin)
    }

    fun updateServingUnit(index: Int, origin: EditFoodFragment.UpdateOrigin) {
        val unit = foodRecord.servingUnits[index].unitName
        foodRecord.setSelectedUnit(unit)
        _internalUpdate.postValue(foodRecord to origin)
    }

    fun updateMealLabel(mealLabel: MealLabel) {
        foodRecord.mealLabel = mealLabel
    }

    fun updateCreatedAt(date: Long) {
        foodRecord.create(date)
    }

    fun removeIngredient(index: Int) {
        foodRecord.removeIngredient(index)
        _internalUpdate.postValue(foodRecord to EditFoodFragment.UpdateOrigin.INGREDIENT)
    }

    fun getIngredient(index: Int) = foodRecord.ingredients[index]

    fun logCurrentRecord() {
        viewModelScope.launch {
            if (useCase.logFoodRecord(foodRecord, isEditMode)) {
                _resultLogFood.postValue(ResultWrapper.Success(foodRecord))
            } else {
                _resultLogFood.postValue(ResultWrapper.Error("Failed to log food. Please try again"))
            }
        }
    }

    fun navigateToDiary(createdAtTime: Long?) {
        viewModelScope.launch(Dispatchers.Main) {
            navigate(EditFoodFragmentDirections.editToDiary(currentDate = createdAtTime ?: 0))
        }

    }

    fun navigateToNutritionInfo(): FoodRecord {
        viewModelScope.launch(Dispatchers.Main) {
            navigate(EditFoodFragmentDirections.editToNutritionInfo())
        }
        return foodRecord

    }

    fun navigateToAddIngredient(): FoodRecord {
        // navigate(EditFoodFragmentDirections.editToSearch())
        return foodRecord
    }
}