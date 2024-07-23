package com.hotworx.requestEntity  ;



import java.io.Serializable;

public class SixtySessionResponse extends BaseModel implements Serializable{

    private String activity_id;

    public String getSixty_actual_burnt() {
        return sixty_actual_burnt;
    }

    public void setSixty_actual_burnt(String sixty_actual_burnt) {
        this.sixty_actual_burnt = sixty_actual_burnt;
    }

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    private String sixty_actual_burnt;


}
