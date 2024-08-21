package com.hotworx.models.HotsquadList

data class Hotsquad(
    val squad_id: String,
    val name: String,
    val icon_url: String,
    val desc: String,
    val total_members: Int,
    val has_squad_access: Boolean
)