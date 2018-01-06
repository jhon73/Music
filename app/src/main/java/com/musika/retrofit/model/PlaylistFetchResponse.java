package com.musika.retrofit.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlaylistFetchResponse {

@SerializedName("message")
@Expose
private String message;
@SerializedName("count")
@Expose
private Integer count;
@SerializedName("data")
@Expose
private List<PlaylistFetchData> data = null;

public String getMessage() {
return message;
}

public void setMessage(String message) {
this.message = message;
}

public Integer getCount() {
return count;
}

public void setCount(Integer count) {
this.count = count;
}

public List<PlaylistFetchData> getData() {
return data;
}

public void setData(List<PlaylistFetchData> data) {
this.data = data;
}

}