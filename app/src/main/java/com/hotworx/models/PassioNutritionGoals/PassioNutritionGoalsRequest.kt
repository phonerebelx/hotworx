package com.hotworx.models.PassioNutritionGoals

import java.util.Date

data class PassioNutritionGoalsRequest(
    val weekly_session_goal: Int,
    val target_calories: Int,
    val activity_level: String,
    val goal_water: Double,
    val goal_weight: Double,
    val goal_weight_timeline: String,
    val calorie_deficit: String,
    val diet: String,
    val nutrition_percentage: NutritionPercentage
)
data class NutritionPercentage(
    val carbs: Int,
    val protein: Int,
    val fat: Int
)