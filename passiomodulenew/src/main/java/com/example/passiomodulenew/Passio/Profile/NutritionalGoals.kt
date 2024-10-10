package com.example.passiomodulenew.Passio.Profile

data class NutritionalGoals(
    val activity_level: String,
    val calorie_deficit: String,
    val diet: String,
    val goal_water: Int,
    val goal_weight_timeline: String,
    val nutrition_percentage: NutritionPercentage,
    val recommended_calories: Int,
    val target_calories: Int,
    val target_weight: Int,
    val weekly_session_goal: Int
)