package com.musika.retrofit.model.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HomeResponse {

@SerializedName("message")
@Expose
private String message;
@SerializedName("data")
@Expose
private HomeData data;

public String getMessage() {
return message;
}

public void setMessage(String message) {
this.message = message;
}

public HomeData getData() {
return data;
}

public void setData(HomeData data) {
this.data = data;
}

}