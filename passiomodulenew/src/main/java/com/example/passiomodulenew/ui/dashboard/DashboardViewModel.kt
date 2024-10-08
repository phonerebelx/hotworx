package com.example.passiomodulenew.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.passiomodulenew.domain.diary.DiaryUseCase
import com.example.passiomodulenew.domain.user.UserProfileUseCase
import com.example.passiomodulenew.domain.water.WaterUseCase
import com.example.passiomodulenew.domain.weight.WeightUseCase
import com.example.passiomodulenew.ui.activity.UserCache
import com.example.passiomodulenew.ui.base.BaseViewModel
import com.example.passiomodulenew.ui.model.FoodRecord
import com.example.passiomodulenew.ui.model.UserProfile
import com.example.passiomodulenew.ui.util.SingleLiveEvent
import com.prolificinteractive.materialcalendarview.CalendarMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import java.util.Date

class DashboardViewModel : BaseViewModel() {

    private val useCase = DiaryUseCase
    private val useCaseUserProfile = UserProfileUseCase
    private val waterUseCase = WaterUseCase
    private val weightUseCase = WeightUseCase


    private var currentDate = Date()
    private val _currentDateEvent = MutableLiveData<Date>()
    val currentDateEvent: LiveData<Date> get() = _currentDateEvent

    private val _logsLD = MutableLiveData<Pair<UserProfile, List<FoodRecord>>>()
    val logsLD: LiveData<Pair<UserProfile, List<FoodRecord>>> get() = _logsLD
    private val _isLogsLoading = SingleLiveEvent<Boolean>()
    val isLogsLoading: LiveData<Boolean> get() = _isLogsLoading

    private val _adherents = MutableLiveData<List<Long>>()
    val adherents: LiveData<List<Long>> get() = _adherents
    private val _isAdherentsLoading = SingleLiveEvent<Boolean>()
    val isAdherentsLoading: LiveData<Boolean> get() = _isAdherentsLoading

    private var calendarModeCurrent = CalendarMode.WEEKS
    private val _calendarMode = MutableLiveData<CalendarMode>()
    val calendarMode: LiveData<CalendarMode> get() = _calendarMode

    private val _waterSummary = MutableLiveData<Pair<Double, Double>>()
    val waterSummary: LiveData<Pair<Double, Double>> get() = _waterSummary
    private val _isWaterLoading = SingleLiveEvent<Boolean>()
    val isWaterLoading: LiveData<Boolean> get() = _isWaterLoading

    private val _weightSummary = MutableLiveData<Pair<Double, Double>>()
    val weightSummary: LiveData<Pair<Double, Double>> get() = _weightSummary
    private val _isWeightLoading = SingleLiveEvent<Boolean>()
    val isWeightLoading: LiveData<Boolean> get() = _isWeightLoading



    init {
        _calendarMode.postValue(calendarModeCurrent)
        _currentDateEvent.postValue(currentDate)
    }

    fun toggleCalendarMode() {
        calendarModeCurrent = if (calendarModeCurrent == CalendarMode.WEEKS) {
            CalendarMode.MONTHS
        } else {
            CalendarMode.WEEKS
        }
        _calendarMode.postValue(calendarModeCurrent)
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

    fun fetchLogsForCurrentDay() {
        viewModelScope.launch {
            _isLogsLoading.postValue(true)
            val userProfile = useCaseUserProfile.getUserProfile()
            val records = useCase.getLogsForDay(currentDate)
            _logsLD.postValue(Pair(userProfile, records))
            _isLogsLoading.postValue(false)
        }
        fetchWaterSummary()
        fetchWeightSummary()
    }


    fun fetchAdherence() {
        viewModelScope.launch {
            _isAdherentsLoading.postValue(true)
            val adherents = useCase.fetchAdherence()
            _adherents.postValue(adherents)
            _isAdherentsLoading.postValue(false)
        }
    }

    private fun fetchWaterSummary() {
        viewModelScope.launch {
            _isWaterLoading.postValue(true)
            val totalWater =
                waterUseCase.getRecords(currentDate).sumOf { it.getWaterInCurrentUnit() }
            val targetWater = UserCache.getProfile().getTargetWaterInCurrentUnit()
            var remainingTarget = targetWater - totalWater
            if (remainingTarget <= 0.0) {
                remainingTarget = 0.0
            }
            _waterSummary.postValue(Pair(totalWater, remainingTarget))
            _isWaterLoading.postValue(false)
        }
    }

    private fun fetchWeightSummary() {
        viewModelScope.launch {
            _isWeightLoading.postValue(true)
            val totalWater =
                weightUseCase.getLatest()?.getWightInCurrentUnit() ?: 0.0
            val targetWater = UserCache.getProfile().getTargetWightInCurrentUnit()
            var remainingTarget = targetWater - totalWater
            if (remainingTarget <= 0.0) {
                remainingTarget = 0.0
            }
            _weightSummary.postValue(Pair(totalWater, remainingTarget))
            _isWeightLoading.postValue(false)
        }
    }

    fun navigateToProgress() {
        viewModelScope.launch(Dispatchers.Main) {
            navigate(DashboardFragmentDirections.dashboardToProgress(currentDate = currentDate.time))
        }
    }

    fun navigateToWeightTracking() {
        viewModelScope.launch(Dispatchers.Main) {
            navigate(DashboardFragmentDirections.dashboardToWeightTracking(currentDate = currentDate.time))
        }
    }

    fun navigateToWaterTracking() {
        viewModelScope.launch(Dispatchers.Main) {
            navigate(DashboardFragmentDirections.dashboardToWaterTracking(currentDate = currentDate.time))
        }
    }

}