package com.example.passiomodulenew.ui.model

import com.example.passiomodulenew.ui.profile.LengthUnit
import com.example.passiomodulenew.ui.profile.WaterUnit
import com.example.passiomodulenew.ui.profile.WeightUnit

data class MeasurementUnit(
    var lengthUnit: LengthUnit = LengthUnit.Imperial,
    var weightUnit: WeightUnit = WeightUnit.Imperial,
    var waterUnit: WaterUnit = WaterUnit.Imperial){

   /* fun getLengthUnit() : LengthUnit{
        return  lengthUnit ?: LengthUnit.Imperial
    }
    fun getWeightUnit() : WeightUnit{
        return  weightUnit ?: WeightUnit.Imperial
    }
    fun getWaterUnit() : WaterUnit{
        return  waterUnit ?: WaterUnit.Imperial
    }*/
}