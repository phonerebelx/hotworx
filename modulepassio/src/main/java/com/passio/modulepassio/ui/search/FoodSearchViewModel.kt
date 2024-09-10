package com.passio.modulepassio.ui.search


import ai.passio.passiosdk.passiofood.PassioFoodDataInfo
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.passio.modulepassio.data.ResultWrapper
import com.passio.modulepassio.domain.mealplan.MealPlanUseCase
import com.passio.modulepassio.domain.search.SearchUseCase
import com.passio.modulepassio.ui.base.BaseViewModel
import com.passio.modulepassio.ui.util.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FoodSearchViewModel : BaseViewModel() {

    private val useCase = SearchUseCase
    private val mealPlanUseCase = MealPlanUseCase

    private val _logFoodEvent = SingleLiveEvent<ResultWrapper<Boolean>>()
    val logFoodEvent: LiveData<ResultWrapper<Boolean>> = _logFoodEvent
    val showLoading = SingleLiveEvent<Boolean>()

    data class SearchResult(
        val query: String,
        val results: List<PassioFoodDataInfo>,
        val suggestions: List<String>
    )

    val searchResults = MutableLiveData<SearchResult>()

    fun fetchSearchResults(query: String) {
        viewModelScope.launch {
            val result = useCase.fetchSearchResults(query)
            searchResults.postValue(SearchResult(query, result.first, result.second))
        }
    }

    fun logFood(passioFoodDataInfo: PassioFoodDataInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            showLoading.postValue(true)
            mealPlanUseCase.getFoodRecord(passioFoodDataInfo, passioMealTimeNow())?.let {
                _logFoodEvent.postValue(ResultWrapper.Success(mealPlanUseCase.logFoodRecord(it)))
            }
                ?: _logFoodEvent.postValue(ResultWrapper.Error("Could not fetch food item for: ${passioFoodDataInfo.foodName}"))

            showLoading.postValue(false)
        }
    }

    fun requestNavigateBack() {
        navigateBack()
    }

    fun navigateToEdit() {
        navigate(FoodSearchFragmentDirections.searchToEdit())
    }
    fun navigateToDiary()
    {
        viewModelScope.launch(Dispatchers.Main) {
            navigate(FoodSearchFragmentDirections.searchToDiary())
        }
    }
}