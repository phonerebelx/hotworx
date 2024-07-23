package com.hotworx.models.NotificationHistory.NotificationRead

data class Data(
    val attachment_downloaded: Boolean,
    val attachment_url: Any,
    val body: String,
    val id: String,
    val image_url: Any,
    val read_status: Boolean,
    val sent_at: String,
    val title: String
)