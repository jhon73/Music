
package com.musika.retrofit.model.explore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.musika.retrofit.WebAPI;
import com.musika.retrofit.model.suggested.ArtistOfAlbum;

import fr.xebia.android.freezer.annotations.Model;

@Model
public class AlbumData {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("image")
    @Expose
    public String image;

    @SerializedName("tag")
    @Expose
    public String tag;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @SerializedName("title")
    @Expose
    public String title;
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @SerializedName("artist")
    @Expose
    public Artist artist;

    @SerializedName("artits")
    @Expose
    public ArtistOfAlbum artists;

    public Integer getId() {
        return id;
    }

    public ArtistOfAlbum getArtists() {
        return artists;
    }

    public void setArtists(ArtistOfAlbum artists) {
        this.artists = artists;
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
