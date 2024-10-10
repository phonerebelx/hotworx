package com.example.passiomodulenew.interfaces

import com.example.passiomodulenew.Passio.GetPassioResponse
import com.example.passiomodulenew.Passio.Profile.HotworxUserProfile
import com.example.passiomodulenew.ui.model.UserProfile
import java.util.Date

interface NutritionDataCallback {
    fun onPostNutritionData(profile: UserProfile)
    fun onNutritionDataSuccess(userProfile: HotworxUserProfile)
    fun onNutritionDataError(error: String)
}
