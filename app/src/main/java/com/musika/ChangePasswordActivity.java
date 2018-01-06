package com.musika;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.musika.baseclass.BaseAppCompactActivity;
import com.musika.databinding.ActivityChangePasswordBinding;
import com.musika.retrofit.APICall;
import com.musika.retrofit.APICallback;
import com.musika.retrofit.OnApiResponseListner;
import com.musika.retrofit.model.ChangePasswordResponse;
import com.musika.retrofit.model.MessageResponse;
import com.musika.utility.MyPref;
import com.musika.utility.Utility;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;


public class ChangePasswordActivity extends BaseAppCompactActivity implements View.OnTouchListener, OnApiResponseListner {

    private ActivityChangePasswordBinding mBinding;
    private Call<?> callGetCurrentPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_change_password);
        ButterKnife.bind(this);
        mBinding.fragmentChangePasswordMain.setOnTouchListener(this);
        mBinding.fragmentChangePasswordSv.setOnTouchListener(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (callGetCurrentPassword != null && !callGetCurrentPassword.isCanceled())
            callGetCurrentPassword.cancel();
    }

    @OnClick(R.id.act_download_iv_back)
    public void onBackPress() {
        onBackPressed();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Utility.hideKeyboared(ChangePasswordActivity.this);
        return false;
    }

    @OnClick(R.id.act_tv_save)
    public void changePassword() {
        if ((Utility.doubleTapTime + Utility.CLICK_INTERVAL) < System.currentTimeMillis()) {
            Utility.doubleTapTime = System.currentTimeMillis();
            if (isValid()) {
                if (Utility.haveNetworkConnection(this)) {
                    showDialog();
                    callGetCurrentPassword = ((MusicApp) getApplication()).getApiTask().changePassword(

                            mBinding.actEdCurrentPassword.getText().toString().trim(),
                            mBinding.actEdNewPassword.getText().toString().trim(),
                            new APICallback(this, APICall.CHANGE_PASSWORD_REQ_CODE, this));
                } else {
                    Utility.showRedSnackBar(this, getString(R.string.no_internet_connection));
                }
            }
        }
    }

    private boolean isValid() {
        if (mBinding.actEdCurrentPassword.getText().toString().trim().length() == 0) {
            Utility.showRedSnackBar(this, getString(R.string.msg_current_password));
            return false;
        }
        if (mBinding.actEdNewPassword.getText().toString().trim().length() == 0) {
            Utility.showRedSnackBar(this, getString(R.string.msg_new_password));
            return false;
        }
        if (mBinding.actEdConfirmNewPassword.getText().toString().trim().length() == 0) {
            Utility.showRedSnackBar(this, getString(R.string.msg_confirm_password));
            return false;
        }
        if (!mBinding.actEdNewPassword.getText().toString().trim().equals(mBinding.actEdConfirmNewPassword.getText().toString().trim())) {
            Utility.showRedSnackBar(this, getString(R.string.change_new_confirm_not_match));
            return false;
        }

        if (mBinding.actEdCurrentPassword.getText().toString().trim().equals(mBinding.actEdNewPassword.getText().toString().trim())) {
            Utility.showRedSnackBar(this, getString(R.string.change_current_new_match));
            return false;
        }
        return true;
    }


    @Override
    public void onResponseError(Object errorMessage, int requestCode) {
        dismissDialog();
        Utility.log("getResponseError call");
        Utility.log((String) errorMessage);
        if (errorMessage instanceof MessageResponse) {
            Utility.log("getResponseError call in if");
            MessageResponse messageResponse = (MessageResponse) errorMessage;
            Utility.showRedSnackBar(this, messageResponse.getMessage());
        }
    }

    @Override
    public void onResponseComplete(Object clsGson, int requestCode) {
        Utility.log("getResponse call");
        dismissDialog();
        if (requestCode == APICall.CHANGE_PASSWORD_REQ_CODE) {
            if (clsGson instanceof ChangePasswordResponse) {
                ChangePasswordResponse changePasswordResponse = (ChangePasswordResponse) clsGson;
                getMyPref().setData(MyPref.Keys.TOKEN, changePasswordResponse.getToken());
                ((MusicApp) getApplication()).refreshToken();
                Utility.showRedSnackBar(this, changePasswordResponse.getMessage());
                mBinding.getRoot().postDelayed(runnable, 1000);
            }
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Utility.navigationIntent(ChangePasswordActivity.this, MainActivity.class);
        }
    };
}
