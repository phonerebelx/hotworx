package com.hotworx.requestEntity  ;


import java.io.Serializable;

public class LocationsPOJO implements Serializable {

    private String location_name;
    private String validation_code;

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    private String location_id;

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public String getValidation_code() {
        return validation_code;
    }

    public void setValidation_code(String validation_code) {
        this.validation_code = validation_code;
    }
}
