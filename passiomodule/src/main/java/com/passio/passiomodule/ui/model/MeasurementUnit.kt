package com.passio.passiomodule.ui.model

import com.passio.passiomodule.ui.profile.LengthUnit
import com.passio.passiomodule.ui.profile.WaterUnit
import com.passio.passiomodule.ui.profile.WeightUnit

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