package com.example.passiomodulenew.ui.model

import com.example.passiomodulenew.ui.activity.UserCache
import com.example.passiomodulenew.ui.profile.ActivityLevel
import com.example.passiomodulenew.ui.profile.CalorieDeficit
import com.example.passiomodulenew.ui.profile.Gender
import com.example.passiomodulenew.ui.profile.LengthUnit
import com.example.passiomodulenew.ui.profile.WaterUnit
import com.example.passiomodulenew.ui.profile.WeightUnit
import com.example.passiomodulenew.ui.profile.kgToLbs
import com.example.passiomodulenew.ui.profile.metersToFeetInches
import com.example.passiomodulenew.ui.profile.metersToMetersCentimeters
import com.example.passiomodulenew.ui.profile.mlToOz
import ai.passio.passiosdk.passiofood.data.model.PassioMealPlan
import kotlin.math.pow

data class UserProfile(
    var userName: String = "",
    var age: Int = 0,
    var gender: Gender = Gender.Male,
    var height: Double = 0.0, //meter
    var weight: Double = 0.0, //kg
    var targetWeight: Double = 0.0, //kg
    var activityLevel: ActivityLevel = ActivityLevel.NotActive,
    var calorieDeficit: CalorieDeficit = CalorieDeficit.Maintain,
    var passioMealPlan: PassioMealPlan? = null,
    var waterTarget: Double = 0.0, //ml
    var carbsPer: Int = 50, //percentage
    var proteinPer: Int = 25, //percentage
    var fatPer: Int = 25, //percentage
    var caloriesTarget: Int = 2100,
    val measurementUnit: MeasurementUnit = MeasurementUnit(),
    val userReminder: UserReminder = UserReminder()
) {

    // Calculate BMI value
    fun calculateBMI(): Float {

        if (height <= 0 || weight <= 0) {
            return 0.0f // or handle this case according to your app's logic
        }

        val bmi = (weight / height.pow(2))
        return bmi.toFloat()
    }

    fun getDisplayAge(): String {
        if (age <= 0)
            return ""
        return age.toString()
    }

    fun getDisplayHeight(): String {
        val displayText: String
        if (measurementUnit.lengthUnit == LengthUnit.Imperial) {
            val pair = metersToFeetInches(height)
            displayText = "" + pair.first + "'" + pair.second + "\""
        } else {
            val pair = metersToMetersCentimeters(height)
            displayText = "" + pair.first + "m " + pair.second + "cm"
        }
        return displayText
    }

    fun getDisplayWeight(): String {
        if (weight <= 0)
            return ""
        val displayText: String = if (measurementUnit.weightUnit == WeightUnit.Metric) {
            weight.toString()
        } else {
            kgToLbs(weight).toString()
        }
        return displayText
    }

    fun getTargetWightInCurrentUnit(): Double {
        if (targetWeight <= 0)
            return 0.0
        return if (UserCache.getProfile().measurementUnit.weightUnit == WeightUnit.Metric) {
            targetWeight
        } else {
            kgToLbs(targetWeight)
        }
    }

    fun getDisplayTargetWeight(): String {
        return if (targetWeight <= 0) {
            ""
        } else {
            getTargetWightInCurrentUnit().toString()
        }
    }


    fun getTargetWaterInCurrentUnit(): Double {
        if (waterTarget <= 0)
            return 0.0
        return if (UserCache.getProfile().measurementUnit.waterUnit == WaterUnit.Metric) {
            waterTarget
        } else {
            mlToOz(waterTarget)
        }
    }

    fun getDisplayTargetWater(): String {
        return if (waterTarget <= 0) {
            ""
        } else {
            getTargetWaterInCurrentUnit().toString()
        }
    }

    fun getCarbsGrams(): Float = (carbsPer * caloriesTarget) / 400f

    fun getProteinGrams(): Float = (proteinPer * caloriesTarget) / 400f

    fun getFatGrams(): Float = (fatPer * caloriesTarget) / 900f
}