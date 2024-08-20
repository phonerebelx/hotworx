package com.hotworx.models.HotsquadList

data class pendingListAcceptRejectRequest(
    val squad_id: String,
    val invitation_id: String,
    val is_accepted: Boolean,
)