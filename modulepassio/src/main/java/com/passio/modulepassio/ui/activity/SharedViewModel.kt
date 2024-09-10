package com.passio.modulepassio.ui.activity

import com.passio.modulepassio.data.ResultWrapper
import com.passio.modulepassio.domain.user.UserProfileUseCase
import com.passio.modulepassio.ui.model.FoodRecord
import com.passio.modulepassio.ui.model.FoodRecordIngredient
import com.passio.modulepassio.ui.model.UserProfile
import com.passio.modulepassio.ui.model.WaterRecord
import com.passio.modulepassio.ui.model.WeightRecord
import com.passio.modulepassio.ui.util.SingleLiveEvent
import ai.passio.passiosdk.passiofood.PassioFoodDataInfo
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

object UserCache{
    private lateinit var userProfile: UserProfile
    fun getProfile() : UserProfile{
        return userProfile
    }
    fun setProfile(userProfile: UserProfile)
    {
        this.userProfile = userProfile
    }
}
class SharedViewModel : ViewModel() {

    private val _editFoodRecordLD = SingleLiveEvent<FoodRecord>()
    val editFoodRecordLD: LiveData<FoodRecord> get() = _editFoodRecordLD

    private val _editIngredientLD = SingleLiveEvent<Pair<FoodRecordIngredient, Int>>()
    val editIngredientLD: LiveData<Pair<FoodRecordIngredient, Int>> get() = _editIngredientLD

    private val _addIngredientLD = SingleLiveEvent<FoodRecord>()
    val addIngredientLD: LiveData<FoodRecord> get() = _addIngredientLD

    private val _editSearchResultLD = SingleLiveEvent<PassioFoodDataInfo>()
    val editSearchResultLD: LiveData<PassioFoodDataInfo> get() = _editSearchResultLD


    private val _nutritionInfoFoodRecordLD = SingleLiveEvent<FoodRecord>()
    val nutritionInfoFoodRecordLD: LiveData<FoodRecord> get() = _nutritionInfoFoodRecordLD


    private val _addWeightLD = SingleLiveEvent<WeightRecord>()
    val addWeightLD: LiveData<WeightRecord> get() = _addWeightLD

    private val _addWaterLD = SingleLiveEvent<WaterRecord>()
    val addWaterLD: LiveData<WaterRecord> get() = _addWaterLD

    private val _photoFoodResultLD = SingleLiveEvent<List<Bitmap>>()
    val photoFoodResultLD: LiveData<List<Bitmap>> get() = _photoFoodResultLD

    private val userProfileCase = UserProfileUseCase

    private val _userProfileCacheEvent = SingleLiveEvent<ResultWrapper<UserProfile>>()
    val userProfileCacheEvent: LiveData<ResultWrapper<UserProfile>> get() = _userProfileCacheEvent

    init {
        preCacheUserProfile()
    }


    private fun preCacheUserProfile() {
        viewModelScope.launch {
            val userProfile = userProfileCase.getUserProfile()
            UserCache.setProfile(userProfile)
            _userProfileCacheEvent.postValue(ResultWrapper.Success(userProfile))
        }
    }


    fun passToNutritionInfo(foodRecord: FoodRecord) {
        _nutritionInfoFoodRecordLD.postValue(foodRecord)
    }

    fun passToEdit(searchResult: PassioFoodDataInfo) {
        _editSearchResultLD.postValue(searchResult)
    }

    fun editIngredient(ingredient: FoodRecordIngredient, ingredientIndex: Int) {
        _editIngredientLD.postValue(ingredient to ingredientIndex)
    }

    fun editFoodRecord(foodRecord: FoodRecord) {
        _editFoodRecordLD.postValue(foodRecord)
    }

    fun addIngredient(foodRecord: FoodRecord) {
        _addIngredientLD.postValue(foodRecord)
    }
    fun addEditWeight(weightRecord: WeightRecord) {
        _addWeightLD.postValue(weightRecord)
    }
    fun addEditWater(waterRecord: WaterRecord) {
        _addWaterLD.postValue(waterRecord)
    }
    fun addPhotoFoodResult(uris: List<Bitmap>) {
        _photoFoodResultLD.postValue(uris)
    }

}