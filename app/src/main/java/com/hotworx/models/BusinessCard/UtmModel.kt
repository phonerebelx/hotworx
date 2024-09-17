package com.hotworx.models.BusinessCard

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UtmModel(
    val name: String?,
    val id: String?,
    val url: String?,
    val buy_url: String?,
    val trail_url: String?,
    val url_list: ArrayList<UrlModel>
): Parcelable
