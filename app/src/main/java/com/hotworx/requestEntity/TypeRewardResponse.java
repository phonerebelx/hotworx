package com.hotworx.requestEntity  ;

/**
 * Created by ingic.developer on 07-Aug-18.
 */
public class TypeRewardResponse {
    private String reward_id;
    private String reward;
    private String reward_description;
    private String gender;
    private String is_redeemed;

    public String getReward_id() {
        return reward_id;
    }

    public void setReward_id(String reward_id) {
        this.reward_id = reward_id;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getReward_description() {
        return reward_description;
    }

    public void setReward_description(String reward_description) {
        this.reward_description = reward_description;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIs_redeemed() {
        return is_redeemed;
    }

    public void setIs_redeemed(String is_redeemed) {
        this.is_redeemed = is_redeemed;
    }
}
