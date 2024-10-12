package com.example.passiomodulenew.interfaces

import com.example.passiomodulenew.Passio.Profile.HotworxUserProfile
import com.example.passiomodulenew.ui.model.UserProfile
import java.util.Date

interface ProfileDataCallback {
    fun onPostProfileData(profile: UserProfile)
    fun onProfileDataSuccess(userProfile: HotworxUserProfile)
    fun onProfileDataError(error: String)
}
