package com.hotworx.models.HotsquadList

data class FoundUser(
    val user_id: String,
    val name: String,
    val email: String,
    val phone: String,
    val profileImageUrl: String?
)