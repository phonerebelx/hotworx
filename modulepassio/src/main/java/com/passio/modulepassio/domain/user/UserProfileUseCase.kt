package com.passio.modulepassio.domain.user

import android.util.Log
import com.passio.modulepassio.Passio.HotworxUserProfile
import com.passio.modulepassio.Passio.interfaces.ProfileDataCallback
import com.passio.modulepassio.interfaces.NutritionDataCallback
import com.passio.modulepassio.data.Repository
import com.passio.modulepassio.ui.model.UserProfile

object UserProfileUseCase {

    private val repository = Repository.getInstance()

    private var nutritionCallback: NutritionDataCallback? = null
    private var callback: ProfileDataCallback? = null

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

    suspend fun getUserProfile(): UserProfile {
        return repository.getUser()
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
}