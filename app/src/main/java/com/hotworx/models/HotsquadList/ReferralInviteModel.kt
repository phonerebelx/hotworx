package com.hotworx.models.HotsquadList

data class ReferralInviteModel(
    val status: Boolean,
    val message: String,
    val data: List<Any> // You can change `Any` to a specific type if you know the type of data expected in the list.
)