package com.hotworx.requestEntity  ;


import java.io.Serializable;

public class ViewAllSessionResponse implements Serializable {
    public String getSession_type() {
        return session_type;
    }

    public void setSession_type(String session_type) {
        this.session_type = session_type;
    }

    private String session_type;

}
