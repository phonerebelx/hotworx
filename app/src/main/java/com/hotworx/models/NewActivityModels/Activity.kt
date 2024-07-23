package com.hotworx.models.NewActivityModels

data class Activity(
    val total_burnt: String,
    val activity_id: String,
    val display_date: String,
    val start_date_time: String,
    val end_date_time: String,
    val workout_type: String,
    val location_name: String,
    val sauna_no: String,
)