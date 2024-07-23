package com.hotworx.requestEntity;

import java.io.Serializable;
import java.util.List;

public class SetIntermittentPlan implements Serializable {
    private Boolean intermittent_status;

    private Integer intermittent_hrs;



    private List<IntermittentPlanResponse.Setting_data.Plan_data> plan_data;

    public Boolean getIntermittent_status() {
        return this.intermittent_status;
    }

    public void setIntermittent_status(Boolean intermittent_status) {
        this.intermittent_status = intermittent_status;
    }

    public Integer getIntermittent_hrs() {
        return this.intermittent_hrs;
    }

    public void setIntermittent_hrs(Integer intermittent_hrs) {
        this.intermittent_hrs = intermittent_hrs;
    }



    public List<IntermittentPlanResponse.Setting_data.Plan_data> getPlan_data() {
        return this.plan_data;
    }

    public void setPlan_data(List<IntermittentPlanResponse.Setting_data.Plan_data> plan_data) {
        this.plan_data = plan_data;
    }

}
