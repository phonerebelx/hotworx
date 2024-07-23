package com.hotworx.models.BrivoDataModels.BrivoLocation


data class Site(
    val accessPoints: List<AccessPoint>,
    val address: Address,
    val hasTrustedNetwork: Boolean,
    val id: Int,
    val preScreening: String,
    val siteName: String,
    val timeZone: String
)