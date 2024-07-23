package com.hotworx.room.model;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.fitbit.api.models.Summary;

@Entity(tableName = "summary")
public class SummaryEnt {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "summary")
    private String summary;
    @ColumnInfo(name = "classes_completed")
    private String classes;

    public SummaryEnt(){

    }

    @Ignore
    public SummaryEnt(String summary,String classes){
        this.summary = summary;
        this.classes = classes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getClasses() {
        return classes;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }
}
