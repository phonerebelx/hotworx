package com.hotworx.models.BusinessCard

data class Data(
    val buy_url: String?,
    val currency_symbol: String?,
    val lead_id: String?,
    val location_code: String?,
    val location_name: String?,
    val location_address: String?,
    val trail_url: String?,
    val utm_list: ArrayList<UtmModel>
)