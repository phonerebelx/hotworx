package com.hotworx.models.HotsquadList

import com.google.gson.annotations.SerializedName

data class HotsquadItem(
    @SerializedName("squad_id")
    val squadId: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("desc")
    val description: String,

    @SerializedName("total_members")
    val totalMembers: Int
)