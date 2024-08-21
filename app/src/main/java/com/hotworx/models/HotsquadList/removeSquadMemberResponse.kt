package com.hotworx.models.HotsquadList

data class RemoveMemberResponse(
    val status: Boolean,
    val message: String,
    val data: List<Any>,
    var selected: Boolean = false
)