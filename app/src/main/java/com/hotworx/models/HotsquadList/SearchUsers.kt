package com.hotworx.models.HotsquadList

data class SearchUsers(
    val foundUser: List<FoundUser>,
    val notFoundUser: List<NotFoundUser>
)