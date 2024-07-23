package com.hotworx.models.NewActivityModels

data class TimelineActivityDataModel(
    val activities: List<NinetyDaysActivity>,
    val ninety_days_activities: List<NinetyDaysActivity>,
    val no_of_pages: Int
)