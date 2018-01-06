/*
 * 
 */

package com.musika.retrofit;

// TODO: Auto-generated Javadoc
/**
 * The Interface OnApiResponseListner.
 */
public interface OnApiResponseListner {
    
    /**
     * On response complete.
     *
     * @param clsGson the cls gson
     * @param requestCode the request code
     */
    public void onResponseComplete(Object clsGson, int requestCode);

    /**
     * On response error.
     *
     * @param errorMessage the error message
     * @param requestCode the request code
     */
    public void onResponseError(Object errorMessage, int requestCode);
}
