package com.hotworx.requestEntity  ;



import java.io.Serializable;

public class LoginResponse extends BaseModel implements Serializable {

    private String user_id;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


}
