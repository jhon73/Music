package com.musika.retrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddSongPLaylistResponse {

@SerializedName("message")
@Expose
private String message;
@SerializedName("count")
@Expose
private Integer count;
@SerializedName("data")
@Expose
private AddSongPlaylistData data;

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

public AddSongPlaylistData getData() {
return data;
}

public void setData(AddSongPlaylistData data) {
this.data = data;
}

}