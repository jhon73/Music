package com.musika.retrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecentPlayResponse {

@SerializedName("message")
@Expose
private String message;
@SerializedName("data")
@Expose
private List<RecentPlayData> data = null;

public String getMessage() {
return message;
}

public void setMessage(String message) {
this.message = message;
}

public List<RecentPlayData> getData() {
return data;
}

public void setData(List<RecentPlayData> data) {
this.data = data;
}

}