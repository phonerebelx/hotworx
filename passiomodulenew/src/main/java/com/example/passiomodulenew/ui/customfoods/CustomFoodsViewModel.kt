package com.example.passiomodulenew.ui.customfoods

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.passiomodulenew.data.ResultWrapper
import com.example.passiomodulenew.domain.customfood.CustomFoodUseCase
import com.example.passiomodulenew.ui.base.BaseViewModel
import  com.example.passiomodulenew.ui.myfood.MyFoodsFragmentDirections
import com.example.passiomodulenew.ui.model.FoodRecord
import com.example.passiomodulenew.ui.util.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CustomFoodsViewModel : BaseViewModel() {

    private val useCase = CustomFoodUseCase

    private val _customFoodListEvent = MutableLiveData<List<FoodRecord>>()
    val customFoodListEvent: LiveData<List<FoodRecord>> = _customFoodListEvent

    private val _showLoading = SingleLiveEvent<Boolean>()
    val showLoading: LiveData<Boolean> = _showLoading

    private val _logFoodEvent = SingleLiveEvent<ResultWrapper<Boolean>>()
    val logFoodEvent: LiveData<ResultWrapper<Boolean>> = _logFoodEvent

    fun getCustomFoods() {
        viewModelScope.launch {
            _showLoading.postValue(true)
            val customFoods = useCase.fetchCustomFoods()
            _customFoodListEvent.postValue(customFoods)
            _showLoading.postValue(false)

        }
    }

    fun deleteCustomFood(uuid: String) {
        viewModelScope.launch {
            _showLoading.postValue(true)
            useCase.deleteCustomFood(uuid)
            getCustomFoods()
            _showLoading.postValue(false)
        }
    }

    fun logCustomFood(foodRecord: FoodRecord) {
        viewModelScope.launch {
            _showLoading.postValue(true)
            _logFoodEvent.postValue(ResultWrapper.Success(useCase.logCustomFood(foodRecord)))
            _showLoading.postValue(false)
        }
    }

    fun navigateToFoodCreator() {
        navigate(MyFoodsFragmentDirections.myFoodsToFoodCreator())
    }

    fun navigateToEditFood() {
        navigate(MyFoodsFragmentDirections.myFoodsToEdit())
    }

    fun navigateToDiary() {
        viewModelScope.launch(Dispatchers.Main) {
            navigate(MyFoodsFragmentDirections.myFoodsToDiary())
        }
    }
}