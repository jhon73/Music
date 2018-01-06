package com.musika.retrofit.model;

import com.musika.retrofit.WebAPI;

/**
 * Created by sachin on 7/19/17.
 */



public class SearchData {

    private int idu;
    private String title;
    private String description;
    private String name;
    private String tag;
    private String type;
    private String picture;
    private String first_name;
    private String last_name;
    private String username;
    private String image;
    private String email;
    private String realname;

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return idu;
    }

    public void setId(int id) {
        this.idu = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return WebAPI.TRACK_FILE+name;
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

    public String getPicture() {
        return WebAPI.ARTIST_PROFILE_PICTURE+picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUsername() {

        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage() {
        return WebAPI.ARTIST_PROFILE_PICTURE+image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
