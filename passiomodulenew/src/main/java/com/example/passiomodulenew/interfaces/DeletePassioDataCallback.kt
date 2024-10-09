package com.example.passiomodulenew.interfaces

import com.example.passiomodulenew.ui.model.FoodRecord
import com.example.passiomodulenew.Passio.DeleteMealData


interface DeletePassioDataCallback {
    fun  onDeletePassioData(uuid:String,food_entry_date:String,records: FoodRecord)
    fun deleteDataSuccess(deleteResponse: DeleteMealData)
    fun deleteDataError(error: String)
}
