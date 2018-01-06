package com.musika.retrofit.model;

import com.musika.retrofit.model.explore.AlbumData;
import com.musika.retrofit.model.explore.Albums;
import com.musika.retrofit.model.explore.Artist;

import java.util.List;

public class SuggestedData {

            private int id;
            private String title;
            private String name;
            private String tag;
            private Artist artist;
            private AlbumData album;


            public int getId() {
                return id;
            }

            public void setId(int id) {
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

            public Artist getArtist() {
                return artist;
            }

            public void setArtist(Artist artist) {
                this.artist = artist;
            }

            public AlbumData getAlbum() {
                return album;
            }

            public void setAlbum(AlbumData album) {
                this.album = album;
            }

}