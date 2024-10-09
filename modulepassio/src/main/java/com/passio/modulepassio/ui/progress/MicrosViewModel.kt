package com.passio.modulepassio.ui.progress

import com.passio.modulepassio.ui.model.MicroNutrient
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.passio.modulepassio.domain.diary.DiaryUseCase
import com.passio.modulepassio.ui.base.BaseViewModel
import com.passio.modulepassio.ui.util.SingleLiveEvent
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import java.util.Date

class MicrosViewModel : BaseViewModel() {
    private val useCase = DiaryUseCase

    private var currentDate = Date()
    private val _currentDateEvent = MutableLiveData<Date>()
    val currentDateEvent: LiveData<Date> get() = _currentDateEvent

    private val _logsLD = SingleLiveEvent<ArrayList<MicroNutrient>>()
    val logsLD: LiveData<ArrayList<MicroNutrient>> get() = _logsLD


    private var isExpanded = false

    init {
        _currentDateEvent.postValue(currentDate)
    }

    fun fetchLogsForCurrentDay() {
        viewModelScope.launch {
            val records = useCase.getLogsForDay(currentDate)
            val nutrients = MicroNutrient.nutrientsFromFoodRecords(records)
            val nutrientsList = arrayListOf<MicroNutrient>()
            if (nutrients.size >= 10) {
                if (!isExpanded) {
                    nutrientsList.addAll(nutrients.take(10))
                    nutrientsList.add(MicroNutrient("Show More", 0.0, 0.0, "showmore"))
                } else {
                    nutrientsList.addAll(nutrients)
                    nutrientsList.add(MicroNutrient("Show Less", 0.0, 0.0, "showmore"))
                }
            }
            _logsLD.postValue(nutrientsList)
        }
    }

    fun setShowMore() {
        this.isExpanded = !this.isExpanded
        fetchLogsForCurrentDay()
    }

    fun setCurrentDate(date: Date) {
        currentDate = date
        _currentDateEvent.postValue(currentDate)
    }

    fun setNextDay() {
        currentDate = DateTime(currentDate.time).plusDays(1).toDate()
        _currentDateEvent.postValue(currentDate)
    }

    fun setPreviousDay() {
        currentDate = DateTime(currentDate.time).minusDays(1).toDate()
        _currentDateEvent.postValue(currentDate)
    }

}