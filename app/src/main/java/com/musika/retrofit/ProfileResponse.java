package com.musika.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileResponse {

@SerializedName("message")
@Expose
private String message;
@SerializedName("data")
@Expose
private ProfileData data;

public String getMessage() {
return message;
}

public void setMessage(String message) {
this.message = message;
}

public ProfileData getData() {
return data;
}

public void setData(ProfileData data) {
this.data = data;
}

}