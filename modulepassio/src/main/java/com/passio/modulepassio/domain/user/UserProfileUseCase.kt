package com.passio.modulepassio.domain.user

import com.passio.modulepassio.data.Repository
import com.passio.modulepassio.ui.model.UserProfile

object UserProfileUseCase {

    private val repository = Repository.getInstance()

    suspend fun updateUserProfile(userProfile: UserProfile): Boolean {
        return repository.updateUser(userProfile)
    }

    suspend fun getUserProfile(): UserProfile {
        return repository.getUser()
    }

}