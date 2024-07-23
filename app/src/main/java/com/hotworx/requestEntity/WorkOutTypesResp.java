package com.hotworx.requestEntity;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WorkOutTypesResp extends BaseModel implements Serializable {
    private String sixty_min_time;
    private ArrayList<WorkOutPOJO> workoutData;

    public ArrayList<WorkOutPOJO> getWorkoutData() {
        return workoutData;
    }

    public void setWorkoutData(ArrayList<WorkOutPOJO> workoutData) {
        this.workoutData = workoutData;
    }

    public String getSixty_min_time() {
        return sixty_min_time;
    }

    public void setSixty_min_time(String sixty_min_time) {
        this.sixty_min_time = sixty_min_time;
    }
}