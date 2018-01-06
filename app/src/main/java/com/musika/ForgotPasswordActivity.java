package com.musika;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import com.musika.databinding.ActivityForgetPasswordBinding;
import com.musika.utility.Utility;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnTouchListener {
    private ActivityForgetPasswordBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_forget_password);
        ButterKnife.bind(this);
        mBinding.activityForgetPasswordMain.setOnTouchListener(this);
    }

    @OnClick(R.id.activity_forget_password_iv_back)
    public void onBackPress() {
        onBackPressed();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Utility.hideKeyboared(ForgotPasswordActivity.this);
        return false;
    }
}
