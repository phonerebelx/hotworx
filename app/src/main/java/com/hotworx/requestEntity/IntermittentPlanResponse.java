package com.hotworx.requestEntity;

import java.io.Serializable;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.String;
import java.util.List;

public class IntermittentPlanResponse extends BaseModel<IntermittentPlanResponse> implements Serializable {
    private Integer intermittent_hrs;

    private String message;

    private Setting_data setting_data;

//    private Boolean status;



    public Integer getIntermittent_hrs() {
        return this.intermittent_hrs;
    }

    public void setIntermittent_hrs(Integer intermittent_hrs) {
        this.intermittent_hrs = intermittent_hrs;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Setting_data getSetting_data() {
        return this.setting_data;
    }

    public void setSetting_data(Setting_data setting_data) {
        this.setting_data = setting_data;
    }

//    public Boolean getStatus() {
//        return this.status;
//    }
//
//    public void setStatus(Boolean status) {
//        this.status = status;
//    }

    public static class Setting_data implements Serializable {
        private Boolean intermittent_status;

        private List<Plan_data> plan_data;

        public Boolean getIntermittent_status() {
            return this.intermittent_status;
        }

        public void setIntermittent_status(Boolean intermittent_status) {
            this.intermittent_status = intermittent_status;
        }

        public List<Plan_data> getPlan_data() {
            return this.plan_data;
        }

        public void setPlan_data(List<Plan_data> plan_data) {
            this.plan_data = plan_data;
        }

        public static class Plan_data implements Serializable {
            private String start_time;

            private Integer intermittent_hrs;

            private String plan_day;

            private String end_time;

            private Boolean active;

            private String plan_date;

            public String getStart_time() {
                return this.start_time;
            }

            public void setStart_time(String start_time) {
                this.start_time = start_time;
            }

            public Integer getIntermittent_hrs() {
                return this.intermittent_hrs;
            }

            public void setIntermittent_hrs(Integer intermittent_hrs) {
                this.intermittent_hrs = intermittent_hrs;
            }

            public Integer getIntermittent_hrs_in_Secs() { return this.intermittent_hrs*60*60; }

            public String getPlan_day() {
                return this.plan_day;
            }

            public void setPlan_day(String plan_day) {
                this.plan_day = plan_day;
            }

            public String getEnd_time() {
                return this.end_time;
            }

            public void setEnd_time(String end_time) {
                this.end_time = end_time;
            }

            public Boolean getActive() {
                return this.active;
            }

            public void setActive(Boolean active) {
                this.active = active;
            }

            public String getPlan_date() {
                return this.plan_date;
            }

            public void setPlan_date(String plan_date) {
                this.plan_date = plan_date;
            }
        }
    }
}

