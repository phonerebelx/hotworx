package com.hotworx.retrofit;


import com.hotworx.global.WebServiceConstants;
import com.hotworx.models.AddNutritionistRequestBody;
import com.hotworx.models.BrivoRequestModel.BrivoCredentialRequestModel;
import com.hotworx.models.BrivoRequestModel.SetLeadIdForBrivoToken;
import com.hotworx.models.GetRewardRequest;
import com.hotworx.models.GettingStarted.GettingStartedRequestModel;
import com.hotworx.models.HotsquadList.SearchListRequest;
import com.hotworx.models.NewActivityModels.TimelineActivityDataModel;
import com.hotworx.models.ViModel.Registration.SetRegisterLocationModel;
import com.hotworx.models.ViModel.Unregistraion.SetUnRegisterLocationModel;
import com.hotworx.requestEntity.AddExerciseDataModel;
import com.hotworx.requestEntity.DeleteCalRequestBody;
import com.hotworx.requestEntity.SetFavoriteFood;
import com.hotworx.requestEntity.SetIntermittentPlan;
import com.hotworx.requestEntity.UpdateSessionEnt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface WebService {

    //Login
    @FormUrlEncoded
    @POST("loginwithpassword")
    Call<ResponseBody> loginwithpassword(
            @Field("email_address") String email_address,
            @Field("password") String password,
            @Field("device_id") String device_id
    );


// This Api is using for Forgot Password
    @FormUrlEncoded
    @POST("login")
    Call<ResponseBody> login(
            @Field("email_address") String email_address,
            @Field("phone_number") String phone_number,
            @Field("device_id") String device_id
    );

    @FormUrlEncoded
    @POST("resetpassword")
    Call<ResponseBody> resetpassword(
            @HeaderMap Map<String, String> headers,
            @Field("newpassword") String newpassword
    );

    @FormUrlEncoded
    @POST("verifyOtp")
    Call<ResponseBody> verifyOtp(
            @HeaderMap Map<String, String> headers,
            @Field("email_address") String email_address,
            @Field("password") String password,
            @Field("phone_number") String phone_number,
            @Field("device_id") String device_id,
            @Field("otp") String otp,
            @Field("type") String type
            );

    @FormUrlEncoded
    @POST("general/update_profile")
    Call<ResponseBody> update_profile(
            @HeaderMap Map<String, String> headers,
            @Field("first_name") String email_address,
            @Field("last_name") String password,
            @Field("image_url") String image_url,
            @Field("dob") String otp,
            @Field("gender") String gender,
            @Field("height") String height,
            @Field("weight") String weight,
            @Field("address") String address
            );


    @FormUrlEncoded
    @POST("general/updateGoals")
    Call<ResponseBody> updateGoals(
            @HeaderMap Map<String, String> headers,
            @Field("current_weight") String current_weight,
            @Field("target_weight") String target_weight,
            @Field("target_weight_goal_date") String target_weight_goal_date,
            @Field("weekly_session_goal") String weekly_session_goal
    );


    @POST("general/view_profile")
    Call<ResponseBody> viewProfile(
            @HeaderMap Map<String, String> headers
    );

    @GET("general/viewGoals")
    Call<ResponseBody> viewGoals(
            @HeaderMap Map<String, String> headers
    );

    //DashBoard Api
    @FormUrlEncoded
    @POST("getDashboard")
    Call<ResponseBody> getDashboard(
            @HeaderMap Map<String, String> headers ,
            @Field("current_date") String currentDate
    );

    //Delete Session API
    @FormUrlEncoded
    @POST("booking/deleteSession")
    Call<ResponseBody> deleteSession(
            @HeaderMap Map<String, String> headers,
            @Field("booking_id") String booking_id,
            @Field("lead_record_id") String lead_record_id
    );

    //   v2 api's for booking session work


    @GET("booking/getBookingLocations_v2")
    Call<ResponseBody> getBookingLocations_v2(
            @HeaderMap Map<String, String> headers
    );


    @FormUrlEncoded
    @POST("booking/getLevelTwo_v2")
    Call<ResponseBody> getLevelTwo_v2(
            @HeaderMap Map<String, String> headers,
            @Field("selected_location_id") String selected_location_id,
            @Field("selected_date") String selected_date,
            @Field("view_type") String view_type
    );

    @FormUrlEncoded
    @POST("booking/bookSession_v2")
    Call<ResponseBody> bookSession_v2(
            @HeaderMap Map<String, String> headers,
            @Field("sauna_no") String sauna_no,
            @Field("time_slot") String time_slot,
            @Field("booking_date") String booking_date,
            @Field("session_type") String session_type,
            @Field("selected_location_id") String selected_location_id,
            @Field("message_popup") Boolean message_popup
    );

    //Session Booking

    @GET("booking/getBookingLocations")
    Call<ResponseBody> getBookingLocations(
            @HeaderMap Map<String, String> headers
    );

    @FormUrlEncoded
    @POST("booking/getLevelTwo")
    Call<ResponseBody> getLevelTwo(
            @HeaderMap Map<String, String> headers,
            @Field("selected_location_id") String selected_location_id,
            @Field("selected_date") String selected_date,
            @Field("view_type") String view_type
    );

    @FormUrlEncoded
    @POST("booking/showSlots")
    Call<ResponseBody> showSlots(
            @HeaderMap Map<String, String> headers,
            @Field("selected_date") String selected_date,
            @Field("selected_location_id") String selected_location_id,
            @Field("view_type") String view_type,
            @Field("selected_time") String selected_time,
            @Field("session_type") String session_type

    );

    @FormUrlEncoded
    @POST("booking/bookSession")
    Call<ResponseBody> bookSession(
            @HeaderMap Map<String, String> headers,
            @Field("sauna_no") String sauna_no,
            @Field("time_slot") String time_slot,
            @Field("booking_date") String booking_date,
            @Field("session_type") String session_type,
            @Field("selected_location_id") String selected_location_id
    );



    //Home
    @FormUrlEncoded
    @POST("general/get_summary")
    Call<ResponseBody> getSummaryByDate(
            @HeaderMap Map<String, String> headers,
            @Field("date") String date
    );

    //Side Menu

    @GET("general/view_calorie_stats")
    Call<ResponseBody> getCaloriesStat(
            @HeaderMap Map<String, String> headers
    );

    //Profile
    @GET("general/view_profile")
    Call<ResponseBody> viewUserProfile(
            @HeaderMap Map<String, String> headers
    );

    @GET("general/update_profile")
    Call<ResponseBody> updateProfile(
            @HeaderMap Map<String, String> headers,
            @Query("first_name") String first_name,
            @Query("last_name") String last_name,
            @Query("email") String email,
            @Query("dob") String dob,
            @Query("gender") String gender,
            @Query("height") String height,
            @Query("weight") String weight,
            @Query("address") String address);

    //Settings

    @POST("general/get_help_content")
    Call<ResponseBody> getHelp(
            @HeaderMap Map<String, String> headers
    );

    @POST("general/get_privacy_content")
    Call<ResponseBody> getPrivacyPolicy(
            @HeaderMap Map<String, String> headers
    );

    @POST("general/add_feedback")
    Call<ResponseBody> addFeedBack(
            @HeaderMap Map<String, String> headers,
            @Query("name") String name,
            @Query("email") String email,
            @Query("feedback") String feedback
    );

    //Book Appointment

    @GET("general/get_online_appointment_link")
    Call<ResponseBody> getBookingAppointments(
            @HeaderMap Map<String, String> headers,
            @Query("country") String country
    );

    //Rewards

    @POST("general/get_ninety_days_summary")
    Call<ResponseBody> getNinetyDaySummary(
            @HeaderMap Map<String, String> headers
    );

    @POST("general/check_redemption_status")
    Call<ResponseBody> getRedemptionStatus(
            @HeaderMap Map<String, String> headers
    );

    @POST("general/mark_redemption")
    Call<ResponseBody> markRedemption(
            @HeaderMap Map<String, String> headers
    );

    //LeaderBoard

    @POST("general/get_user_leaderboard_local")
    Call<ResponseBody> getLocalLeaderBoard(
            @HeaderMap Map<String, String> headers
    );

    @POST("general/get_user_leaderboard_global")
    Call<ResponseBody> getGlobalLeaderBoard(
            @HeaderMap Map<String, String> headers
    );

    //Activity
    @POST("general/view_user_activity_yearly")
    Call<ResponseBody> getUserActivity(
            @HeaderMap Map<String, String> headers
    );

    //Body Fat Percentage

    @POST("general/get_summary_thirty_days")
    Call<ResponseBody> getSummaryThirtyDays(
            @HeaderMap Map<String, String> headers
    );

    @POST("general/get_bodayfat_graph")
    Call<ResponseBody> getBodyFatGraph(
            @HeaderMap Map<String, String> headers
    );

    @Multipart
    @POST("general/upload_image")
    Call<ResponseBody> uploadImage(
            @HeaderMap Map<String, String> headers,
            @Part MultipartBody.Part uploaded_file
    );

    @FormUrlEncoded
    @POST("general/record_body_fat")
    Call<ResponseBody> recordMonthlyBodyFat(
            @HeaderMap Map<String, String> headers,
            @Field("weight_in_pound") String weight_in_pound,
            @Field("body_fat_percentage") String body_fat_percentage,
            @Field("date") String date,
            @Field("image") String image,
            @Field("answer") String answer
    );


    //Begin Session

    @POST("general/get_todays_sessions")
    Call<ResponseBody> getTodaysSession(
            @HeaderMap Map<String, String> headers
    );


    //Workout Summary Screen

    @GET("general/share_activity_fb")
    Call<ResponseBody> shareFacebook(
            @HeaderMap Map<String, String> headers,
            @Query("parent_activity_id") String parent_activity_id,
            @Query("device") String device
    );


    //VPT Video

    @FormUrlEncoded
    @POST("general/vpt_videos")
    Call<ResponseBody> getVptVideo(
            @HeaderMap Map<String, String> headers,
            @Field("node_type") String node_type
    );

    @POST("general/vpt_video_history")
    Call<ResponseBody> addToVideoHistory(
            @HeaderMap Map<String, String> headers,
            @Query("video_id") String video_id,
            @Query("device") String device
    );

    @FormUrlEncoded
    @POST("general/addfullsession")
    Call<UpdateSessionEnt> updateSessions(
            @HeaderMap Map<String, String> headers,
            @Field("app_name") String app_name,
            @Field("id") String id,
            @Field("parent_id") String parent_id,
            @Field("start_date") String start_date,
            @Field("end_date") String end_date,
            @Field("session_record_id") String session_record_id,
            @Field("workout_type") String workout_type,
            @Field("start_cal") String start_cal,
            @Field("end_cal") String end_cal,
            @Field("total_cal_burned") String total_cal_burned,
            @Field("is_cancelled") String is_cancelled,
            @Field("session_type_start") String session_type_start,
            @Field("session_type_end") String session_type_end,
            @Field("user_id") String user_id
    );

    //Dashboard

    @GET("general/GetFranchiseList")
    Call<ResponseBody> getFranchiseList(
            @HeaderMap Map<String, String> headers
    );

    @GET("general/get_franchise_summary")
    Call<ResponseBody> getFranchiseSummary(
            @HeaderMap Map<String, String> headers,
            @Query("location_id") String location_id,
            @Query("interval") String interval
    );

    //Activity
    @FormUrlEncoded
    @POST( "general/get_final_summary")
    Call<ResponseBody> getFinalSummary(
            @HeaderMap Map<String, String> headers,
            @Field("parent_activity_id") String parent_activity_id);

    //Notifications
    @POST("general/update_token")
    Call<ResponseBody> updateToken(
            @HeaderMap Map<String, String> headers,
            @Query("type") String type,
            @Query("token") String token);

    @GET("general/notification_user_action")
    Call<ResponseBody> updateUserAction(
            @HeaderMap Map<String, String> headers,
            @Query("notification_id") String notification_id,
            @Query("user_action") String user_action);

    //notification api's
    @POST("general/get_notification_history")
    Call<ResponseBody> getNotificationHistory(
            @HeaderMap Map<String, String> headers
    );
   @POST("general/mark_notification_as_read")
    Call<ResponseBody> getNotificationAsRead(
           @HeaderMap Map<String, String> headers,
           @Query("id") String id

    );

   @POST("general/mark_notification_attachment_as_downloaded")
    Call<ResponseBody> getNotificationAttachmentAsRead(
           @HeaderMap Map<String, String> headers,
           @Query("id") String id

    );

    // Nutrition Calories
    @POST("general/get_intermittent_plan")
    Call<ResponseBody> getIntermittentPlan(
            @HeaderMap Map<String, String> headers
    );

    // Nutrition Calories
    @POST("general/set_intermittent_plan")
    Call<ResponseBody> setIntermittentPlan(
            @HeaderMap Map<String, String> headers,
            @Body SetIntermittentPlan body
            );

    // Nutrition Calories
    @POST("general/get_intermittent_food_list")
    Call<ResponseBody> getIntermittentFood(
            @HeaderMap Map<String, String> headers
    );

    // Nutrition Calories
    @POST("general/get_reward_level")
    Call<ResponseBody> getRewards(
            @HeaderMap Map<String, String> headers
    );

    // Nutrition Calories
    @FormUrlEncoded
    @POST("general/get_nutritionix_calories")
    Call<ResponseBody> getNutritionCalories(
            @HeaderMap Map<String, String> headers,
            @Field("recording_date") String recording_date,
            @Field("local_date") String local_date,
            @Field("use_local_date") String use_local_date
    );

    // Search Food
    @GET(WebServiceConstants.CUSTOM_SUB_URL + "instant_search")
    Call<ResponseBody> getFood(
            @Query("query") String query
    );

    // Favorite Foods List
    @POST("general/set_intermittent_fav_list")
    Call<ResponseBody> setFavoriteFoods(
            @HeaderMap Map<String, String> headers,
            @Body SetFavoriteFood body
    );

    // Search Favorite Food
    @POST("general/get_intermittent_fav_list")
    Call<ResponseBody> getFavoriteFood(
            @HeaderMap Map<String, String> headers
    );

    // Search Food
    @GET(WebServiceConstants.CUSTOM_SUB_URL + "get_item_by_barcode")
    Call<ResponseBody> getBarCodeData(
            @Query("query") String query
    );

    // Add Cart Data
    @POST("general/add_nutritionix_calories")
    Call<ResponseBody> addCart(
            @HeaderMap Map<String, String> headers,
            @Body AddNutritionistRequestBody body
    );

    // Add Cart Data
    @POST("general/add_nutritionix_calories")
    Call<ResponseBody> addCartIntermittent(
            @HeaderMap Map<String, String> headers,
            @Body AddNutritionistRequestBody body
    );

    // View Nutritionist Summary
    @POST("general/view_90_days_activities")
    Call<ResponseBody> viewNutritionistSummary(
            @HeaderMap Map<String, String> headers
    );

    // Get Weight
    @POST("general/get_weight")
    Call<ResponseBody> getWeight(
            @HeaderMap Map<String, String> headers
    );

    // Set Weight
    @FormUrlEncoded
    @POST("general/set_weight")
    Call<ResponseBody> setWeight(
            @HeaderMap Map<String, String> headers,
            @Field("weight_in_pound") String weight_in_pound

    );

    // Delete Nutrition Calories
    @POST("general/delete_cal_intake")
    Call<ResponseBody> deleteNutritionCalories(
            @HeaderMap Map<String, String> headers,
            @Body DeleteCalRequestBody body
    );

    // Delete Nutrition Calories
    @POST("general/delete_exercise")
    Call<ResponseBody> deleteExercise(
            @HeaderMap Map<String, String> headers,
            @Body DeleteCalRequestBody body
    );

    // Get Weight
    @FormUrlEncoded
    @POST("general/set_weight_height")
    Call<ResponseBody> setWeightHeight(
            @HeaderMap Map<String, String> headers,
            @Field("weight_in_pound") String weight_in_pound,
            @Field("height_in_ft") String height_in_ft,
            @Field("dob") String dob
    );

    // GET Exercise cal data
    @GET(WebServiceConstants.CUSTOM_SUB_URL + "exercise")
    Call<ResponseBody> getExerciseCalData(
            @Query("query") String query
    );

    // Add Exercise Data
    @POST("general/add_nutritionix_exercise")
    Call<ResponseBody> addExercise(
            @HeaderMap Map<String, String> headers,
            @Body AddExerciseDataModel body
    );


    //Add Videos
    @POST("general/other_videos")
    Call<ResponseBody> other_videos(
            @HeaderMap Map<String, String> headers,
            @Body GettingStartedRequestModel body
    );


    //Brivo Api's

    @GET("general/get_brivo_location")
    Call<ResponseBody> getBrivoLocation(
            @HeaderMap Map<String, String> headers
    );
    @GET("general/get_brivo_user_sites")
    Call<ResponseBody> getBrivoUserSites(
            @HeaderMap Map<String, String> headers,
            @Query("latitude") String latitude,
            @Query("longitude") String longitude
    );


    @GET("general/get_sites_access_points")
    Call<ResponseBody> getSitesAccessSitesPoints(
            @HeaderMap Map<String, String> headers,
            @Query("site_id") String site_id
    );


    @POST("general/get_brivo_token")
    Call<ResponseBody> getBrivoToken(
            @HeaderMap Map<String, String> headers

    );

    @POST("unlock")
    Call<ResponseBody> unlock(
            @HeaderMap Map<String, String> headers,
            @Body BrivoCredentialRequestModel body
    );

    // GET New ACTIVITY API
    @GET("activities/SessionTypeGraph")
    Call<ResponseBody> getSessionTypeGraph(
            @HeaderMap Map<String, String> headers
    );

    @GET("activities/ActivityByLifeTime")
    Call<TimelineActivityDataModel> ActivityByLifeTime(
            @HeaderMap Map<String, String> headers,
            @Query("page_no") Integer page_no,
            @Query("page_limit") Integer page_limit,
            @Query("session_type") String session_type
    );

    @GET("activities/ActivityByTimeline")
    Call<ResponseBody> getActivityByTimeline(
            @HeaderMap Map<String, String> headers
    );

    //Referral
    @GET("activities/AmbassadorReferralURL")
    Call<ResponseBody> getRefferalScreen(
            @HeaderMap Map<String, String> headers
    );

    @GET("activities/GetleadAmbassador")
    Call<ResponseBody> getleadAmbassador(
            @HeaderMap Map<String, String> headers
    );


    //Business Card
    @GET("activities/EmployeeReferralInfo")
    Call<ResponseBody> getEmployeeReferralInfo(
            @HeaderMap Map<String, String> headers
    );


    //HotSquad List

    @FormUrlEncoded
    @POST("hotsquad/createSquad")
    Call<ResponseBody> createHotsquadList(
            @HeaderMap Map<String, String> headers,
            @Field("name") String name,
            @Field("desc") String desc
    );

    @GET("hotsquad/getSquadList")
    Call<ResponseBody> getHotsquadList(
            @HeaderMap Map<String, String> headers
    );


    @POST("hotsquad/searchSquadMembers")
    Call<ResponseBody> searchAddSquadMember(
            @HeaderMap Map<String, String> headers,
            @Body SearchListRequest request // Accepting the custom request body
    );

    // Vi Management
    @POST("vi/list_location_suana")
    Call<ResponseBody> getListLocationSuana(
            @HeaderMap Map<String, String> headers
    );

    @POST("vi/register_device")
    Call<ResponseBody> registerDevice(
            @HeaderMap Map<String, String> headers,
            @Body List<SetRegisterLocationModel> body
    );

    @POST("vi/unregister_device")
    Call<ResponseBody> unRegisterDevice(
            @HeaderMap Map<String, String> headers,
            @Body List<SetUnRegisterLocationModel> body
    );

}
