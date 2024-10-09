package com.example.passiomodulenew.ui.navigation

import ai.passio.passiosdk.passiofood.PassioFoodDataInfo
import com.example.passiomodulenew.ui.model.FoodRecordIngredient

interface Router {

    fun navigateUp()

    fun navigateToEdit(searchResult: PassioFoodDataInfo)

    fun editIngredient(ingredient: FoodRecordIngredient)

    fun navigateToTop3()

    fun navigateToImageLocal()

    fun navigateToBarcode()

    fun navigateToOCR()

    fun navigateToNutritionFacts()

    fun navigateToVoice()

    fun navigateToImageRemote()

    fun navigateToAdvisor()
}