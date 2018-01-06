package com.musika.retrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.musika.retrofit.WebAPI;

import fr.xebia.android.freezer.annotations.Model;

/**
 * Created by sachin on 8/11/17.
 * 
 */
@Model
public class AddSongArtistResponse {

    // only use for Add song playlist
    @SerializedName("idu")
    @Expose
    public Integer idu;
    @SerializedName("image")
    @Expose
    public String image;
    @SerializedName("cover")
    @Expose
    public String cover;

    public Integer getIdu() {
        return idu;
    }
    public void setIdu(Integer idu) {
        this.idu = idu;
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
    ////


    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("first_name")
    @Expose
    public String firstName;
    @SerializedName("last_name")
    @Expose
    public String lastName;
    @SerializedName("picture")
    @Expose
    public String picture;
    @SerializedName("realname")
    @Expose
    public String realname;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPicture() {
        return WebAPI.ARTIST_PROFILE_PICTURE+picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

}
