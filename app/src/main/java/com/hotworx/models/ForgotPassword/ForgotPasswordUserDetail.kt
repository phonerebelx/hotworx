package com.hotworx.models.ForgotPassword

data class ForgotPasswordUserDetail(
    val email_address: String = "",
    val phone_number: String = "",
    val device_id: String = "",
    val type: String = ""
)