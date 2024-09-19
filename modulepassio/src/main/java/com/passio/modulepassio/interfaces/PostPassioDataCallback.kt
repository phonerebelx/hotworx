package com.passio.modulepassio.interfaces

import com.passio.modulepassio.models.HotsquadList.Passio.GetPassioResponse
import com.passio.modulepassio.ui.model.FoodRecord
import java.util.Date

interface PostPassioDataCallback {
    fun onPostPassioData(records: List<FoodRecord>)
    fun onPassioDataSuccess(records: List<FoodRecord>)
    fun onPassioDataError(error: String)
}
