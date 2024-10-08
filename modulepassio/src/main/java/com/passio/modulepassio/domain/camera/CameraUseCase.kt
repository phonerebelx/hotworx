package com.passio.modulepassio.domain.camera

import com.passio.modulepassio.domain.camera.RecognitionResult
import com.passio.modulepassio.data.Repository
import com.passio.modulepassio.ui.model.FoodRecord
import com.passio.modulepassio.ui.model.MealLabel
import ai.passio.passiosdk.passiofood.FoodCandidates
import ai.passio.passiosdk.passiofood.FoodDetectionConfiguration
import ai.passio.passiosdk.passiofood.PassioID
import ai.passio.passiosdk.passiofood.data.model.PassioFoodItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date

object CameraUseCase {

    private val repository = Repository.getInstance()

    fun recognitionFlow(config: FoodDetectionConfiguration): Flow<RecognitionResult> {
        return repository.recognitionResultFlow(config).map { mapRecognitionResult(it) }
    }

    fun nutritionFactsFlow(): Flow<RecognitionResult> {
        return repository.nutritionFactsResultFlow()
            .map {
                if (it.first == null) {
                    RecognitionResult.NoRecognition
                } else {
                    RecognitionResult.NutritionFactRecognition(it)
                }
            }
    }

    suspend fun fetchFoodItemForPassioID(passioID: PassioID): PassioFoodItem? {
        return repository.fetchFoodItemForPassioID(passioID)
    }


    suspend fun logFoodRecord(record: FoodRecord): Boolean {
        record.create(Date().time)
        val mealLabel = MealLabel.dateToMealLabel(record.createdAtTime()!!)
        record.mealLabel = mealLabel
        return repository.logFoodRecord(record)
    }

    fun stopFoodDetection() {
        repository.stopFoodDetection()
    }

    private suspend fun mapRecognitionResult(candidates: FoodCandidates?): RecognitionResult {
        if (candidates == null) {
            return RecognitionResult.NoRecognition
        }

        val visualCandidate =
            candidates.detectedCandidates?.firstOrNull() //maxByOrNull { it.confidence }
        if (visualCandidate != null) {
            return RecognitionResult.VisualRecognition(visualCandidate)
        }

        val barcodeCandidate =
            candidates.barcodeCandidates?.firstOrNull() //maxByOrNull { it.boundingBox.width() * it.boundingBox.height() }
        if (barcodeCandidate != null) {
            val foodItem = repository.fetchFoodItemForProduct(barcodeCandidate.barcode)
                ?: return RecognitionResult.NoProductRecognition
            return RecognitionResult.FoodRecordRecognition(FoodRecord(foodItem).apply {
                this.barcode = barcodeCandidate.barcode
            })
        }

        val packagedCandidate =
            candidates.packagedFoodCandidates?.firstOrNull() //maxByOrNull { it.confidence }
        if (packagedCandidate != null) {
            val foodItem = repository.fetchFoodItemForProduct(packagedCandidate.packagedFoodCode)
                ?: return RecognitionResult.NoProductRecognition
            return RecognitionResult.FoodRecordRecognition(FoodRecord(foodItem).apply {
                this.packagedFoodCode = packagedCandidate.packagedFoodCode
            })
        }

        return RecognitionResult.NoRecognition
    }
}