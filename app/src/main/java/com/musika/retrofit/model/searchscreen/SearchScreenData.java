package com.musika.retrofit.model.searchscreen;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchScreenData {

@SerializedName("tracks")
@Expose
private Tracks tracks;
@SerializedName("generes")
@Expose
private Generes generes;

public Tracks getTracks() {
return tracks;
}

public void setTracks(Tracks tracks) {
this.tracks = tracks;
}

public Generes getGeneres() {
return generes;
}

public void setGeneres(Generes generes) {
this.generes = generes;
}

}
