package com.passio.modulepassio.domain.search

import com.passio.modulepassio.data.Repository
import com.passio.modulepassio.ui.model.FoodRecord
import ai.passio.passiosdk.passiofood.PassioFoodDataInfo

object SearchUseCase {

    private val repository = Repository.getInstance()

    suspend fun fetchSearchResults(query: String): Pair<List<PassioFoodDataInfo>, List<String>> {
        return repository.fetchSearchResults(query)
    }

    suspend fun fetchFoodRecord(passioFoodDataInfo: PassioFoodDataInfo): FoodRecord? {
        repository.fetchPassioFoodItem(passioFoodDataInfo)?.let {
            return FoodRecord(it)
        }
        return null
    }
}