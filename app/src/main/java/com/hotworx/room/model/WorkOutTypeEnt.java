package com.hotworx.room.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.PrimaryKey;

import com.hotworx.requestEntity.WorkOutPOJO;

import java.util.ArrayList;

@Entity(tableName = "workout_type")
public class WorkOutTypeEnt {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "sixty_min_time")
    private String sixty_min_time;
    @ColumnInfo(name = "data")
    private String data;

    public WorkOutTypeEnt(){

    }

    @Ignore
    public WorkOutTypeEnt(int id,String sixty_min_time,String data){
        this.id = id;
        this.sixty_min_time = sixty_min_time;
        this.data = data;
    }

    @Ignore
    public WorkOutTypeEnt(String sixty_min_time,String data){
        this.sixty_min_time = sixty_min_time;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSixty_min_time() {
        return sixty_min_time;
    }

    public void setSixty_min_time(String sixty_min_time) {
        this.sixty_min_time = sixty_min_time;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
