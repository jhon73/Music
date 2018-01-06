package com.musika.baseclass;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.musika.utility.MyPref;


/**
 * Created by Yudiz on 22/11/16.
 */
abstract public class BaseFragment extends Fragment {
    BaseAppCompactActivity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (BaseAppCompactActivity) getActivity();
    }


    protected void log(String msg) {
        activity.log(msg);
    }

    protected void toast(String msg) {
        activity.toast(msg);
    }

//    protected void showDialog() {
//        activity.showDialog();
//    }
//
//
//    protected void dismissDialog() {
//        activity.dismissDialog();
//    }



    protected MyPref getMypref() {
        return activity.getMyPref();
    }

    protected void showDialog() {
        activity.showDialog();
    }


    protected void dismissDialog() {
        activity.dismissDialog();
    }

}
