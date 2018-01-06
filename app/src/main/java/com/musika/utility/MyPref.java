package com.musika.utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.musika.R;
import com.musika.retrofit.ProfileData;


/**
 * Created by sachin on 6/9/17.
 */

public class MyPref {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public MyPref(Context context) {
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setData(Keys keys, String value) {
        editor.putString(keys.name(), value);
        editor.commit();
    }

    public void setData(Keys keys, boolean value) {
        editor.putBoolean(keys.name(), value);
        editor.commit();
    }

    public String getData(Keys keys) {
        return sharedPreferences.getString(keys.name(), "");
    }

    public boolean getData(Keys keys, boolean flag) {
        return sharedPreferences.getBoolean(keys.name(), flag);
    }



    public void setProfileData(ProfileData userData) {
        editor.putString(Keys.PROFILE.name(), new Gson().toJson(userData));
        editor.commit();
    }
    public ProfileData getProfileData() {
        String data = sharedPreferences.getString(Keys.PROFILE.name(), "");
        if (!data.equals(""))
            return new Gson().fromJson(data, ProfileData.class);
        else
            return new ProfileData();
    }
    public enum Keys {
        ISLOGIN, ISTC, UserDataEnum, IMAGEURL, ISNOTIFICATION, ISINVISIBLE, USERDATA, TOKEN, PROFILE;
    }
}
