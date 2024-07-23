package com.hotworx.requestEntity  ;
/*
 * Created by Aamir Saleem on 11/4/2017.
   Email ID: aamirsaleem06@gmail.com
   Skype ID: aamirsaleem07
*/

public class AnsModel  {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public AnsModel(String name, boolean selected) {
        this.name = name;
        this.selected = selected;
    }

    private String name;
    private boolean selected;
}
