package com.hotworx.requestEntity  ;

public class RewardSummaryPojo {
    private String start_date;
    private String end_date;
    private String calories;
    private String expiry_date;
    private String lock_status;
    private String allow_redeem;

    public String getStart_date() {
        return start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public String getCalories() {
        return calories;
    }

    public String getExpiry_date() {
        return expiry_date;
    }

    public String getLock_status() {
        return lock_status;
    }

    public String getAllow_redeem() {
        return allow_redeem;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public void setExpiry_date(String expiry_date) {
        this.expiry_date = expiry_date;
    }

    public void setLock_status(String lock_status) {
        this.lock_status = lock_status;
    }

    public void setAllow_redeem(String allow_redeem) {
        this.allow_redeem = allow_redeem;
    }
}
