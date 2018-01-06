package com.musika.retrofit.model.home;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Albums {

@SerializedName("title")
@Expose
private String title;
@SerializedName("data")
@Expose
private List<AlbumsData> data = null;

public String getTitle() {
return title;
}

public void setTitle(String title) {
this.title = title;
}

public List<AlbumsData> getData() {
return data;
}

public void setData(List<AlbumsData> data) {
this.data = data;
}

}