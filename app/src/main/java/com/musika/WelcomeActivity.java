package com.musika;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.musika.baseclass.BaseAppCompactActivity;
import com.musika.databinding.ActivityWelcomeBinding;
import com.musika.utility.Utility;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WelcomeActivity extends BaseAppCompactActivity {
    private ActivityWelcomeBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_welcome);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.act_welcome_tv_login)
    public void emailClick() {
        Utility.navigationIntent(this, LoginActivity.class);
    }

    @OnClick(R.id.act_welcome_tv_sign_up)
    public void signUpClick() {
        Utility.navigationIntent(this, SignUpActivity.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
