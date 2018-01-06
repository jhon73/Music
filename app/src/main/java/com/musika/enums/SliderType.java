package com.musika.enums;

/**
 * Created by sachin on 7/13/17.
 */

public enum SliderType {

    TRACK("track"),ARTIST("artist"),ALBUM("album"),EXTERNAL("external");
    public String val;
    SliderType(String val){
        this.val=val;
    }
}
