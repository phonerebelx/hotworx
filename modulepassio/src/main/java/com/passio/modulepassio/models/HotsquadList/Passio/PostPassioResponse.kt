package com.passio.modulepassio.models.HotsquadList.Passio

data class PostPassioResponse(
    val message: String,
    val results: List<Result>,
    val status: Boolean
)