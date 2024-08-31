package com.passio.passiomodule.domain.user

import com.passio.passiomodule.data.Repository
import com.passio.passiomodule.ui.model.UserProfile

object UserProfileUseCase {

    private val repository = Repository.getInstance()

    suspend fun updateUserProfile(userProfile: UserProfile): Boolean {
        return repository.updateUser(userProfile)
    }

    suspend fun getUserProfile(): UserProfile {
        return repository.getUser()
    }

}