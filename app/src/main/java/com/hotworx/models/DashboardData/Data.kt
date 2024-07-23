package com.hotworx.models.DashboardData

data class Data(
    val ninety_days_summary: ArrayList<NinetyDaysSummary>,
    val summary: Summary,
    val todays_completed_sessions: ArrayList<TodaysPendingSession>,
    val todays_pending_sessions: ArrayList<TodaysPendingSession>
)