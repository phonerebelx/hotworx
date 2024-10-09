package com.hotworx.models.HotsquadList.Passio

import com.example.passiomodulenew.ui.model.FoodRecord


data class FoodEntry(
    val food_entry_date: String,
    val url: String,
    val food_list: List<FoodRecord>
)