package com.musika.retrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.musika.retrofit.WebAPI;
import com.musika.retrofit.model.Track;
import com.musika.retrofit.model.explore.AlbumData;
import com.musika.retrofit.model.explore.Artist;

import java.util.List;

public class AlbumByIdData {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("artist")
    @Expose
    private Artist artist;
    @SerializedName("tracks")
    @Expose
    private List<Track> tracks = null;
    @SerializedName("other_albums")
    @Expose
    private List<AlbumData> otherAlbums = null;

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

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public List<AlbumData> getOtherAlbums() {
        return otherAlbums;
    }

    public void setOtherAlbums(List<AlbumData> otherAlbums) {
        this.otherAlbums = otherAlbums;
    }

}