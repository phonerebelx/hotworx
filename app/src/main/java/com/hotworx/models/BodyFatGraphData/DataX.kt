package com.hotworx.models.BodyFatGraphData

data class DataX(
    val daily_bodyfat: List<DailyBodyfat>,
    val message: String, // Success
    val status: Boolean // true
)