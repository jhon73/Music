package com.musika.retrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AlbumByIdResponse {

@SerializedName("message")
@Expose
private String message;
@SerializedName("data")
@Expose
private AlbumByIdData data;

public String getMessage() {
return message;
}

public void setMessage(String message) {
this.message = message;
}

public AlbumByIdData getData() {
return data;
}

public void setData(AlbumByIdData data) {
this.data = data;
}

}