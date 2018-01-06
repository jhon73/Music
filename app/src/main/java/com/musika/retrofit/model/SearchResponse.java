package com.musika.retrofit.model;

import java.util.List;

/**
 * Created by sachin on 7/18/17.
 */

public class SearchResponse {
    private String message;
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    private List<SearchData> data;

    public List<SearchData> getSearchDatas() {
        return data;
    }

    public void setSearchDatas(List<SearchData> searchDatas) {
        this.data = searchDatas;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
