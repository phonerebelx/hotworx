package com.hotworx.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hotworx.models.ComposeModel.RefferalDetailModel.AmbassadorReferralDataModel
import com.hotworx.models.ComposeModel.RefferalDetailModel.Data

class ReferralDetailViewModel : ViewModel() {
    private val _referralDetails = MutableLiveData<AmbassadorReferralDataModel>()
    val referralDetails: LiveData<AmbassadorReferralDataModel> get() = _referralDetails

    private val _selectedLocation = MutableLiveData<Data>()

    val selectedLocation: LiveData<Data> get() = _selectedLocation

    fun setReferralDetails(details: AmbassadorReferralDataModel) {
        _referralDetails.value = details
    }

    fun setSelectedLocation(location: Data) {
        _selectedLocation.postValue(location)

    }
}
