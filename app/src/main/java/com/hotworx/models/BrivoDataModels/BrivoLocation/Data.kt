package com.hotworx.models.BrivoDataModels.BrivoLocation

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue


@Parcelize
data class Data(
    val accountId: Int,
    val accountName: String,
    val bleAuthTimeFrame: Int,
    val bleCredential: String,
    val enablePassTransfer: Boolean,
    val enabled: String,
    val firstName: String,
    val lastName: String,
    val pass: String,
    val sites: @RawValue List<Site> = listOf(),
    val site: @RawValue Site,
    val userImage: @RawValue UserImage
): Parcelable