package com.passio.passiomodule

import com.passio.passiomodule.data.PassioConnector
import com.passio.passiomodule.ui.activity.PassioUiModuleActivity
import android.content.Context
import android.content.Intent

object NutritionUIModule {

    private var connector: PassioConnector? = null

    fun launch(context: Context, connector: PassioConnector? = null) {
        NutritionUIModule.connector = connector
        context.startActivity(Intent(context, PassioUiModuleActivity::class.java))
    }

    internal fun getConnector(): PassioConnector? = connector
}