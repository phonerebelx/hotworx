package com.hotworx.room.DAO;

import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.hotworx.requestEntity.Branded;
import com.hotworx.room.model.WorkOutTypeEnt;

import java.util.ArrayList;
import java.util.List;

import javax.sql.StatementEventListener;

@Dao
public interface WorkoutTypeDao {
    @Insert
    void insert(WorkOutTypeEnt workoutTypeDao);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(WorkOutTypeEnt workoutTypeDao);

    @Query("Select * FROM workout_type LIMIT 1")
    WorkOutTypeEnt getWorkoutType();

}
