package com.musika.retrofit.model.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.musika.retrofit.WebAPI;

public class OtherRowArtist {

@SerializedName("idu")
@Expose
private Integer idu;
@SerializedName("realname")
@Expose
private String realname;
@SerializedName("image")
@Expose
private String image;
@SerializedName("cover")
@Expose
private String cover;

public Integer getIdu() {
return idu;
}

public void setIdu(Integer idu) {
this.idu = idu;
}

public String getRealname() {
return realname;
}

public void setRealname(String realname) {
this.realname = realname;
}

public String getImage() {
return WebAPI.ARTIST_PROFILE_PICTURE+image;
}

public void setImage(String image) {
this.image = image;
}

public String getCover() {
return WebAPI.ARTIST_COVER_PICTURE+cover;
}

public void setCover(String cover) {
this.cover = cover;
}

}