package com.hotworx.models.BrivoDataModels.BrivoLocation

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class BluetoothReader(
    val protocolVersion: Int,
    val readerUid: String,
    val securityScheme: String
) : Parcelable