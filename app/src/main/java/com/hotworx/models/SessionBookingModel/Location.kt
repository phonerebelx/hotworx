package com.hotworx.models.SessionBookingModel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
@Parcelize
data class Location(
    val is_allow: String,
    val location_code: String,
    val location_id: String,
    val location_name: String,
    val location_tier: String,
    val reciprocal_fees: String,
    val currency_symbol: String,
    val description: String,
    var type: String?,

    ): Parcelable