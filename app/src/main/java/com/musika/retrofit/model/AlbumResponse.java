package com.musika.retrofit.model;

/**
 * Created by sachin on 8/11/17.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.musika.retrofit.model.explore.AlbumData;

public class AlbumResponse {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("count")
    @Expose
    private Integer count;  //not consider by fetch albumById
    @SerializedName("data")
    @Expose
    private List<AlbumData> data = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<AlbumData> getData() {
        return data;
    }

    public void setData(List<AlbumData> data) {
        this.data = data;
    }

}