package com.hotworx.models;

import java.io.Serializable;

public class GetRewardResponse implements Serializable {
    private String msg;

    private Data[] data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Data[] getData() {
        return data;
    }

    public void setData(Data[] data) {

        if (data != null && data.length == 0) {
            this.data = null;
        } else {
            this.data = data;
        }


    }


    public static class Data implements Serializable {
        private InnerData data;

        public InnerData getData() {
            return data;
        }

        public void setData(InnerData data) {
            if (data == null ) {
                this.data = null;
            } else {
                this.data = data;
            }

        }
    }

    public static class InnerData implements Serializable {
        private Integer level;
        private Integer cal_burned;
        private Integer cal_req_for_next_level;
        private Integer next_level;
        private Integer days_left;
        private Integer total_days;
        private String city_state;
        private String location;
        private String average;

        public Integer getLevel() {
            return level;
        }

        public void setLevel(Integer level) {
            this.level = level;
        }

        public Integer getCal_burned() {
            return cal_burned;
        }

        public void setCal_burned(Integer cal_burned) {
            this.cal_burned = cal_burned;
        }

        public Integer getCal_req_for_next_level() {
            return cal_req_for_next_level;
        }

        public void setCal_req_for_next_level(Integer cal_req_for_next_level) {
            this.cal_req_for_next_level = cal_req_for_next_level;
        }

        public Integer getNext_level() {
            return next_level;
        }

        public void setNext_level(Integer next_level) {
            this.next_level = next_level;
        }

        public Integer getDays_left() {
            return days_left;
        }

        public void setDays_left(Integer days_left) {
            this.days_left = days_left;
        }

        public Integer getTotal_days() {
            return total_days;
        }

        public void setTotal_days(Integer total_days) {
            this.total_days = total_days;
        }

        public String getCity_state() {
            return city_state;
        }

        public void setCity_state(String city_state) {
            this.city_state = city_state;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getAverage() {
            return average;
        }

        public void setAverage(String average) {
            this.average = average;
        }
    }
}
