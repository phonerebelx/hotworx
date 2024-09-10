package com.passio.modulepassio.ui.model

import com.passio.modulepassio.ui.profile.LengthUnit
import com.passio.modulepassio.ui.profile.WaterUnit
import com.passio.modulepassio.ui.profile.WeightUnit

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