package com.hotworx.models.HotsquadList

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserModel(
    @SerializedName("searchedName")
    var searchedName: String? = null,
)