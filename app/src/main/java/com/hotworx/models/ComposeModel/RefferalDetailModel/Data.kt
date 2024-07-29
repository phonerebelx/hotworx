package com.hotworx.models.ComposeModel.RefferalDetailModel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Data(
    var buy_url: String,
    var currency_symbol: String,
    var lead_id: String,
    var location_code: String,
    var location_name: String,
    var remaining_balance: Int,
    var total_amount_used: Int,
    var total_gift_amount: Int,
    var trail_url: String,
    var redeemed_text: String?,
    var location_address: String = "",
): Parcelable
