package com.hotworx.models.HotsquadList

data class removeSquadMemberRequest(
    val squad_id: String,
    val squad_member_list: List<String>
)