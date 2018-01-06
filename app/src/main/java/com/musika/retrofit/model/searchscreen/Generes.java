package com.musika.retrofit.model.searchscreen;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Generes {

@SerializedName("title")
@Expose
private String title;
@SerializedName("data")
@Expose
private List<GeneresData> data = null;

public String getTitle() {
return title;
}

public void setTitle(String title) {
this.title = title;
}

public List<GeneresData> getData() {
return data;
}

public void setData(List<GeneresData> data) {
this.data = data;
}

}