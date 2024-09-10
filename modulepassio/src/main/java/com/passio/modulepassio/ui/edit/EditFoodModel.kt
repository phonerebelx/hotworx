package com.passio.modulepassio.ui.edit

import com.passio.modulepassio.ui.model.FoodRecord

data class EditFoodModel(
    val foodRecord: FoodRecord?,
    val showIngredients: Boolean,
    val showSave: Boolean
)