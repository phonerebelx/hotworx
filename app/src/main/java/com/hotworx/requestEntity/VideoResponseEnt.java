package com.hotworx.requestEntity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class VideoResponseEnt extends BaseModel<VideoResponseEnt> {
    @SerializedName("videos")
    ArrayList<VideoDetailEnt> videos;

    public ArrayList<VideoDetailEnt> getVideos() {
        return videos;
    }

    public void setVideos(ArrayList<VideoDetailEnt> videos) {
        this.videos = videos;
    }
}
