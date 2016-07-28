package com.codezero.fireprevention.activity.listview;

/**
 * Created by GyungDal on 2016-07-27.
 */

public class Item {
    private String name;
    private boolean state;
    public Item(String name, boolean state) {
        this.name = name;
        this.state = state;
    }
    public String getName() {
        return name;
    }

    public boolean getState(){
        return state;
    }

    public void setState(boolean state){
        this.state = state;
    }

}