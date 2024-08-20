package com.hotworx.models.HotsquadList

data class PendingInvitationResponse(
    val status: Boolean,
    val message: String,
    val data: List<SquadData> = emptyList()
) {
    data class SquadData(
        var squad_id: String? = null,
        var name: String? = null,
        var desc: String? = null,
        var invitation_id: String? = null,
        var request_from: RequestFrom? = null
    ) {
        data class RequestFrom(
            var name: String? = null,
            var profile_image_url: String? = null,
            var email: String? = null,
            var phone: String? = null,
            var sent_at: String? = null
        )
    }
}
