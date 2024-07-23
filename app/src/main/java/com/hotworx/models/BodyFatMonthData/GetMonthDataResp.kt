package com.hotworx.models.BodyFatMonthData

data class GetMonthDataResp(
    val `data`: List<Data>,
    val msg: String // success
)