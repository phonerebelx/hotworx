package com.passio.modulepassio.interfaces

import com.passio.modulepassio.models.HotsquadList.Passio.DeleteMealData
import com.passio.modulepassio.ui.model.FoodRecord

interface DeletePassioDataCallback {
    fun  onDeletePassioData(uuid:String,food_entry_date:String,records: FoodRecord)
    fun deleteDataSuccess(deleteResponse: DeleteMealData)
    fun deleteDataError(error: String)
}
