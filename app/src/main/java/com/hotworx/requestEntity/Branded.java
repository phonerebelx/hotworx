package com.hotworx.requestEntity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Branded implements Serializable {

    String food_name;
    String serving_unit;
    String nix_brand_id;
    String brand_name_item_name;
    double serving_qty;
    Photo photo;
    Float nf_calories;
    String brand_name;
    int region;
    int brand_type;
    String locale;
    String nix_item_id;

    public String getFood_name() {
        return this.food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public String getServing_unit() {
        return this.serving_unit;
    }

    public void setServing_unit(String serving_unit) {
        this.serving_unit = serving_unit;
    }


    public String getNix_brand_id() {
        return this.nix_brand_id;
    }

    public void setNix_brand_id(String nix_brand_id) {
        this.nix_brand_id = nix_brand_id;
    }


    public String getBrand_name_item_name() {
        return this.brand_name_item_name;
    }

    public void setBrand_name_item_name(String brand_name_item_name) {
        this.brand_name_item_name = brand_name_item_name;
    }

    public double getServing_qty() {
        return this.serving_qty;
    }

    public void setServing_qty(double serving_qty) {
        this.serving_qty = serving_qty;
    }

    public float getNf_calories() {
        return this.nf_calories;
    }

    public void setNf_calories(float nf_calories) {
        this.nf_calories = nf_calories;
    }


    public Photo getPhoto() {
        return this.photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public String getBrand_name() {
        return this.brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public int getRegion() {
        return this.region;
    }

    public void setRegion(int region) {
        this.region = region;
    }

    public int getBrand_type() {
        return this.brand_type;
    }

    public void setBrand_type(int brand_type) {
        this.brand_type = brand_type;
    }

    public String getNix_item_id() {
        return this.nix_item_id;
    }

    public void setNix_item_id(String nix_item_id) {
        this.nix_item_id = nix_item_id;
    }

    public String getLocale() {
        return this.locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }


}
