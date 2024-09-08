package com.hotworx.models.HotsquadList.Session

data class UserActivitiesResponse(
    val status: Boolean,
    val message: String,
    val data: UserData
) {
    data class UserData(
        val profile_info: ProfileInfo,
        val highlights: MutableList<Highlight>,
        val activities: MutableList<Activity>,
        val no_of_pages: Int
    ) {
        data class ProfileInfo(
            val image: String,
            val name: String,
            val email: String,
            val phone: String
        )
        data class Highlight(
            val title: String,
            val image: String,
            val session_date: String,
            val exercise_time: String,
            val burned_cal: String
        )
        data class Activity(
            val activity_id: String,
            val display_date: String,
            val workout_date: String,
            val start_date_time: String,
            val end_date_time: String,
            val workout_type: String,
            val total_burnt: String,
            val location_name: String,
            val sauna_no: String
        )
    }
}
