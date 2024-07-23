package com.hotworx.requestEntity  ;

import java.io.Serializable;

public class WorkOutPOJO implements Serializable {
    private String type;
    private String duration;
    private String apple_watch_type;
    private String is_after_burnt;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getApple_watch_type() {
        return apple_watch_type;
    }

    public void setApple_watch_type(String apple_watch_type) {
        this.apple_watch_type = apple_watch_type;
    }

    public String getIs_after_burnt() {
        return is_after_burnt;
    }

    public void setIs_after_burnt(String is_after_burnt) {
        this.is_after_burnt = is_after_burnt;
    }
}