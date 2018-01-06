package com.musika.retrofit.model;

import java.util.List;

/**
 * Created by sparken02 on 26/7/17.
 */

public class SuggestedResponse {


    private String message;
    private List<SuggestedDataBean> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<SuggestedDataBean> getData() {
        return data;
    }

    public void setData(List<SuggestedDataBean> data) {
        this.data = data;
    }


}
