package com.hotworx.ui.passioactivity

import ai.passio.passiosdk.core.config.PassioConfiguration
import ai.passio.passiosdk.core.config.PassioMode
import ai.passio.passiosdk.passiofood.PassioSDK
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.hotworx.R
import com.passio.modulepassio.NutritionUIModule

class PassioMainActivity : ComponentActivity() {
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_passio_main)
        textView = findViewById(R.id.text1)

        val passioConfiguration = PassioConfiguration(
            this.applicationContext,
            "pHAFLe95rgGnHjSZwkZIBrY0pyqXchBZymTtp4gX4hoZ"
        ).apply {
            sdkDownloadsModels = true
            debugMode = -333
        }

        PassioSDK.instance.configure(passioConfiguration) { passioStatus ->
            Log.d("HHHH", passioStatus.toString())
            when (passioStatus.mode) {
                PassioMode.NOT_READY -> onSDKError("Not ready")
                PassioMode.FAILED_TO_CONFIGURE -> onSDKError(getString(passioStatus.error!!.errorRes))
                PassioMode.IS_READY_FOR_DETECTION -> onSDKReady()
                PassioMode.IS_BEING_CONFIGURED -> {
                }
                PassioMode.IS_DOWNLOADING_MODELS -> {}
            }
        }
    }

    private fun onSDKError(error: String) {
        textView.text = "ERROR: $error"
    }

    private fun onSDKReady() {
        NutritionUIModule.launch(this)
        Log.d("ytytytuy","jhkjhkhlkhlkllhlk")
        finish()
    }
}