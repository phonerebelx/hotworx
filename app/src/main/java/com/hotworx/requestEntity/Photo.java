package com.hotworx.requestEntity;

import java.io.Serializable;

public class Photo implements Serializable {

    public String getThumb() {
        return this.thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    String thumb;

    public Object getHighres() {
        return this.highres;
    }

    public void setHighres(Object highres) {
        this.highres = highres;
    }

    Object highres;

    public boolean getIs_user_uploaded() {
        return this.is_user_uploaded;
    }

    public void setIs_user_uploaded(boolean is_user_uploaded) {
        this.is_user_uploaded = is_user_uploaded;
    }

    boolean is_user_uploaded;

}
