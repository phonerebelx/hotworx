package com.example.passiomodulenew.interfaces

import com.example.passiomodulenew.ui.model.FoodRecord
import com.example.passiomodulenew.Passio.PostPassioResponse

interface PostPassioDataCallback {
    fun onPostPassioData(date:String,url:String,records: List<FoodRecord>)
    fun onPassioDataSuccess(passioPost: PostPassioResponse)
    fun Error(error: String)
}
