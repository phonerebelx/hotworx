package com.hotworx.models.HotsquadList.Session

data class SessionMemberResponse(
    val status: Boolean,
    val message: String,
    val data: SquadData
){
    data class SquadData(
        val lead_session_id: Int,
        val squad_id: String,
        val squad_name: String,
        val squad_desc: String,
        val squad_event_name: String,
        val total_members: Int,
        val members: List<Member>
    ) {
        data class Member(
            val member_id: String? = null,
            val profile_image_url: String,
            val name: String,
            val email: String,
            val phone: String,
            val invite_message: String?,
            val has_owner: Boolean,
            var selected: Boolean
        )


    }
}