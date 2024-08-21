package com.hotworx.models.HotsquadList.Session

data class sendSquadSessionMemberRequest(
    val squad_id: String,
    val squad_event_name: String,
    val lead_session_id: String,
    val member_id_list: List<String>
)
