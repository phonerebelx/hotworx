package com.example.passiomodulenew.interfaces

import com.example.passiomodulenew.Passio.GetPassioResponse.GetFoodRecord
import java.util.Date

interface PassioDataCallback {
    fun  onFetchPassioData(day: Date)
    fun onPassioDataSuccess(passioList: GetFoodRecord)
    fun onPassioDataError(error: String)
}
