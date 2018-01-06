package com.musika.retrofit.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.musika.retrofit.WebAPI;
import com.musika.retrofit.model.explore.*;

import fr.xebia.android.freezer.annotations.Model;

@Model
public class LikeData {

@SerializedName("id")
@Expose
public Integer id;
@SerializedName("title")
@Expose
public String title;
@SerializedName("description")
@Expose
public String description;
@SerializedName("name")
@Expose
public String name;
@SerializedName("tag")
@Expose
public String tag;
@SerializedName("artist")
@Expose
public ArtistData artist;
@SerializedName("album")
@Expose
public AlbumData album = null;

public Integer getId() {
return id;
}

public void setId(Integer id) {
this.id = id;
}

public String getTitle() {
return title;
}

public void setTitle(String title) {
this.title = title;
}

public String getDescription() {
return description;
}

public void setDescription(String description) {
this.description = description;
}

public String getName() {
return WebAPI.TRACK_FILE+name;
}

public void setName(String name) {
this.name = name;
}

public String getTag() {
return tag;
}

public void setTag(String tag) {
this.tag = tag;
}

public ArtistData getArtist() {
return artist;
}

public void setArtist(ArtistData artist) {
this.artist = artist;
}

public AlbumData getAlbum() {
return album;
}

public void setAlbum(AlbumData album) {
this.album = album;
}

}