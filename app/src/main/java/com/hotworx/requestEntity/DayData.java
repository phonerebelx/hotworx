package com.hotworx.requestEntity;

import java.util.List;

public class DayData {

    String record_id;
    String product_id;
    String product_name = "";
    String qty;
    String unit;
    String claorie_count = "";
    String type;
    String img_url;


    public String getRecord_id() {
        return this.record_id;
    }

    public void setRecord_id(String record_id) {
        this.record_id = record_id;
    }

    public String getProduct_id() {
        return this.product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }


    public String getProduct_name() {
        return this.product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }


    public String getQty() {
        return this.qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }


    public String getUnit() {
        return this.unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }


    public String getClaorie_count() {
        return this.claorie_count;
    }

    public void setClaorie_count(String claorie_count) {
        this.claorie_count = claorie_count;
    }


    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getImg_url() {
        return this.img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

}
