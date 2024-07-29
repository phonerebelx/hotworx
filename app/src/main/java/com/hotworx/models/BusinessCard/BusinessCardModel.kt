package com.hotworx.models.BusinessCard

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


data class BusinessCardModel(
    val address: String?,
    val business_email: String?,
    val buy_text: String?,
    val card_title: String?,
    val `data`: ArrayList<Data>?,
    val facebook_profile_link: String?,
    val insta_profile_link: String?,
    val name_on_businesscard: String?,
    val phone_number: String?,
    val trail_text: String?,

)