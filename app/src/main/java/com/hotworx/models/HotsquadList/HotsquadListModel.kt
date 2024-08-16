package com.hotworx.models.HotsquadList

data class HotsquadListModel(
    val status: Boolean,
    val message: String,
    val `data`: List<Hotsquad> = emptyList()
)