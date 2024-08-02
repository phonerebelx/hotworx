package com.hotsquad.hotsquadlist.model.response

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class GlobalResponse
{
    @SerializedName("success")
    @Expose
    val success: Boolean? = null

    @SerializedName("message")
    @Expose
    val message: String? = null
}