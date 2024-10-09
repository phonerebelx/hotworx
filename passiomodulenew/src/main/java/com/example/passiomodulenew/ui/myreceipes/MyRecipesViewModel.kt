package com.example.passiomodulenew.ui.myreceipes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.passiomodulenew.data.ResultWrapper
import com.example.passiomodulenew.domain.recipe.RecipeUseCase
import com.example.passiomodulenew.ui.base.BaseViewModel
import  com.example.passiomodulenew.ui.myfood.MyFoodsFragmentDirections
import com.example.passiomodulenew.ui.model.FoodRecord
import com.example.passiomodulenew.ui.util.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyRecipesViewModel : BaseViewModel() {

    private val useCase = RecipeUseCase

    private val _recipeListEvent = MutableLiveData<List<FoodRecord>>()
    val recipeListEvent: LiveData<List<FoodRecord>> = _recipeListEvent

    private val _showLoading = SingleLiveEvent<Boolean>()
    val showLoading: LiveData<Boolean> = _showLoading

    private val _logRecipeEvent = SingleLiveEvent<ResultWrapper<Boolean>>()
    val logRecipeEvent: LiveData<ResultWrapper<Boolean>> = _logRecipeEvent

    fun getRecipes() {
        viewModelScope.launch {
            _showLoading.postValue(true)
            val customFoods = useCase.fetchRecipes()
            _recipeListEvent.postValue(customFoods)
            _showLoading.postValue(false)

        }
    }

    fun deleteRecipe(uuid: String) {
        viewModelScope.launch {
            _showLoading.postValue(true)
            useCase.deleteRecipe(uuid)
            getRecipes()
            _showLoading.postValue(false)
        }
    }

    fun logRecipe(foodRecord: FoodRecord) {
        viewModelScope.launch {
            _showLoading.postValue(true)
            _logRecipeEvent.postValue(ResultWrapper.Success(useCase.logRecipe(foodRecord)))
            _showLoading.postValue(false)
        }
    }

    fun navigateToDiary() {
        viewModelScope.launch(Dispatchers.Main) {
            navigate(MyFoodsFragmentDirections.myFoodsToDiary())
        }
    }

    fun navigateToEditRecipe() {
        viewModelScope.launch(Dispatchers.Main) {
            navigate(MyFoodsFragmentDirections.myFoodsToEditRecipe())
        }
    }
    fun navigateToDetails() {
        viewModelScope.launch(Dispatchers.Main) {
            navigate(MyFoodsFragmentDirections.myFoodsToEdit())
        }
    }

}