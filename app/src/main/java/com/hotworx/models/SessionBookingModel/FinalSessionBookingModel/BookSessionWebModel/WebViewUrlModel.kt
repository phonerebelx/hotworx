package com.hotworx.models.SessionBookingModel.FinalSessionBookingModel.BookSessionWebModel

data class WebViewUrlModel(
    val payment_status: Boolean?,
    val add_card_url: String?,
    val text: String?,
    val card_number: String?,
    val message_popup: Boolean?,
    val description: String?,
    val success: String?,
)