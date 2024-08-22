package com.hotworx.models.AppInfo

data class AppInfoResponse(
    val status: Boolean,
    val message: String,
    val data: Data
) {
    data class Data(
        val sdkkeys: SdkKeys,
        val hotsquad: Hotsquad
    )

    data class SdkKeys(
        val passioKey: String
    )

    data class Hotsquad(
        val search_squad_member_limit: Int
    )
}