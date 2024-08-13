package com.hotworx.room.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.hotworx.room.model.SessionEnt;

import java.util.List;

import retrofit2.http.DELETE;

@Dao
public interface SessionTypeDao {

    @Insert
    long insert(SessionEnt sessionEnt);

    @Update
    int update(SessionEnt sessionEnt);

    @Query("SELECT * FROM session WHERE id = :id")
    SessionEnt getSession(int id);

    @Query("UPDATE session SET activity_id = :activity_id, parent_id = :parent_id WHERE id = :id")
    void updateActivitiesId(String activity_id,String parent_id,int id);

    @Query("UPDATE session SET end_calories = :end_calories, end_picture = :end_picture,end_date = :end_date,session_type_end = :session_type_end WHERE id = :id")
    void updateEndCalories(String end_calories,String end_picture,String end_date,String session_type_end,int id);

    @Query("UPDATE session SET is_cancelled = :iscancelled,end_calories =:session_calories, end_date = :end_date WHERE id = :id")
    void updateCancellation(String iscancelled,String session_calories,int id, String end_date);

    @Query("DELETE FROM session  WHERE activity_id = :activity_id")
    void deleteSessionByActivityId(String activity_id);

    @Query("DELETE FROM session")
    void deleteAllSessions();

    @Query("SELECT * FROM session")
    List<SessionEnt> getAllSession();

    @Query("SELECT * FROM session WHERE parent_id = :parent_id")
    List<SessionEnt> getAllSessionByParentId(String parent_id);


}
