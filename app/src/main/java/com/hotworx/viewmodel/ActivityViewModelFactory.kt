package com.hotworx.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hotworx.activities.DockActivity
import com.hotworx.retrofit.WebService

class ActivityViewModelFactory(
    private val webService: WebService,
    private val context: DockActivity,
    val showLoader: Boolean
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ActivityViewModel::class.java)) {
            return ActivityViewModel(webService, context, showLoader) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
