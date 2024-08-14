package com.hotworx.models.HotsquadList

import com.google.gson.annotations.SerializedName
import com.hotworx.models.NotificationHistory.Data

data class HotsquadListModel(
    val `data`: List<HotsquadItem> = emptyList(),
    val msg: String
)