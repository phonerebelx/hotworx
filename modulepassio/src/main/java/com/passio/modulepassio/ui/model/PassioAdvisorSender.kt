package com.passio.modulepassio.ui.model

import android.graphics.Bitmap

class PassioAdvisorSender {
    var textMessage: String = ""
    val bitmaps : MutableList<Bitmap> = mutableListOf()
    var createdAt: Long = 0
}