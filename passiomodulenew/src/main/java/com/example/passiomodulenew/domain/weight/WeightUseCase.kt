package com.example.passiomodulenew.domain.weight

import com.example.passiomodulenew.data.Repository
import com.example.passiomodulenew.ui.model.WeightRecord
import com.example.passiomodulenew.ui.progress.TimePeriod
import java.util.Date

object WeightUseCase {

    private val repository = Repository.getInstance()

    suspend fun updateWeightRecord(weightRecord: WeightRecord): Boolean {
        return repository.updateWeight(weightRecord)
    }

    suspend fun removeWeightRecord(weightRecord: WeightRecord): Boolean {
        return repository.removeWeightRecord(weightRecord)
    }

    suspend fun getWeightRecords(currentDate: Date, timePeriod: TimePeriod): List<WeightRecord> {
        return repository.fetchWeightRecords(currentDate, timePeriod)
    }

    suspend fun getLatest(): WeightRecord? {
        return repository.fetchLatestWeightRecord()
    }
}