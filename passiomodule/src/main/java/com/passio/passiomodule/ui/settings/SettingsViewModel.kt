package com.passio.passiomodule.ui.settings

import com.passio.passiomodule.data.ResultWrapper
import com.passio.passiomodule.domain.user.UserProfileUseCase
import com.passio.passiomodule.ui.base.BaseViewModel
import com.passio.passiomodule.ui.model.UserProfile
import com.passio.passiomodule.ui.profile.LengthUnit
import com.passio.passiomodule.ui.profile.WaterUnit
import com.passio.passiomodule.ui.profile.WeightUnit
import com.passio.passiomodule.ui.util.SingleLiveEvent
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
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