package com.hotworx.requestEntity  ;

import java.util.ArrayList;

/**
 * Created by Addi.
 */
public class CaloriesDetailPojo {

    String sixty_burnt;
    ArrayList<BurntPojo> forty_burnt;

    public String getSixty_burnt() {
        return sixty_burnt;
    }

    public void setSixty_burnt(String sixty_burnt) {
        this.sixty_burnt = sixty_burnt;
    }

    public ArrayList<BurntPojo> getForty_burnt() {
        return forty_burnt;
    }

    public void setForty_burnt(ArrayList<BurntPojo> forty_burnt) {
        this.forty_burnt = forty_burnt;
    }
}
