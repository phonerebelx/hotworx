package com.hotworx.requestEntity  ;


import java.io.Serializable;

public class FortySessionResponse extends BaseModel implements Serializable{

    private String activity_id;

    public String getForty_actual_burnt() {
        return forty_actual_burnt;
    }

    public void setForty_actual_burnt(String forty_actual_burnt) {
        this.forty_actual_burnt = forty_actual_burnt;
    }

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    private String forty_actual_burnt;
}
