package com.hotworx.requestEntity;

public class WeightHeightRequest {

    private String user_id;
    private String dob;
    private String height_in_ft;
    private String weight_in_pound;

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setHeight(String height_in_ft) {
        this.height_in_ft = height_in_ft;
    }

    public String getHeight() {
        return height_in_ft;
    }

    public void setWeight(String weight_in_pound) {
        this.weight_in_pound = weight_in_pound;
    }

    public String getWeight() {
        return weight_in_pound;
    }


    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_id() {
        return user_id;
    }
}
