package com.musika.retrofit.model.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.musika.retrofit.WebAPI;

public class AlbumsData {

@SerializedName("id")
@Expose
private Integer id;
@SerializedName("name")
@Expose
private String name;
@SerializedName("image")
@Expose
private String image;
@SerializedName("artist")
@Expose
private Artist artist;

public Integer getId() {
return id;
}

public void setId(Integer id) {
this.id = id;
}

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

public String getImage() {
return WebAPI.ALBUM_PICTURE+image;
}

public void setImage(String image) {
this.image = image;
}

public Artist getArtist() {
return artist;
}

public void setArtist(Artist artist) {
this.artist = artist;
}

}