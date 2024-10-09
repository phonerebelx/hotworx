package com.example.passiomodulenew

import com.example.passiomodulenew.data.PassioConnector
import android.content.Context
import android.content.Intent
import com.example.passiomodulenew.ui.activity.PassioUiModuleActivity

object NutritionUIModule {

    private var connector: PassioConnector? = null

    fun launch(context: Context, connector: PassioConnector? = null) {
        NutritionUIModule.connector = connector
        context.startActivity(Intent(context, PassioUiModuleActivity::class.java))
    }

    internal fun getConnector(): PassioConnector? = connector
}