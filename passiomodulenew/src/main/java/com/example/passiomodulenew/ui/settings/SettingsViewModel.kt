package com.example.passiomodulenew.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.passiomodulenew.data.ResultWrapper
import com.example.passiomodulenew.domain.user.UserProfileUseCase
import com.example.passiomodulenew.ui.base.BaseViewModel
import com.example.passiomodulenew.ui.model.UserProfile
import com.example.passiomodulenew.ui.profile.LengthUnit
import com.example.passiomodulenew.ui.profile.WaterUnit
import com.example.passiomodulenew.ui.profile.WeightUnit
import com.example.passiomodulenew.ui.util.SingleLiveEvent
import kotlinx.coroutines.launch

class SettingsViewModel : BaseViewModel() {

    private val useCase = UserProfileUseCase
    private lateinit var userProfile: UserProfile
    private val _userProfileEvent = SingleLiveEvent<UserProfile>()
    val userProfileEvent: LiveData<UserProfile> get() = _userProfileEvent


    private val _updateProfileResult = SingleLiveEvent<ResultWrapper<Boolean>>()
    val updateProfileResult: LiveData<ResultWrapper<Boolean>> get() = _updateProfileResult


    init {
        getMeasurementUnit()
    }

    fun getMeasurementUnit() {
        viewModelScope.launch {
            userProfile = useCase.getUserProfile()
            _userProfileEvent.postValue(userProfile)
        }
    }

    fun updateLengthUnit(lengthUnit: LengthUnit) {
        viewModelScope.launch {
            with(userProfile) {
                if (measurementUnit.lengthUnit.value != lengthUnit.value) {
                    measurementUnit.lengthUnit = lengthUnit
                    _updateProfileResult.postValue(
                        ResultWrapper.Success(
                            useCase.updateUserProfile(
                                this
                            )
                        )
                    )

                }
            }
        }
    }

    fun updateWeightUnit(weightUnit: WeightUnit) {
        viewModelScope.launch {
            with(userProfile) {
                if (measurementUnit.weightUnit.value != weightUnit.value) {
                    measurementUnit.weightUnit = weightUnit
                    if (weightUnit == WeightUnit.Metric) {
                        measurementUnit.waterUnit = WaterUnit.Metric
                    } else {
                        measurementUnit.waterUnit = WaterUnit.Imperial
                    }
                    _updateProfileResult.postValue(
                        ResultWrapper.Success(
                            useCase.updateUserProfile(
                                this
                            )
                        )
                    )
                }
            }
        }
    }

    fun updateBreakfastReminder(isReminderOn: Boolean) {
        viewModelScope.launch {
            with(userProfile) {
                userReminder.isBreakfastOn = isReminderOn
                _updateProfileResult.postValue(ResultWrapper.Success(useCase.updateUserProfile(this)))
            }
        }
    }

    fun updateLunchReminder(isReminderOn: Boolean) {
        viewModelScope.launch {
            with(userProfile) {
                userReminder.isLunchOn = isReminderOn
                _updateProfileResult.postValue(ResultWrapper.Success(useCase.updateUserProfile(this)))
            }
        }
    }

    fun updateDinnerReminder(isReminderOn: Boolean) {
        viewModelScope.launch {
            with(userProfile) {
                userReminder.isDinnerOn = isReminderOn
                _updateProfileResult.postValue(ResultWrapper.Success(useCase.updateUserProfile(this)))
            }
        }
    }
}