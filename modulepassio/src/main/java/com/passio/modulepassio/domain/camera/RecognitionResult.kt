package com.passio.modulepassio.domain.camera

import com.passio.modulepassio.ui.model.FoodRecord
import ai.passio.passiosdk.passiofood.DetectedCandidate
import ai.passio.passiosdk.passiofood.nutritionfacts.PassioNutritionFacts

sealed class RecognitionResult {
    data class VisualRecognition(val visualCandidate: DetectedCandidate) : RecognitionResult()
    data class FoodRecordRecognition(val foodItem: FoodRecord) : RecognitionResult()
    data class NutritionFactRecognition(val nutritionFactsPair: Pair<PassioNutritionFacts?, String>) : RecognitionResult()
    object NoProductRecognition : RecognitionResult()
    object NoRecognition : RecognitionResult()
}