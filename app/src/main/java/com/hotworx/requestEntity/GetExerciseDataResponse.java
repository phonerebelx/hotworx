package com.hotworx.requestEntity;

import java.util.ArrayList;

public class GetExerciseDataResponse {

    public ArrayList<Exercise> getExercises() {
        return this.exercises;
    }

    public void setExercises(ArrayList<Exercise> exercises) {
        this.exercises = exercises;
    }

    ArrayList<Exercise> exercises;

}
