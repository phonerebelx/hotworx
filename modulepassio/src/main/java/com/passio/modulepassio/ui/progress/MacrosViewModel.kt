package com.passio.modulepassio.ui.progress

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.passio.modulepassio.domain.diary.DiaryUseCase
import com.passio.modulepassio.ui.base.BaseViewModel
import com.passio.modulepassio.ui.model.FoodRecord
import com.passio.modulepassio.ui.util.SingleLiveEvent
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import java.util.Date

class MacrosViewModel : BaseViewModel() {

    private val useCase = DiaryUseCase

    private val _logsLD = SingleLiveEvent<Pair<TimePeriod, List<FoodRecord>>>()
    val logsLD: LiveData<Pair<TimePeriod, List<FoodRecord>>> get() = _logsLD

    private var currentDate = Date()

    private val _timePeriod = SingleLiveEvent<TimePeriod>()
    val timePeriod: LiveData<TimePeriod> get() = _timePeriod

    fun fetchLogsForCurrentWeek() {
        viewModelScope.launch {
            _timePeriod.postValue(TimePeriod.WEEK)
            val records = useCase.getLogsForWeek(currentDate)
            _logsLD.postValue(Pair(TimePeriod.WEEK, records))
        }
    }

    fun fetchLogsForCurrentMonth() {
        viewModelScope.launch {
            _timePeriod.postValue(TimePeriod.MONTH)
            val records = useCase.getLogsForMonth(currentDate)
            _logsLD.postValue(Pair(TimePeriod.MONTH, records))
        }
    }

    fun getCurrentDate() = currentDate

    fun setPrevious() {
        if (_timePeriod.value == TimePeriod.WEEK) {
            val dateTime = DateTime(currentDate)
            currentDate = dateTime.minusDays(7).toDate()
            fetchLogsForCurrentWeek()
        } else if (_timePeriod.value == TimePeriod.MONTH) {
            val dateTime = DateTime(currentDate)
            currentDate = dateTime.minusMonths(1).toDate()
            fetchLogsForCurrentMonth()
        }
    }

    fun setNext() {
        if (_timePeriod.value == TimePeriod.WEEK) {
            val dateTime = DateTime(currentDate)
            currentDate = dateTime.plusDays(7).toDate()
            fetchLogsForCurrentWeek()
        } else if (_timePeriod.value == TimePeriod.MONTH) {
            val dateTime = DateTime(currentDate)
            currentDate = dateTime.plusMonths(1).toDate()
            fetchLogsForCurrentMonth()
        }
    }

    fun setDate(date: Date) {
        currentDate = date
        fetchLogsForCurrentWeek()
    }

    fun deleteLog(foodRecord: FoodRecord) {
        viewModelScope.launch {
            useCase.deleteRecord(foodRecord)
            fetchLogsForCurrentWeek()
        }
    }

}