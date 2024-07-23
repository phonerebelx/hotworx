package com.hotworx.models.BrivoDataModels.BrivoCredentialDataModel

data class Data(
    val credentials: String,
    val status: Boolean,
    val access_token: String,
    val refresh_token: String
)