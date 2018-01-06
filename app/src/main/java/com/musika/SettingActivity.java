package com.musika;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.musika.baseclass.BaseAppCompactActivity;
import com.musika.databinding.ActivitySettingBinding;
import com.musika.retrofit.APICall;
import com.musika.retrofit.APICallback;
import com.musika.retrofit.OnApiResponseListner;
import com.musika.retrofit.model.MessageResponse;
import com.musika.utility.Utility;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;


public class SettingActivity extends BaseAppCompactActivity implements OnApiResponseListner {

    private ActivitySettingBinding mBinding;
    private Call<?> callLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
        ButterKnife.bind(this, mBinding.getRoot());
        if (getMyPref().getProfileData() != null && getMyPref().getProfileData().getFirstName() != null) {
            if (getMyPref().getProfileData().getLastName() != null)
                mBinding.tvSignOut.setText("Sign out (" + getMyPref().getProfileData().getFirstName() + " " + getMyPref().getProfileData().getLastName() + ")");
            else
                mBinding.tvSignOut.setText("Sign out (" + getMyPref().getProfileData().getFirstName() + ")");

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (callLogout != null && !callLogout.isCanceled())
            callLogout.cancel();
    }

    @OnClick(R.id.act_setting_rl_change_password)
    public void changePassword() {
        Utility.navigationIntent(SettingActivity.this, ChangePasswordActivity.class);
    }

    @OnClick(R.id.act_setting_close)
    public void close() {
        onBackPressed();
    }

    @OnClick(R.id.act_setting_rl_sign_out)
    public void logout() {
        if (Utility.haveNetworkConnection(this)) {
            callLogout = ((MusicApp) getApplication()).getApiTask().logout(new APICallback(this, APICall.LOGOUT_REQ_CODE, this));
        } else {
            Utility.showRedSnackBar(this, getString(R.string.no_internet_connection));
        }
    }

    @Override
    public void onResponseComplete(Object clsGson, int requestCode) {
        if (requestCode == APICall.LOGOUT_REQ_CODE) {
            if (clsGson instanceof MessageResponse) {
                MessageResponse logout = (MessageResponse) clsGson;
                Utility.showRedSnackBar(this, logout.getMessage());
                mBinding.getRoot().postDelayed(runnable, 1000);
            }
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Utility.navigationIntent(SettingActivity.this, WelcomeActivity.class);

        }
    };

    @Override
    public void onResponseError(Object errorMessage, int requestCode) {
        if (errorMessage instanceof MessageResponse) {
            MessageResponse messageResponse = (MessageResponse) errorMessage;
            Utility.showRedSnackBar(this, messageResponse.getMessage());
        }
    }
}
