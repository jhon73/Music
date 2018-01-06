
package com.musika.retrofit.model.explore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.musika.retrofit.WebAPI;

import fr.xebia.android.freezer.annotations.Model;

@Model
public class Artist {

    @SerializedName("idu")
    @Expose
    public Integer idu;
    @SerializedName("realname")
    @Expose
    public String realname;

    @SerializedName("image")
    @Expose
    public String image;

    public String getCover() {
        return WebAPI.ARTIST_COVER_PICTURE+cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getImage() {
        return WebAPI.ARTIST_PROFILE_PICTURE+image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @SerializedName("cover")

    @Expose
    public String cover;
    public Integer getIdu() {
        return idu;
    }

    public void setIdu(Integer id) {
        this.idu = id;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

}
