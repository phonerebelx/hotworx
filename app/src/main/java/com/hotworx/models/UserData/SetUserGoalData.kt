package com.hotworx.models.UserData

data class SetUserGoalData(
    val current_weight: String = "",
    val target_weight: String = "",
    val target_weight_goal_date: String = "",
    val weekly_session_goal: String = ""
)
