package com.hotworx.models.ComposeModel.MyReferrals

data class Data(
    val count: Int,
    val location_code: String,
    val location_name: String,
    val referral_details: List<ReferralDetail>
)

