package com.musika.retrofit.model;

import java.util.List;

public class SuggestedDataBean {

        private String title;
        private String type;
        private List<SuggestedData> data;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<SuggestedData> getData() {
            return data;
        }

        public void setData(List<SuggestedData> data) {
            this.data = data;
        }

       
    }