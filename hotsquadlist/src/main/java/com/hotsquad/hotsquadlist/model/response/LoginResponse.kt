package com.hotsquad.hotsquadlist.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginResponse
{
    @SerializedName("success")
    @Expose
    val success: Boolean? = null

    @SerializedName("message")
    @Expose
    val message: String? = null

    @SerializedName("token")
    @Expose
    val token: String? = null

    @SerializedName("name")
    @Expose
    val name: String? = null

    @SerializedName("image")
    @Expose
    val image: String? = null
}