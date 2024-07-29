package com.hotworx.models.BusinessCard

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UtmModel(
    val name: String?,
    val url: String?,
): Parcelable
