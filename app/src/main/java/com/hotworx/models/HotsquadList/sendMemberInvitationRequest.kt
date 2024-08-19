package com.hotworx.models.HotsquadList

data class sendMemberInvitationRequest(
    val squad_id: String,
    val squad_invite_list: List<String>
)