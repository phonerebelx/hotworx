package com.hotworx.models.HotsquadList

import com.google.gson.annotations.SerializedName
import com.hotworx.models.NotificationHistory.Data

data class CreateHotsquadModel(
    val success: Boolean,
    val message: String,
    val data: List<HotsquadItem>
)