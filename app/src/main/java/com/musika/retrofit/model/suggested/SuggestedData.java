package com.musika.retrofit.model.suggested;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.musika.retrofit.WebAPI;

public class SuggestedData {

@SerializedName("id")
@Expose
private Integer id;
@SerializedName("title")
@Expose
private String title;
@SerializedName("name")
@Expose
private String name;
@SerializedName("tag")
@Expose
private String tag;

    @SerializedName("picture")
    @Expose
    private String picture;
    @SerializedName("likes")
    @Expose
    private String likes;
    @SerializedName("plays")
    @Expose
    private String plays;
    @SerializedName("share_url")
    @Expose
    private String share_url;
    @SerializedName("isLiked")
    @Expose
    private boolean isLiked;

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getPlays() {
        return plays;
    }

    public void setPlays(String plays) {
        this.plays = plays;
    }

    public String getShare_url() {
        return WebAPI.SHARE_URL+share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    @SerializedName("artist")
@Expose
private SuggestedArtist artist;

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

public String getTag() {
return tag;
}

public void setTag(String tag) {
this.tag = tag;
}

public SuggestedArtist getArtist() {
return artist;
}

public void setArtist(SuggestedArtist artist) {
this.artist = artist;
}

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}