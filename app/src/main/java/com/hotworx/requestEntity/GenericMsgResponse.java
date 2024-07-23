package com.hotworx.requestEntity;

public class GenericMsgResponse extends BaseModel<GenericMsgResponse>{

    private boolean status;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
