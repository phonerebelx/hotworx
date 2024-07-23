package com.hotworx.requestEntity;

import com.google.gson.annotations.SerializedName;

public class VideoEnt {
    @SerializedName("video_id")
    String video_id;
    @SerializedName("video_level")
    String video_level;
    @SerializedName("video_name")
    String video_name;
    @SerializedName("video_link")
    String video_link;
    @SerializedName("android_url")
    String android_url;

    public String getAndroid_url() {
        return android_url;
    }

    public void setAndroid_url(String android_url) {
        this.android_url = android_url;
    }

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public String getVideo_level() {
        return video_level;
    }

    public void setVideo_level(String video_level) {
        this.video_level = video_level;
    }

    public String getVideo_name() {
        return video_name;
    }

    public void setVideo_name(String video_name) {
        this.video_name = video_name;
    }

    public String getVideo_link() {
        return video_link;
    }

    public void setVideo_link(String video_link) {
        this.video_link = video_link;
    }
}
