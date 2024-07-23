package com.hotworx.requestEntity  ;


import java.io.Serializable;

public class GetUrlResponse extends BaseModel implements Serializable {

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    private String contents;
}
