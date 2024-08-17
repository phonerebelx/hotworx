package com.hotworx.models.ViModel.GetAllLocation

data class Data(
    val location_id: String,
    val name: String,
    val sauna_details: ArrayList<SaunaDetail>
)