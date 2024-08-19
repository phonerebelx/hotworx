package com.hotworx.models.HotsquadList

data class SendMemberInviteModel(
    val status: Boolean,
    val message: String,
    val data: SquadInvitationData
)

data class SquadInvitationData(
    val sent_invite: Int,
    val failed_invite: Int,
    val failed_squad_invites: List<String>
)