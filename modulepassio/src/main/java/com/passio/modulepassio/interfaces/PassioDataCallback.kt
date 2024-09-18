package com.passio.modulepassio.interfaces

import com.passio.modulepassio.models.HotsquadList.Passio.GetPassioResponse
import java.util.Date

interface PassioDataCallback {
    fun onFetchPassioData(day: Date)
    fun onPassioDataSuccess(passioList: GetPassioResponse)
    fun onPassioDataError(error: String)
}
