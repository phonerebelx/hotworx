package com.example.passiomodulenew.Passio

data class PostPassioResponse(
    val message: String,
    val results: List<Result>,
    val status: Boolean
)