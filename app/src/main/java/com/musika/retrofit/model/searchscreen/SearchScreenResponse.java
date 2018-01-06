package com.musika.retrofit.model.searchscreen;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchScreenResponse {

@SerializedName("message")
@Expose
private String message;
@SerializedName("data")
@Expose
private SearchScreenData data;

public String getMessage() {
return message;
}

public void setMessage(String message) {
this.message = message;
}

public SearchScreenData getData() {
return data;
}

public void setData(SearchScreenData data) {
this.data = data;
}

}