package com.hotworx.models.SessionBookingModel

data class SessionBookingDataModel(
    val frequently_locations: ArrayList<Location>,
    val locations: ArrayList<Location>,
    val session_type: SessionType
)