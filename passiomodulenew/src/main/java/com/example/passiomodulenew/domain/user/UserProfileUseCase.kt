package com.example.passiomodulenew.domain.user

import com.example.passiomodulenew.data.Repository
import com.example.passiomodulenew.ui.model.UserProfile

object UserProfileUseCase {

    private val repository = Repository.getInstance()

    suspend fun updateUserProfile(userProfile: UserProfile): Boolean {
        return repository.updateUser(userProfile)
    }

    suspend fun getUserProfile(): UserProfile {
        return repository.getUser()
    }

}