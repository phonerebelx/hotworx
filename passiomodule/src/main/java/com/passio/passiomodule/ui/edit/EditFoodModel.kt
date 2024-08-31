package com.passio.passiomodule.ui.edit

import com.passio.passiomodule.ui.model.FoodRecord

data class EditFoodModel(
    val foodRecord: FoodRecord?,
    val showIngredients: Boolean,
    val showSave: Boolean
)