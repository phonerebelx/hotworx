package com.hotworx.models.HotsquadList.Session

import com.google.gson.annotations.SerializedName

data class SquadSessionInvitationResponse(
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: InvitationData
)

data class InvitationData(
    @SerializedName("sent_invite") val sentInvite: Int,
    @SerializedName("failed_invite") val failedInvite: Int,
    @SerializedName("failed_squad_session_invites") val failedSquadSessionInvites: List<Any> // Use `Any` if the items in the list can vary, or specify a more concrete type if possible
)
