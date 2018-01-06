package com.musika.retrofit.model.suggested;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SuggestedDataBean {

@SerializedName("title")
@Expose
private String title;
@SerializedName("type")
@Expose
private String type;
@SerializedName("data")
@Expose
private List<SuggestedData> data = null;

public String getTitle() {
return title;
}

public void setTitle(String title) {
this.title = title;
}

public String getType() {
return type;
}

public void setType(String type) {
this.type = type;
}

public List<SuggestedData> getData() {
return data;
}

public void setData(List<SuggestedData> data) {
this.data = data;
}

}
