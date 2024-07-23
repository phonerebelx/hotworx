package com.hotworx.requestEntity;

import java.io.Serializable;

public class Common implements Serializable {

    public String getFood_name() {
        return this.food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    String food_name;

    public String getServing_unit() {
        return this.serving_unit;
    }

    public void setServing_unit(String serving_unit) {
        this.serving_unit = serving_unit;
    }

    String serving_unit;

    public String getTag_name() {
        return this.tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    String tag_name;

    public float getServing_qty() {
        return this.serving_qty;
    }

    public void setServing_qty(int serving_qty) {
        this.serving_qty = serving_qty;
    }

    float serving_qty;

    public int getCommon_type() {
        return this.common_type;
    }

    public void setCommon_type(int common_type) {
        this.common_type = common_type;
    }

    int common_type;

    public String getTag_id() {
        return this.tag_id;
    }

    public void setTag_id(String tag_id) {
        this.tag_id = tag_id;
    }

    String tag_id;

    public Photo getPhoto() {
        return this.photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    Photo photo;

    public String getLocale() {
        return this.locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    String locale;
}
