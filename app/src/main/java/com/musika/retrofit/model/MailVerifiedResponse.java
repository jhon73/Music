package com.musika.retrofit.model;

/**
 * Created by sachin on 7/19/17.
 */

public class MailVerifiedResponse {

    private boolean isAvailable;
    private String message;

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
