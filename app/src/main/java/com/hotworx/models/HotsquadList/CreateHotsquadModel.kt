package com.hotworx.models.HotsquadList

data class CreateHotsquadModel(
    val status: Boolean,
    val message: String,
    val data: List<Hotsquad>
)