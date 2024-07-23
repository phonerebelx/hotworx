package com.hotworx.requestEntity ;



import java.io.Serializable;

public class AddStartSessionResponse extends BaseModel implements Serializable{
    private String activity_id;

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }


}
