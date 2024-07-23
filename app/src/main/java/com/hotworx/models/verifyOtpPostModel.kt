package com.hotworx.models

data class VerifyOtpPostModel(
    var email_address:  String = "",
    var password:  String = "",
    var phone_number:  String = "",
    var device_id:  String = "",
    var otp:  String = "",
    var type:  String = "",


)
