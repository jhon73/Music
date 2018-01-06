
package com.musika.retrofit.model.explore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExploreResponse {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ExploreData data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ExploreData getData() {
        return data;
    }

    public void setData(ExploreData data) {
        this.data = data;
    }

}
