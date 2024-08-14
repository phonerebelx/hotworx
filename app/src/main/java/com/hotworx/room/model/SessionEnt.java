package com.hotworx.room.model;


import android.nfc.tech.NfcA;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.InverseBindingAdapter;
import androidx.media.AudioAttributesCompat;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "session")
public class SessionEnt {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "activity_id")
    String activity_id;
    @ColumnInfo(name = "parent_id")
    String parent_id;
    @ColumnInfo(name = "workout_type")
    String workout_type;
    @ColumnInfo(name = "start_date")
    String start_date;
    @ColumnInfo(name = "end_date")
    String end_date;
    @ColumnInfo(name = "start_calories")
    String start_calories;
    @ColumnInfo(name = "end_calories")
    String end_calories;
    @ColumnInfo(name = "session_type_start")
    String session_type_start;
    @ColumnInfo(name = "session_type_end")
    String session_type_end;
    @ColumnInfo(name = "start_picture")
    String start_picture;
    @ColumnInfo(name = "end_picture")
    String end_picture;
    @ColumnInfo(name = "is_afterburn")
    boolean is_afterburn;
    @ColumnInfo(name = "session_time")
    String session_time;
    @ColumnInfo(name = "is_cancelled")
    String is_cancelled;
    @ColumnInfo(name = "session_record_id")
    String session_record_id;

    public SessionEnt(){

    }

    @Ignore
    public SessionEnt(String start_calories,String start_picture,String start_date,String end_date,String workout_type,String session_type_start,boolean is_afterburn,String session_time,String is_cancelled, String session_record_id){
        this.start_calories = start_calories;
        this.start_picture = start_picture;
        this.start_date = start_date;
        this.end_date = end_date;
        this.session_type_start = session_type_start;
        this.workout_type = workout_type;
        this.is_afterburn = is_afterburn;
        this.session_time = session_time;
        this.is_cancelled = is_cancelled;
        this.session_record_id = session_record_id;
    }

    @Ignore
    public SessionEnt(String start_calories,String start_picture,String start_date,String end_date,String end_calories,String end_picture,String workout_type,String session_type_start,String session_type_end,String activity_id,String parent_id,boolean is_afterburn,String session_time,String is_cancelled){
        this.start_calories = start_calories;
        this.start_picture = start_picture;
        this.start_date = start_date;
        this.end_date = end_date;
        this.end_calories = end_calories;
        this.end_picture = end_picture;
        this.session_type_start = session_type_start;
        this.workout_type = workout_type;
        this.activity_id = activity_id;
        this.parent_id = parent_id;
        this.is_afterburn = is_afterburn;
        this.session_time = session_time;
        this.is_cancelled = is_cancelled;
        this.session_type_end = session_type_end;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSession_type_start() {
        return session_type_start;
    }

    public void setSession_type_start(String session_type_start) {
        this.session_type_start = session_type_start;
    }

    public String getWorkout_type() {
        return workout_type;
    }

    public void setWorkout_type(String workout_type) {
        this.workout_type = workout_type;
    }

    public String getStart_calories() {
        return start_calories;
    }

    public void setStart_calories(String start_calories) {
        this.start_calories = start_calories;
    }

    public String getStart_picture() {
        return start_picture;
    }

    public void setStart_picture(String start_picture) {
        this.start_picture = start_picture;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public String getEnd_calories() {
        return end_calories;
    }

    public void setEnd_calories(String end_calories) {
        this.end_calories = end_calories;
    }

    public String getEnd_picture() {
        return end_picture;
    }

    public void setEnd_picture(String end_picture) {
        this.end_picture = end_picture;
    }


    public boolean isIs_afterburn() {
        return is_afterburn;
    }

    public void setIs_afterburn(boolean is_afterburn) {
        this.is_afterburn = is_afterburn;
    }

    public String getSession_time() {
        return session_time;
    }

    public void setSession_time(String session_time) {
        this.session_time = session_time;
    }

    public String getIs_cancelled() {
        return is_cancelled;
    }

    public void setIs_cancelled(String is_cancelled) {
        this.is_cancelled = is_cancelled;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getSession_type_end() {
        return session_type_end;
    }

    public void setSession_type_end(String session_type_end) {
        this.session_type_end = session_type_end;
    }

    public String getSession_record_id() {
        return session_record_id;
    }

    public void setSession_record_id(String session_record_id) {
        this.session_record_id = session_record_id;
    }
}
