package com.musika;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;

import com.example.yudiz.wheel.ArrayWheelAdapter;
import com.example.yudiz.wheel.OnWheelChangedListener;
import com.example.yudiz.wheel.WheelView;
import com.musika.baseclass.BaseAppCompactActivity;
import com.musika.databinding.ActivitySignUpBinding;
import com.musika.helper.SlideAnimation;
import com.musika.retrofit.APICall;
import com.musika.retrofit.APICallback;
import com.musika.retrofit.OnApiResponseListner;
import com.musika.retrofit.model.MailVerifiedResponse;
import com.musika.retrofit.model.MessageResponse;
import com.musika.retrofit.model.SignUpResponse;
import com.musika.utility.Utility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

public class SignUpActivity extends BaseAppCompactActivity implements View.OnTouchListener, OnApiResponseListner, TextWatcher, OnWheelChangedListener {


    private ActivitySignUpBinding mBinding;
    private boolean isMale = true;
    private int date;
    private Call<?> callRegister, callEmail;
    private boolean isAvailable;
    private ArrayWheelAdapter mAdapter;
    private boolean isExpand;
    private boolean isPasswordVisible;
    private AnimatorSet set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        ButterKnife.bind(this);
        mBinding.activitySignUp.setOnTouchListener(this);
        mBinding.activitySignUpSv.setOnTouchListener(this);
        mBinding.actSignUpEdMail.addTextChangedListener(this);
        setUpWheel();
    }



    private void setUpWheel() {
        List<String> age = new ArrayList<>();
        for (int i = 10; i < 100; i++) {
            age.add(i + "");
        }
        mAdapter = new ArrayWheelAdapter<>(this, R.layout.wheel_picker_row, R.id.wheel_picker_row_tv, new ArrayList<String>());
        mAdapter.setList(age);
        mBinding.actSignUpWheelAge.setViewAdapter(mAdapter);
        mBinding.actSignUpWheelAge.addChangingListener(this);

    }


    @OnClick(R.id.act_sign_up_wheel_age_view)
    public void wheelClose() {
        Animation animation;
        if (isExpand) {
            animation = new SlideAnimation(mBinding.actSignUpRlAge, (int) getResources().getDimension(R.dimen._110sdp), 0);
            mBinding.activitySignUpSv.setScrollingEnabled(true);
        } else {
            animation = new SlideAnimation(mBinding.actSignUpRlAge, 0, (int) getResources().getDimension(R.dimen._110sdp));
            mBinding.activitySignUpSv.setScrollingEnabled(false);
        }

        isExpand = !isExpand;
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setDuration(300);
        mBinding.actSignUpRlAge.setAnimation(animation);
        mBinding.actSignUpRlAge.startAnimation(animation);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    protected void onStop() {
        super.onStop();
        if (callRegister != null && !callRegister.isCanceled())
            callRegister.cancel();
        if (callEmail != null && !callEmail.isCanceled())
            callEmail.cancel();
        if (runnable != null) {
            mBinding.getRoot().removeCallbacks(runnable);
        }
    }

    @OnClick(R.id.act_tv_sign_up)
    public void signUpClick() {
        if ((Utility.doubleTapTime + Utility.CLICK_INTERVAL) < System.currentTimeMillis()) {
            Utility.doubleTapTime = System.currentTimeMillis();

            if (isValid()) {
                if (Utility.haveNetworkConnection(this)) {
                    showDialog();
                    callRegister = ((MusicApp) getApplication()).getApiTask().signUp(
                            mBinding.actSignUpEdFname.getText().toString().trim(),
                            mBinding.actSignUpEdLname.getText().toString().trim(),
                            mBinding.actSignUpEdMobile.getText().toString().trim(),
                            date, isMale ? "male" : "female",
                            mBinding.actSignUpEdMail.getText().toString().trim(),
                            mBinding.actSignUpEdPassword.getText().toString().trim(),
                            new APICallback(this, APICall.REGISTER_REQ_CODE, this));
                } else {
                    Utility.showRedSnackBar(this, getString(R.string.no_internet_connection));
                }
            }
        }
    }

    private void checkEmail() {

        if (Utility.haveNetworkConnection(this)) {
            mBinding.actSignRegProgress.setVisibility(View.VISIBLE);
            mBinding.actSignRegSuccess.setVisibility(View.GONE);
            mBinding.actSignRegFail.setVisibility(View.GONE);
            if (callEmail != null && !callEmail.isCanceled())
                callEmail.cancel();
            callEmail = ((MusicApp) getApplication()).getApiTask().checkEmail(
                    mBinding.actSignUpEdMail.getText().toString().trim(),
                    new APICallback(this, APICall.CHECK_MAIL, this));

        } else {
            mBinding.actSignRegProgress.setVisibility(View.GONE);
            mBinding.actSignRegSuccess.setVisibility(View.GONE);
            mBinding.actSignRegFail.setVisibility(View.GONE);
            Utility.showRedSnackBar(this, getString(R.string.no_internet_connection));

        }

    }

    @OnClick(R.id.act_sign_up_ll_male)
    public void maleClick(View view) {

        if (!isMale) {
            isMale = true;
            mBinding.actSignUpIvMale.setVisibility(View.VISIBLE);
            mBinding.actSignUpIvFemale.setVisibility(View.GONE);
        }

    }

    @OnClick(R.id.act_sign_up_ll_age)
    public void ageClick() {
        if (mBinding.actSignUpTvAge.getText().toString().equals(""))
            mBinding.actSignUpTvAge.setText(10 + " years");

        Animation animation;
        if (isExpand) {
            animation = new SlideAnimation(mBinding.actSignUpRlAge, (int) getResources().getDimension(R.dimen._110sdp), 0);
            mBinding.activitySignUpSv.setScrollingEnabled(true);
        } else {
            animation = new SlideAnimation(mBinding.actSignUpRlAge, 0, (int) getResources().getDimension(R.dimen._110sdp));
            mBinding.activitySignUpSv.setScrollingEnabled(false);
        }

        isExpand = !isExpand;
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setDuration(300);
        mBinding.actSignUpRlAge.setAnimation(animation);
        mBinding.actSignUpRlAge.startAnimation(animation);

    }


    @OnClick(R.id.activity_sign_up_iv_back)
    public void onBackPress() {
        onBackPressed();
    }

    @OnClick(R.id.act_sign_up_password)
    public void passwordClick() {
        if (isPasswordVisible) {
            set = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.password_anim_reverse);
            set.setTarget(mBinding.actSignUpPassword);
            set.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    isPasswordVisible = false;
                    mBinding.actSignUpPassword.setImageResource(R.drawable.ic_hidden_eye);
                    mBinding.actSignUpEdPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mBinding.actSignUpEdPassword.setSelection(mBinding.actSignUpEdPassword.getText().toString().length());

                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            set.start();


        } else {
            set = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.password_anim);
            set.setTarget(mBinding.actSignUpPassword);
            set.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    isPasswordVisible = true;
                    mBinding.actSignUpPassword.setImageResource(R.drawable.ic_eye);
                    mBinding.actSignUpEdPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mBinding.actSignUpEdPassword.setSelection(mBinding.actSignUpEdPassword.getText().toString().length());

                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            set.start();
        }
    }

    private boolean isValid() {

        if (mBinding.actSignUpEdFname.getText().toString().trim().length() == 0) {
            Utility.showRedSnackBar(this, getString(R.string.msg_first_name));
            return false;
        }
        if (mBinding.actSignUpEdLname.getText().toString().trim().length() == 0) {
            Utility.showRedSnackBar(this, getString(R.string.msg_last_name));
            return false;
        }
        if (mBinding.actSignUpEdMobile.getText().toString().trim().length() == 0) {
            Utility.showRedSnackBar(this, getString(R.string.msg_mobile));
            return false;
        }
        if (mBinding.actSignUpTvAge.getText().toString().trim().length() == 0) {
            Utility.showRedSnackBar(this, getString(R.string.msg_age));
            return false;
        }
        if (mBinding.actSignUpEdMail.getText().toString().trim().length() == 0) {
            Utility.showRedSnackBar(this, getString(R.string.msg_email));
            return false;
        }
        if (!Utility.isValidEmaillId(mBinding.actSignUpEdMail.getText().toString().trim())) {
            Utility.showRedSnackBar(this, getString(R.string.msg_valid_email));
            return false;
        }
        if (!isAvailable) {
            Utility.showRedSnackBar(this, getString(R.string.msg_already_exist));
            return false;

        }

        if (mBinding.actSignUpEdPassword.getText().toString().trim().length() == 0) {
            Utility.showRedSnackBar(this, getString(R.string.msg_password));
            return false;
        }

        return true;
    }

    @OnClick(R.id.act_sign_up_ll_female)
    public void femaleClick() {
        if (isMale) {
            isMale = false;
            mBinding.actSignUpIvMale.setVisibility(View.GONE);
            mBinding.actSignUpIvFemale.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        mBinding.activitySignUpSv.setScrollingEnabled(true);
        Utility.hideKeyboared(this);
        return false;
    }

    @Override
    public void onResponseComplete(Object clsGson, int requestCode) {
        if (requestCode == APICall.REGISTER_REQ_CODE) {
            dismissDialog();
            if (clsGson instanceof SignUpResponse) {
                SignUpResponse signUpResponse = (SignUpResponse) clsGson;
                Utility.showRedSnackBar(this, signUpResponse.getMessage());
                mBinding.getRoot().postDelayed(runnable, 1200);
            }
        }

        if (requestCode == APICall.CHECK_MAIL) {
            if (clsGson instanceof MailVerifiedResponse) {
                MailVerifiedResponse mailVerifiedResponse = (MailVerifiedResponse) clsGson;
                if (mailVerifiedResponse.isAvailable()) {
                    mBinding.actSignRegProgress.setVisibility(View.GONE);
                    mBinding.actSignRegFail.setVisibility(View.GONE);
                    mBinding.actSignRegSuccess.setVisibility(View.VISIBLE);
                    isAvailable = true;
                    Utility.showRedSnackBar(this, mailVerifiedResponse.getMessage());
                } else {
                    mBinding.actSignRegProgress.setVisibility(View.GONE);
                    mBinding.actSignRegFail.setVisibility(View.VISIBLE);
                    mBinding.actSignRegSuccess.setVisibility(View.GONE);
                    isAvailable = false;
                    Utility.showRedSnackBar(this, mailVerifiedResponse.getMessage());
                }
            }
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            ActivityCompat.finishAffinity(SignUpActivity.this);
        }
    };

    @Override
    public void onResponseError(Object errorMessage, int requestCode) {
        dismissDialog();

        if (!errorMessage.equals("Socket closed") && requestCode == APICall.CHECK_MAIL) {
            mBinding.actSignRegProgress.setVisibility(View.GONE);
            mBinding.actSignRegFail.setVisibility(View.GONE);
            mBinding.actSignRegSuccess.setVisibility(View.GONE);
        }
        if (errorMessage instanceof MessageResponse) {
            MessageResponse messageResponse = (MessageResponse) errorMessage;
            Utility.showRedSnackBar(this, messageResponse.getMessage());
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        if (Utility.isValidEmaillId(charSequence.toString().trim())) {
            checkEmail();
        } else {
            mBinding.actSignRegProgress.setVisibility(View.GONE);
            mBinding.actSignRegSuccess.setVisibility(View.GONE);
            mBinding.actSignRegFail.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        mBinding.actSignUpTvAge.setText((newValue + 10) + " years");
        date = newValue+10;
    }
}
