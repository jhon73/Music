package com.musika;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import com.musika.baseclass.BaseAppCompactActivity;
import com.musika.databinding.ActivitySplashBinding;
import com.musika.utility.MyPref;
import com.musika.utility.Utility;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashActivity extends BaseAppCompactActivity {
    private ActivitySplashBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        ButterKnife.bind(this);
        if (getMyPref().getData(MyPref.Keys.ISLOGIN, false)) {
            Utility.navigationIntent(this, MainActivity.class);
            finish();
        }
    }

    @OnClick(R.id.act_tv_lets_start)
    public void startActivity() {
        Utility.navigationIntentWithTransitionEffect(this, WelcomeActivity.class, mBinding.actTvLetsStart);
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
