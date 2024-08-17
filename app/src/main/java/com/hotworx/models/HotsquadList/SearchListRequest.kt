package com.hotworx.models.HotsquadList

data class SearchListRequest(
    val squad_id: String,
    val search_list: List<String>
)