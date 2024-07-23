package com.hotworx.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.hotworx.requestEntity.WorkOutTypesResp;
import com.hotworx.room.DAO.SessionTypeDao;
import com.hotworx.room.DAO.SummaryDao;
import com.hotworx.room.DAO.WorkoutTypeDao;
import com.hotworx.room.model.SessionEnt;
import com.hotworx.room.model.SummaryEnt;
import com.hotworx.room.model.WorkOutTypeEnt;

@Database(entities = {WorkOutTypeEnt.class, SessionEnt.class, SummaryEnt.class},version = 2)

public abstract class HotworxDatabase extends RoomDatabase {
    public abstract WorkoutTypeDao getWorkoutTypeDao();
    public abstract SessionTypeDao getSessionTypeDao();
    public abstract SummaryDao getSummaryDao();
}
