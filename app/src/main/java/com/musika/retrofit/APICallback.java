package com.musika.retrofit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.musika.LoginActivity;
import com.musika.R;
import com.musika.retrofit.model.MessageResponse;
import com.musika.utility.MyPref;
import com.musika.utility.Utility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class APICallback implements Callback {

    private int reqCode;
    private OnApiResponseListner onApiResponseListner;
    private Context context;

    public APICallback(Context context, int reqCode, OnApiResponseListner onApiResponseListner) {
        this.context = context;
        this.reqCode = reqCode;
        this.onApiResponseListner = onApiResponseListner;
    }

    @Override
    public void onResponse(Call call, Response response) {

        if (response.code() == APICall.Token_RES_CODE) {
            SharedPreferences.Editor editor = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE).edit();
            editor.putBoolean(MyPref.Keys.ISLOGIN.name(), false);
            editor.commit();
            context.startActivity(new Intent(context, LoginActivity.class));
            ((Activity) context).finish();

        } else if (response.errorBody() != null) {
            try {
                if (onApiResponseListner != null) {
                    MessageResponse messageResponse = new Gson().fromJson(response.errorBody().string(), MessageResponse.class);
                    onApiResponseListner.onResponseError(messageResponse, reqCode);
                    Utility.log(messageResponse.getMessage());
                }
            } catch (Exception e) {
                e.printStackTrace();
                onApiResponseListner.onResponseError("", reqCode);
            }

        } else {
            if (onApiResponseListner != null)
                onApiResponseListner.onResponseComplete(response.body(), reqCode);
        }
    }

    @Override
    public void onFailure(Call call, Throwable t) {
        try {
            if (onApiResponseListner != null) {
                if (t != null && t.getMessage().toLowerCase().contains("failed to connect"))
                    onApiResponseListner.onResponseError("Connection Problem", reqCode);
                else if (t != null)
                    onApiResponseListner.onResponseError(t.getMessage(), reqCode);
                else
                    onApiResponseListner.onResponseError("Something wrong", reqCode);
            }
        } catch (Exception ae) {
            ae.printStackTrace();
            onApiResponseListner.onResponseError("", reqCode);
        }
    }
}
