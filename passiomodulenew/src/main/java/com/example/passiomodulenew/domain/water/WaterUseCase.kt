package com.example.passiomodulenew.domain.water

import com.example.passiomodulenew.data.Repository
import com.example.passiomodulenew.ui.model.WaterRecord
import com.example.passiomodulenew.ui.progress.TimePeriod
import java.util.Date

object WaterUseCase {

    private val repository = Repository.getInstance()

    suspend fun updateRecord(weightRecord: WaterRecord): Boolean {
        return repository.updateWater(weightRecord)
    }

    suspend fun removeRecord(weightRecord: WaterRecord): Boolean {
        return repository.removeWaterRecord(weightRecord)
    }

    suspend fun getRecords(currentDate: Date, timePeriod: TimePeriod): List<WaterRecord> {
        return repository.fetchWaterRecords(currentDate, timePeriod)
    }

    suspend fun getRecords(currentDate: Date): List<WaterRecord> {
        return repository.fetchWaterRecords(currentDate)
    }
}