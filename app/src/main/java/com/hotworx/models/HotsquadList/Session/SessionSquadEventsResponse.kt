import com.google.gson.annotations.SerializedName

data class SessionSquadEventsResponse(
    val status: Boolean,
    val message: String,
    val data: SquadEventsData
){
    data class SquadEventsData(
        @SerializedName("squad_events") val squadEvents: MutableList<SquadEvent>,
        @SerializedName("members") val members: MutableList<Member>,
    )

    data class SquadEvent(
        val id: String,
        val name: String,
        val highlights: Highlights,
        val participants: List<Participant>
    ){
        data class Participant(
            @SerializedName("profile_image") val profileImage: String,
            val name: String
        )
    }

    data class Highlights(
        val title: String,
        @SerializedName("session_date") val sessionDate: String,
        @SerializedName("avg_exercise_time") val avgExerciseTime: String,
        @SerializedName("total_burned_cal") val totalBurnedCal: String
    )

    data class Member(
        val id: String,
        @SerializedName("profile_image") val profileImage: String,
        val name: String,
        val email: String,
        val phone: String,
        @SerializedName("burned_cal") val burnedCal: String
    )
}
