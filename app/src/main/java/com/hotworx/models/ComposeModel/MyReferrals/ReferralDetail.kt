package com.hotworx.models.ComposeModel.MyReferrals

data class ReferralDetail(
    val client_type: String,
    val lead_id: String,
    val member_status: String,
    val membership_type: String,
    val profile_pic_url: String,
    val purchase_date: String,
    val referral_name: String,
    val status: String
)