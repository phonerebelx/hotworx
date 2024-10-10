package com.example.passiomodulenew.domain.user

import android.util.Log
import com.example.passiomodulenew.Passio.Profile.HotworxUserProfile
import com.example.passiomodulenew.data.Repository
import com.example.passiomodulenew.interfaces.NutritionDataCallback
import com.example.passiomodulenew.interfaces.PostPassioDataCallback
import com.example.passiomodulenew.interfaces.ProfileDataCallback
import com.example.passiomodulenew.ui.model.FoodRecord
import com.example.passiomodulenew.ui.model.UserProfile

object UserProfileUseCase {

    private val repository = Repository.getInstance()
    private var callback: ProfileDataCallback? = null
    private var nutritionCallback: NutritionDataCallback? = null

    fun postProfileDataCallback(callback: ProfileDataCallback) {
        this.callback = callback
    }

    fun postNutritionDataCallback(nutritionCallback: NutritionDataCallback) {
        this.nutritionCallback = nutritionCallback
    }

    suspend fun updateUserProfile(userProfile: UserProfile): Boolean {

        callback?.onPostProfileData(userProfile)
        nutritionCallback?.onPostNutritionData(userProfile)
        return repository.updateUser(userProfile)
    }

    // This method will be called from the parent once the API data is available
    fun onProfileDataPost(userProfile: HotworxUserProfile) {
        if (userProfile != null) {
            Log.d("MealPlanUseCaseeee", "Passio data received: $userProfile")
            // Process the data
        } else {
            Log.d("MealPlanUseCaseeee", "Received empty Passio data")
        }
    }

    suspend fun getUserProfile(): UserProfile {

        return repository.getUser()
    }
}