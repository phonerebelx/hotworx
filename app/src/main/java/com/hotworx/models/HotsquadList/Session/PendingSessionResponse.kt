package com.hotworx.models.HotsquadList.Session

data class PendingSessionResponse(
    val status: Boolean,
    val message: String,
    val data: List<SquadInvitation>
){
    data class SquadInvitation(
        val squad_id: String,
        val name: String,
        val desc: String,
        val squad_event_id: String,
        val squad_event_name: String,
        val invitation_id: String,
        val sender_info: SenderInfo,
        val session_info: SessionInfo
    )
}

data class SenderInfo(
    val profile_image_url: String?,
    val name: String,
    val email: String,
    val phone: String,
    val sent_at: String
)

data class SessionInfo(
    val sauna_no: Int,
    val time_slot: String,
    val booking_date: String,
    val session_type: String,
    val location_id: Int,
    val location_code: String,
    val location_name: String
)
