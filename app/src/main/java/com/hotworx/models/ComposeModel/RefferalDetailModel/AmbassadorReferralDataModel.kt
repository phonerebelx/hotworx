package com.hotworx.models.ComposeModel.RefferalDetailModel

data class AmbassadorReferralDataModel(
    val buy_text: String,
    val `data`: ArrayList<Data>,
    val premium_text: String,
    val trail_text: String,
    val redeemed_info_text: String?,
    val currency_Symbol: String,
    val all_locations_total_amount: Int,
    val all_locations_used_amount: Int,
    val all_locations_remaining_amount: Int,
)