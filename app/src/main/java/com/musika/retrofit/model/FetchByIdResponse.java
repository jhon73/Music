package com.musika.retrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FetchByIdResponse {

@SerializedName("message")
@Expose
private String message;
@SerializedName("count")
@Expose
private Integer count;
@SerializedName("data")
@Expose
private FetchByIdPlaylistInfoData data;

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

public FetchByIdPlaylistInfoData getData() {
return data;
}

public void setData(FetchByIdPlaylistInfoData data) {
this.data = data;
}

}
