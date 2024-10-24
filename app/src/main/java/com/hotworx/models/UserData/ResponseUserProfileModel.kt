package com.hotworx.models.UserData

import com.example.passionewsdk.ui.model.UserProfile

data class ResponseUserProfileModel(
    val `data`: List<DataXXX>,
    val msg: String ,// success
    val user_profile: UserProfileHotworx // success
)
data class UserProfileHotworx(
    val login_id: String,
    val first_name: String,
    val last_name: String,
    val password: String,
    val country: String,
    val email: String,
    val phone: String,
    val location_id: String,
    val email_address: String,
    val phone_number: String,
    val user_type: String,
    val creation_date: String,
    val img_src: String,
    val gender: String,
    val dob: String,
    val height: String,
    val height_in_cm: String,
    val weight: String,
    val weight_in_kg: String,
    val is_reminder: String,
    val is_vibration: String,
    val access_camera: String,
    val apple_push_id: String,
    val android_push_id: String,
    val address: String,
    val full_name: String,
    val access_token_fitbit: String,
    val db_type: String,
    val rating_status: String,
    val intermittent_status: String,
    val intermittent_hr: String,
    val image_url: String,
    val by_pass_otp: String,
    val syn_attribute: String,
    val message: String,
    val height_in_meter: Any?, // nullable
    val age: Any?, // nullable
    val franchise_count: String,
    val is_admin: String,
    val is_active: String,
    val status: Boolean,
    val showpvt: String,
    val showdiettrax: String,
    val show_myaccount: String,
    val name: String,
    val myaccount_url: String,
    val is_brivo_allowed: String,
    val is_ambassador_allowed: String,
    val is_employee_allowed: String,
    val unread_notifications: String,
    val new_reciprocal_enabled: String,
    val hotsquad_pending_invites: Int,
    val is_hotsquad_enabled: String,
    val is_passio_enabled: String,
    val nutritional_goals: NutritionalGoals
)

data class NutritionalGoals(
    val weekly_session_goal: Int,
    val target_calories: Int,
    val recommended_calories: Int,
    val activity_level: String,
    val target_weight: Double,
    val goal_water: Double,
    val goal_weight_timeline: String,
    val calorie_deficit: String,
    val diet: String,
    val nutrition_percentage: NutritionPercentage
)

data class NutritionPercentage(
    val carbs: Int,
    val protein: Int,
    val fat: Int
)