package com.hotworx.models.SessionBookingModel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FrequentlyLocation(
    val is_allow: String, // yes
    val location_code: String,
    val location_id: String, // 0
    val location_name: String // Demo Location USA
):Parcelable