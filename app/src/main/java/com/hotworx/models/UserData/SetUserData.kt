package com.hotworx.models.UserData

data class SetUserData(
    val first_name: String = "",
    val last_name: String = "",
    val image_url: String = "",
    val dob: String = "",
    val age: String = "",
    val gender: String = "",
    val height: Double = 0.0,
    val weight: Double = 0.0,
    val address: String = ""
)
