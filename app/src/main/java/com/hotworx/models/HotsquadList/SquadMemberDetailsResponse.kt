package com.hotworx.models.HotsquadList

data class SquadMemberDetailsResponse(
    val status: Boolean,
    val message: String,
    val data: SquadData
) {
    data class SquadData(
        val squad_id: String,
        val name: String,
        val desc: String,
        val total_members: Int,
        val members: MutableList<Member>
    ) {
        data class Member(
            val member_id: String,
            val name: String,
            val email: String,
            val phone: String,
            val profile_image_url: String,
            val invite_status: String,
            val invite_message: String?,
            val has_squad_access: Boolean,
            var selected: Boolean
        )
    }
}
