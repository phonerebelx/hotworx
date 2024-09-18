package com.passio.modulepassio.models.HotsquadList.Passio

data class GetPassioResponseItem(
    val barcode: String,
    val createdAt: Double,
    val details: String,
    val entityType: String,
    val iconId: String,
    val id: String,
    val ingredients: List<Ingredient>,
    val mealLabel: String,
    val name: String,
    val openFoodLicense: String,
    val passioID: String,
    val scannedUnitName: String,
    val selectedQuantity: Int,
    val selectedUnit: String,
    val servingSizes: List<ServingSizeX>,
    val servingUnits: List<ServingUnitX>,
    val uuid: String
)