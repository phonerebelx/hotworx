package com.hotworx.requestEntity  ;

import java.io.Serializable;

public class LeaderBoardPOJO implements Serializable {

    private String username;
    private String TotalCaloriesBurnt;
    private String user_id;
    private String reward;
    private String img_url;
    private String selft_entry;

    public String getSelft_entry() {
        return selft_entry;
    }

    public void setSelft_entry(String selft_entry) {
        this.selft_entry = selft_entry;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public String getTotalCaloriesBurnt() {
        return TotalCaloriesBurnt;
    }

    public void setTotalCaloriesBurnt(String totalCaloriesBurnt) {
        TotalCaloriesBurnt = totalCaloriesBurnt;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }
}
