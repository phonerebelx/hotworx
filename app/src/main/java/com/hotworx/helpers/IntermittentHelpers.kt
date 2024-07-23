package com.hotworx.helpers

import android.util.Log
import com.hotworx.requestEntity.IntermittentPlanResponse
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object IntermittentHelpers {
    var settingData: IntermittentPlanResponse.Setting_data? = null

    fun getIntermittentPlanData(): Boolean {

        settingData?.let { data ->

            if (data.intermittent_status) {
                // Check for current day
                val calendar = Calendar.getInstance()
                val date = calendar.time
                val currentDay = SimpleDateFormat("EE", Locale.ENGLISH).format(date.time)

                data.plan_data.first { it.plan_day.equals(currentDay, ignoreCase = true) }?.let { planData ->
                    if (planData.active) {
                        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(date.time)
                        val startDateTime = currentDate + " " + planData.start_time
                        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                        Log.d( "currentDate: ",currentDate.toString())
                        Log.d( "startDateTime: ",startDateTime.toString())
                        Log.d( "formatter: ",formatter.toString())
                        formatter.timeZone = TimeZone.getDefault()
                        formatter.parse(startDateTime)?.let { startDate ->
                            calendar.time = startDate
                            calendar.add(Calendar.HOUR, planData.intermittent_hrs)
                            val endDate = calendar.time
                            if (Date() >= startDate && Date() < endDate) {
                                return true
                            }
                        }
                    }
                }
            }
        }

        return false
    }
}