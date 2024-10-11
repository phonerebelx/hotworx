package com.example.passiomodulenew

import com.example.passiomodulenew.data.PassioConnector
import android.content.Context
import android.content.Intent
import com.example.passiomodulenew.ui.activity.PassioUiModuleActivity

object NutritionUIModule {

    var connector: PassioConnector? = null

    fun launch(context: Context, connector: PassioConnector? = null) {
        NutritionUIModule.connector = connector
//        context.startActivity(Intent(context, PassioUiModuleActivity::class.java))
        val intent = Intent(context, PassioUiModuleActivity::class.java).apply {
            putExtra("start_destination", "home")
        }
        context.startActivity(intent)
    }

    fun launchProfile(context: Context, connector: PassioConnector? = null) {
        NutritionUIModule.connector = connector
//        context.startActivity(Intent(context, PassioUiModuleActivity::class.java))
        val intent = Intent(context, PassioUiModuleActivity::class.java).apply {
            putExtra("start_destination", "profile")
            putExtra("hide_personal_info", true)
        }
        context.startActivity(intent)
    }

    internal fun getConnector(): PassioConnector? = connector
}