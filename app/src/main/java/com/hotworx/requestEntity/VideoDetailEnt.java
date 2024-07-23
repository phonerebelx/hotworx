package com.hotworx.requestEntity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class VideoDetailEnt {

    @SerializedName("category_name")
    String category_name;
    @SerializedName("category_id")
    String category_id;
    @SerializedName("videos")
    ArrayList<VideoEnt> videos;

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public ArrayList<VideoEnt> getVideos() {
        return videos;
    }

    public void setVideos(ArrayList<VideoEnt> videos) {
        this.videos = videos;
    }
}
