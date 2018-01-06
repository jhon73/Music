package com.musika.retrofit.model.genrictracks;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GenericsResponse {

@SerializedName("message")
@Expose
private String message;
@SerializedName("data")
@Expose
private GenresData data;

public String getMessage() {
return message;
}

public void setMessage(String message) {
this.message = message;
}

public GenresData getData() {
return data;
}

public void setData(GenresData data) {
this.data = data;
}

}
