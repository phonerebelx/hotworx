package com.example.passiomodulenew.interfaces

import com.example.passiomodulenew.Passio.GetPassioResponse
import java.util.Date

interface PassioDataCallback {
    fun  onFetchPassioData(day: Date)
    fun onPassioDataSuccess(passioList: GetPassioResponse)
    fun onPassioDataError(error: String)
}
