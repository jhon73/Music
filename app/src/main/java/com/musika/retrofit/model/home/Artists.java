package com.musika.retrofit.model.home;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Artists {

@SerializedName("title")
@Expose
private String title;
@SerializedName("data")
@Expose
private List<ArtistData> data = null;

public String getTitle() {
return title;
}

public void setTitle(String title) {
this.title = title;
}

public List<ArtistData> getData() {
return data;
}

public void setData(List<ArtistData> data) {
this.data = data;
}

}