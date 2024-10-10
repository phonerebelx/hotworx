package com.hotworx.models.UserData

import java.io.Serializable

data class DataX(
    val access_camera: String,
    val access_token_fitbit: String,
    val address: String,
    val android_push_id: String,
    val apple_push_id: String,
    val country: String,
    val creation_date: String, // 2023-11-19 10:51:04
    val db_type: String, // hotworx
    val dob: String,
    val email: String, // employee@emloyee.com
    val email_address: String,
    val first_name: String, // Employeeeee
    val franchise_count : String, // 0
    val full_name: String, // Employeeeee _
    val gender: String,
    val height: String,
    val age: String,
    val height_in_cm: String,
    val image_url: String,
    val img_src: String,
    val intermittent_hr: String, // 0
    val intermittent_status: String, // true
    val is_active: String, // yes
    val is_admin: String, // no
    val is_reminder: String,
    val is_vibration: String,
    val last_name: String, // _
    val location_id: String, // 0
    val login_id: String,
    val message: String, // login successfully
    val myaccount_url: String, // https://hotworx.net
    val name: String, // Employeeeee _
    val password: String, // test1234
    val phone: String, // 5556667777
    val phone_number: String,
    val rating_status: String, // 0
    val show_myaccount: String, // no
    val showdiettrax: String, // yes
    val showpvt: String, // yes
    val status: Boolean, // true
    val user_type: String,
    val weight: String,
    val weight_in_kg: String,
    val getAppointmentLink: String,
    val is_brivo_allowed: String,
    val is_ambassador_allowed: String?,
    val is_employee_allowed: String?,
    val unread_notifications: String?,
    val new_reciprocal_enabled: String?,
    val hotsquad_pending_invites: String?,
    val is_hotsquad_enabled: String?,
    val is_passio_enabled: String?,
//    private NutritionalGoals nutritionalGoals;

): Serializable