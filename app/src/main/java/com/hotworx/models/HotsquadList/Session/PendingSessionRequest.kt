package com.hotworx.models.HotsquadList.Session

data class PendingSessionRequest(
    val status: Boolean,
    val message: String,
    val data: List<PendingSessionResponse.SquadInvitation>
)
