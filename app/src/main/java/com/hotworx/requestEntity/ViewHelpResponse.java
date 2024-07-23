package com.hotworx.requestEntity  ;



import java.io.Serializable;

public class ViewHelpResponse extends BaseModel<ViewHelpResponse>implements Serializable{

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    private String contents;
}
