package com.hotworx.models.SessionBookingModel.FinalSessionBookingModel

data class PostBookSessionDataModel(
    val sauna_no: String = "",
    val time_slot: String = "",
    val booking_date: String = "",
    val session_type: String = "",
    val selected_location_id: String = "",
    val duration: Int = 0,

)
