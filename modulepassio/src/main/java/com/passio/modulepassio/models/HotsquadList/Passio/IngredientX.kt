package com.passio.modulepassio.models.HotsquadList.Passio

data class IngredientX(
    val additionalData: String,
    val iconId: String,
    val id: String,
    val name: String,
    val referenceNutrients: ReferenceNutrients,
    val selectedQuantity: Double,
    val selectedUnit: String,
    val servingSizes: List<ServingSizeXX>,
    val servingUnits: List<ServingUnitXX>
)