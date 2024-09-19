package com.hotworx.models.HotsquadList.Passio

data class Ingredient(
    val details: String,
    val entityType: String,
    val iconId: String,
    val name: String,
    val nutrients: Nutrients,
    val passioID: String,
    val selectedQuantity: Double,
    val selectedUnit: String,
    val servingSizes: List<ServingSizeX>,
    val servingUnits: List<ServingUnitX>
)