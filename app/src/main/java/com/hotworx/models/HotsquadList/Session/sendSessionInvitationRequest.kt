package com.hotworx.models.HotsquadList.Session

data class sendSessionInvitationRequest(
    val squad_id: String,
    val squad_event_id: String,
    val invitation_id: String,
    val is_accepted: Boolean
)
