package com.hotsquad.hotsquadlist.model.request

import com.google.gson.annotations.SerializedName
import org.apache.commons.lang3.StringUtils

data class LoginRequest(
    
    @SerializedName("phone")
    var phone: String = StringUtils.EMPTY,

    @SerializedName("password")
    var password: String = StringUtils.EMPTY
)