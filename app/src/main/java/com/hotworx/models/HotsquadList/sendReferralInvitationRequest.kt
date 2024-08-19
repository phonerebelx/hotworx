package com.hotworx.models.HotsquadList

data class sendReferralInvitationRequest(
    val squad_id: String,
    val squad_referral_invite_list: List<String>
)