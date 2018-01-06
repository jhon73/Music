package com.musika.retrofit.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LikeResponse {

@SerializedName("message")
@Expose
private String message;
@SerializedName("data")
@Expose
private List<LikeData> data = null;

public String getMessage() {
return message;
}

public void setMessage(String message) {
this.message = message;
}

public List<LikeData> getData() {
return data;
}

public void setData(List<LikeData> data) {
this.data = data;
}

}