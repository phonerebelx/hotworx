package com.hotworx.models.BusinessCard

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UrlModel(
    val type: String?,
    val url: String?,
    val description: String?,
): Parcelable
