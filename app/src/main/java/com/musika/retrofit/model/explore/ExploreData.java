
package com.musika.retrofit.model.explore;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExploreData {

    @SerializedName("sliders")
    @Expose
    private List<Slider> sliders = null;
    @SerializedName("artists")
    @Expose
    private Artists artists;
    @SerializedName("albums")
    @Expose
    private Albums albums;

    @SerializedName("other_row")
    @Expose
    private List<Albums> other_row;

    public List<Albums> getOther_row() {
        return other_row;
    }

    public void setOther_row(List<Albums> other_row) {
        this.other_row = other_row;
    }

    public List<Slider> getSliders() {
        return sliders;
    }

    public void setSliders(List<Slider> sliders) {
        this.sliders = sliders;
    }

    public Artists getArtists() {
        return artists;
    }

    public void setArtists(Artists artists) {
        this.artists = artists;
    }

    public Albums getAlbums() {
        return albums;
    }

    public void setAlbums(Albums albums) {
        this.albums = albums;
    }

}
