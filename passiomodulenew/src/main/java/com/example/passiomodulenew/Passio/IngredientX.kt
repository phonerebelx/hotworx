package com.example.passiomodulenew.Passio

import com.example.passiomodulenew.Passio.ReferenceNutrients
import com.example.passiomodulenew.Passio.ServingSizeXX
import com.example.passiomodulenew.Passio.ServingUnitXX

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