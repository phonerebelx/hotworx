package com.example.passiomodulenew.ui.progress

import android.util.Log
import com.example.passiomodulenew.ui.model.MicroNutrient
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.passiomodulenew.domain.diary.DiaryUseCase
import com.example.passiomodulenew.ui.base.BaseViewModel
import com.example.passiomodulenew.ui.util.SingleLiveEvent
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MicrosViewModel : BaseViewModel() {
    private val useCase = DiaryUseCase

    private var currentDate = Date()
    private val _currentDateEvent = MutableLiveData<Date>()
    val currentDateEvent: LiveData<Date> get() = _currentDateEvent

    private val nutrientsList = arrayListOf<MicroNutrient>()
    private val _logsLD = SingleLiveEvent<ArrayList<MicroNutrient>>()
    val logsLD: LiveData<ArrayList<MicroNutrient>> get() = _logsLD

    private val _showLoading = SingleLiveEvent<Boolean>()
    val showLoading: LiveData<Boolean> = _showLoading

    private var isExpanded = false

    init {
        _currentDateEvent.postValue(currentDate)
    }

    fun fetchLogsForCurrentDay() {
        val formattedDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(currentDate)
        Log.d("PassioFragment", "Formatted date for API: $formattedDate")
        viewModelScope.launch {
            _showLoading.postValue(true)
            val records = useCase.getFoodDetails(formattedDate)
            val nutrients = MicroNutrient.nutrientsFromFoodRecords(records)
            nutrientsList.clear()
            nutrientsList.addAll(nutrients)
            _logsLD.postValue(filterDataAccordingToExpanded())
            _showLoading.postValue(false)
        }
    }

    private fun filterDataAccordingToExpanded(): ArrayList<MicroNutrient> {
        val tempNutrientsList = arrayListOf<MicroNutrient>()
        if (nutrientsList.size >= 10) {
            if (!isExpanded) {
                tempNutrientsList.addAll(nutrientsList.take(10))
                tempNutrientsList.add(MicroNutrient("Show More", 0.0, 0.0, "showmore"))
            } else {
                tempNutrientsList.addAll(nutrientsList)
                tempNutrientsList.add(MicroNutrient("Show Less", 0.0, 0.0, "showmore"))
            }
        } else {
            tempNutrientsList.addAll(nutrientsList)
        }
        return tempNutrientsList
    }

    fun setShowMore() {
        this.isExpanded = !this.isExpanded
        _logsLD.postValue(filterDataAccordingToExpanded())
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