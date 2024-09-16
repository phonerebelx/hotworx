package com.hotworx.models.NotificationHistory


data class Data(
    val id: String?,
    val attachment_url: String?,
    val navigation_type: String?,
    val body: String,
    val image_url: String?,
    val banner: String?,
    val sent_at: String,
    val title: String,
    var read_status: Boolean,
    var attachment_downloaded: Boolean
)