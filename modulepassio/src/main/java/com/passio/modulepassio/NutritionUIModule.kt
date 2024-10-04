package com.passio.modulepassio

import android.content.Context
import android.content.Intent
import com.passio.modulepassio.data.PassioConnector
import com.passio.modulepassio.ui.activity.PassioUiModuleActivity

object NutritionUIModule {

    private var connector: PassioConnector? = null

    fun launch(context: Context, connector: PassioConnector? = null) {
        this.connector = connector
        context.startActivity(Intent(context, PassioUiModuleActivity::class.java))
    }

    internal fun getConnector(): PassioConnector? = connector
}