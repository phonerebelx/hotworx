package com.hotworx.models.SessionBookingModel.FinalSessionBookingModel

data class GetLevelTwoDataModel(
    val dates: ArrayList<String>,
    val list: ArrayList<ListData>,
    val location_details: LocationDetails,
    val session_type: SessionType
)