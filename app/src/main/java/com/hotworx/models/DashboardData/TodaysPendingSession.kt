package com.hotworx.models.DashboardData

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TodaysPendingSession(
    val apple_watch_type: String = "", // yoga
    val date: String = "", // Dec 04
    var duration: String = "", // 40
    val is_after_burnt: String = "", // no
    val lat: String = "",
    val lead_record_id: String = "", // 1092439
    val location_name: String = "", // Demo Location USA(AA0001)
    val long: String = "",
    val sauna: String = "", // 1
    val session_name: String = "", // HOT ISO
    val session_record_id: String = "", // 62091721
    val slot: String = "", // 12:45AM-01:30AM
    val type: String = "", // HOT ISO
    val week_day: String  = "", // Mon
    val cal_burnt: String = "",
    val startCalories: String = "",
    var comeFromActivity: Boolean = false,
    val display_date: String = "",
    val start_date_time: String = "",
    val end_date_time: String = "",
    val has_shared_access: Boolean,

): Serializable