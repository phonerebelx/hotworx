package com.hotworx.models.HotsquadList

import com.google.gson.annotations.SerializedName

data class HotsquadItem(
    val squad_id: String,
    val name: String,
    val desc: String,
    val total_members: Int
)