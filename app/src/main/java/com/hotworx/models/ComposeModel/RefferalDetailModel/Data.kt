package com.hotworx.models.ComposeModel.RefferalDetailModel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Data(
    val buy_url: String,
    val currency_symbol: String,
    val lead_id: String,
    val location_code: String,
    val location_name: String,
    val remaining_balance: Int,
    val total_amount_used: Int,
    val total_gift_amount: Int,
    val trail_url: String,
    val redeemed_text: String?,
    val location_address: String = "",
): Parcelable
