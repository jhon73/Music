package com.musika.retrofit.model.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.musika.retrofit.WebAPI;

public class Slider {

@SerializedName("slider_name")
@Expose
private String sliderName;
@SerializedName("type")
@Expose
private String type;
@SerializedName("value")
@Expose
private String value;

public String getSliderName() {
return WebAPI.SLIDER_PICTURE+sliderName;
}

public void setSliderName(String sliderName) {
this.sliderName = sliderName;
}

public String getType() {
return type;
}

public void setType(String type) {
this.type = type;
}

public String getValue() {
return value;
}

public void setValue(String value) {
this.value = value;
}

}