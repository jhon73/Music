
package com.musika.retrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.musika.retrofit.WebAPI;

import java.util.List;

import fr.xebia.android.freezer.annotations.Model;

@Model
public class ArtistData {

    @SerializedName("idu")
    @Expose
    public Integer idu;
    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("first_name")
    @Expose
    public String firstName;

    @SerializedName("realname")
    @Expose
    public String realname;
    public int pos;

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    @SerializedName("last_name")
    @Expose
    public String lastName;


    @SerializedName("image")
    @Expose
    public String image;
    @SerializedName("cover")
    @Expose
    public String cover;
    @SerializedName("verfied")
    @Expose
    public String verfied;
    @SerializedName("followers_count")
    @Expose
    public Integer followersCount;
    @SerializedName("plays_count")
    @Expose
    public Integer playsCount;
    @SerializedName("isFollowing")
    @Expose
    public Boolean isFollowing;
    @SerializedName("tracks")
    @Expose
    public List<Track> tracks = null;

    public Integer getIdu() {
        return idu;
    }

    public void setIdu(Integer idu) {
        this.idu = idu;
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

    public String getVerfied() {
        return verfied;
    }

    public void setVerfied(String verfied) {
        this.verfied = verfied;
    }

    public String getFollowersCount() {


        return followersCount + "";
    }
    public int getFollowersCounts() {


        return followersCount;
    }
    public String incrementFollower(int val) {
        followersCount=followersCount+val;
        return followersCount + "";
    }

    public String getFollowersCountText() {
        if (followersCount == 0)
            return "Follower";

        return "Followers";
    }

    public void setFollowersCount(Integer followersCount) {
        this.followersCount = followersCount;
    }

    public String getPlaysCount() {

        return playsCount + "";
    }

    public String getPlaysCountText() {
        if (playsCount == 0)
            return "Play";

        return "Plays";
    }

    public void setPlaysCount(Integer playsCount) {
        this.playsCount = playsCount;
    }

    public Boolean getIsFollowing() {
        return isFollowing;
    }

    public void setIsFollowing(Boolean isFollowing) {
        this.isFollowing = isFollowing;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getPos() {
        return pos;
    }
}
