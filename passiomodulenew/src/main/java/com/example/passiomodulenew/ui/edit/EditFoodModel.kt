package com.example.passiomodulenew.ui.edit

import com.example.passiomodulenew.ui.model.FoodRecord


data class EditFoodModel(
    val foodRecord: FoodRecord?,
    val showIngredients: Boolean,
)