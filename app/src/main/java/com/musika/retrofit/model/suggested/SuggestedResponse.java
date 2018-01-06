package com.musika.retrofit.model.suggested;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SuggestedResponse {

@SerializedName("message")
@Expose
private String message;
@SerializedName("data")
@Expose
private List<SuggestedDataBean> data = null;

public String getMessage() {
return message;
}

public void setMessage(String message) {
this.message = message;
}

public List<SuggestedDataBean> getData() {
return data;
}

public void setData(List<SuggestedDataBean> data) {
this.data = data;
}

}


