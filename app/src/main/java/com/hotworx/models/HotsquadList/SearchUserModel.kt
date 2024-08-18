package com.hotworx.models.HotsquadList

import com.google.gson.annotations.SerializedName

data class SearchUserModel(
    val status: Boolean,
    val message: String,
    val data: SearchUserData?
)

data class SearchUserData(
    @SerializedName("found_user") val foundUser: List<FoundUser>?,  // Match JSON field name
    @SerializedName("not_found_user") val notFoundUser: List<NotFoundUser>?,
    @SerializedName("referral_url") val referralUrl: String?
)

data class FoundUser(
    @SerializedName("squad_invite_id") val squadInviteId: String,
    @SerializedName("squad_invite_status") val squadInviteStatus: String?,
    val name: String,
    val email: String,
    val phone: String,
    val profile_image_url: String,
    var selected: Boolean = false
)

data class NotFoundUser(
    @SerializedName("referral_inviteId") val referralInviteId: String,
    @SerializedName("search_by") val searchBy: String,
    @SerializedName("record_status") val recordStatus: String
)