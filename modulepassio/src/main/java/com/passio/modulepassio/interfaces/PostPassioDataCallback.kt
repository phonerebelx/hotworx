package com.passio.modulepassio.interfaces

import com.passio.modulepassio.models.HotsquadList.Passio.GetPassioResponse
import com.passio.modulepassio.models.HotsquadList.Passio.PostPassioResponse
import com.passio.modulepassio.ui.model.FoodRecord
import java.util.Date

interface PostPassioDataCallback {
    fun onPostPassioData(date:String,url:String,records: List<FoodRecord>)
    fun onPassioDataSuccess(passioPost: PostPassioResponse)
    fun Error(error: String)
}
