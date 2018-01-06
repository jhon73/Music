package com.musika.retrofit.model.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.musika.retrofit.WebAPI;

public class ArtistData {

@SerializedName("idu")
@Expose
private Integer id;
@SerializedName("username")
@Expose
private String username;
@SerializedName("first_name")
@Expose
private String firstName;
@SerializedName("last_name")
@Expose
private String lastName;
@SerializedName("realname")
@Expose
private String realname;
@SerializedName("image")
@Expose
private String image;
@SerializedName("cover")
@Expose
private String cover;
@SerializedName("isFollowing")
@Expose
private Boolean isFollowing;
@SerializedName("followers_count")
@Expose
private Integer followersCount;

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
return cover;
}

public void setCover(String cover) {
this.cover = cover;
}

public Boolean getIsFollowing() {
return isFollowing;
}

public void setIsFollowing(Boolean isFollowing) {
this.isFollowing = isFollowing;
}


public void setFollowersCount(Integer followersCount) {
this.followersCount = followersCount;
}
    public void toggleFollowing() {
        this.isFollowing = !isFollowing;
    }
    public String getFollowersCount() {
        if (followersCount <2)
            return followersCount + " Follower";

        return followersCount + " Followers";
    }
    public void updateCount(int val) {
        followersCount=followersCount+val;
    }



}