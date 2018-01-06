package com.musika.retrofit.model.searchscreen;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.musika.retrofit.model.Track;

public class Tracks {

@SerializedName("title")
@Expose
private String title;
@SerializedName("data")
@Expose
private List<Track> data = null;

public String getTitle() {
return title;
}

public void setTitle(String title) {
this.title = title;
}

public List<Track> getData() {
return data;
}

public void setData(List<Track> data) {
this.data = data;
}

}
