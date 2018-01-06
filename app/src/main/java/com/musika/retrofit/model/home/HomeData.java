package com.musika.retrofit.model.home;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HomeData {

@SerializedName("sliders")
@Expose
private List<Slider> sliders = null;
@SerializedName("artists")
@Expose
private Artists artists;
@SerializedName("albums")
@Expose
private Albums albums;
    
@SerializedName("other_row")
@Expose
private List<OtherRowResponse> otherRow = null;

public List<Slider> getSliders() {
return sliders;
}

public void setSliders(List<Slider> sliders) {
this.sliders = sliders;
}

public Artists getArtists() {
return artists;
}

public void setArtists(Artists artists) {
this.artists = artists;
}

public Albums getAlbums() {
return albums;
}

public void setAlbums(Albums albums) {
this.albums = albums;
}

public List<OtherRowResponse> getOtherRow() {
return otherRow;
}

public void setOtherRow(List<OtherRowResponse> otherRow) {
this.otherRow = otherRow;
}

}