package com.musika.retrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.musika.retrofit.WebAPI;
import com.musika.retrofit.model.explore.Artist;

import fr.xebia.android.freezer.annotations.Id;
import fr.xebia.android.freezer.annotations.Model;

@Model
public class RecentPlayData {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("picture")
    @Expose
    public String picture;
    @SerializedName("likes")
    @Expose
    public String totallikes;
    @SerializedName("plays")
    @Expose
    public String totalplays;
    @SerializedName("share_url")
    @Expose
    public String share_url;
    @SerializedName("isLiked")
    @Expose
    public Boolean isLiked;
    @SerializedName("tag")
    @Expose
    public String tag;
    @SerializedName("artist")
    @Expose
    public Artist artist;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getTotallikes() {
        return totallikes;
    }

    public void setTotallikes(String totallikes) {
        this.totallikes = totallikes;
    }

    public String getTotalplays() {
        return totalplays;
    }

    public void setTotalplays(String totalplays) {
        this.totalplays = totalplays;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public Boolean getLiked() {
        return isLiked;
    }

    public void setLiked(Boolean liked) {
        isLiked = liked;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }
}