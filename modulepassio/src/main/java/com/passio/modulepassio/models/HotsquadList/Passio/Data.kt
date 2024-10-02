package com.passio.modulepassio.models.HotsquadList.Passio

data class Data(
    val additionalData: String,
    val createdAt: Int,
    val iconId: String,
    val id: String,
    val ingredients: List<IngredientX>,
    val mealLabel: String,
    val name: String,
    val selectedQuantity: Double,
    val selectedUnit: String,
    val servingSizes: List<ServingSizeXX>,
    val servingUnits: List<ServingUnitXX>,
    val uuid: String
)