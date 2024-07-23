package com.hotworx.room.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.hotworx.room.model.SessionEnt;
import com.hotworx.room.model.SummaryEnt;

@Dao
public interface SummaryDao {
    @Insert
    long insert(SummaryEnt summaryEnt);

    @Update
    int update(SummaryEnt summaryEnt);

    @Query("SELECT * FROM SUMMARY LIMIT 1")
    SummaryEnt getOfflineSummary();
}
