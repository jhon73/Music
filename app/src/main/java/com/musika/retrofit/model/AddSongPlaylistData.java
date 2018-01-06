package com.musika.retrofit.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddSongPlaylistData {

@SerializedName("playlist_id")
@Expose
private Integer playlistId;
@SerializedName("playlist_name")
@Expose
private String playlistName;
@SerializedName("tracks")
@Expose
private List<Track> tracks = null;

public Integer getPlaylistId() {
return playlistId;
}

public void setPlaylistId(Integer playlistId) {
this.playlistId = playlistId;
}

public String getPlaylistName() {
return playlistName;
}

public void setPlaylistName(String playlistName) {
this.playlistName = playlistName;
}

public List<Track> getTracks() {
return tracks;
}

public void setTracks(List<Track> tracks) {
this.tracks = tracks;
}

}