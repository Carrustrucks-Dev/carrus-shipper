package com.carrus.carrusshipper.model;

/**
 * Created by Sunny on 11/16/15 for CarrusShipper.
 */
public class Header {
    private String name;
    private boolean isVisible;

    public Header(String name , boolean isVisible){
        this.name=name;
        this.isVisible=isVisible;
    }


    public boolean isVisible() {
        return isVisible;
    }

    public void setIsVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
