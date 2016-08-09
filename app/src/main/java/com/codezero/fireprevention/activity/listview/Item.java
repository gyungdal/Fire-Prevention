package com.codezero.fireprevention.activity.listview;

/**
 * Created by GyungDal on 2016-07-27.
 */

public class Item {
    private String name;
    private boolean state;
    private boolean isdel;
    public Item(String name, boolean state, boolean isdel) {
        this.name = name;
        this.state = state;
        this.isdel = isdel;
    }

    public void setIsdel(boolean isdel){
        this.isdel = isdel;
    }

    public boolean getIsdel(){ return this.isdel; }

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