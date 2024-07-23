package com.hotworx.models.SessionBookingModel.FinalSessionBookingModel

data class ShowAppointmentDataModel(
    val session: String = "",
    val time: String = "",
    val location: String = "",
    val date: String = "",
    val duration: Int
)
