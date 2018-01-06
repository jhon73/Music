package com.musika;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.musika.baseclass.BaseAppCompactActivity;
import com.musika.databinding.ActivityLoginBinding;
import com.musika.retrofit.APICall;
import com.musika.retrofit.APICallback;
import com.musika.retrofit.OnApiResponseListner;
import com.musika.retrofit.model.MessageResponse;
import com.musika.retrofit.model.SignUpResponse;
import com.musika.retrofit.model.SocialTwitterResponse;
import com.musika.utility.MyPref;
import com.musika.utility.Utility;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

public class LoginActivity extends BaseAppCompactActivity implements View.OnTouchListener, OnApiResponseListner {

    private Twitter twitter;
    private RequestToken requestToken = null;
    private AccessToken accessToken;
    private String oauth_url,oauth_verifier,profile_url;
    private Dialog auth_dialog;
    private WebView web;
    private String full_name,phone,email,dob,gender,device_token,image_URL;
    private long id;
    private ProgressDialog progress;
    private Bitmap bitmap;
    private Context context;


    private ActivityLoginBinding mBinding;
    private boolean isPasswordVisible;
    private AnimatorSet set;
    private Call<?> callLogin,callTwitter;
    private  String CONSUMER_KEY = "EPZ7CmPnQ4oBwRGj5n1iuAofy";
    private  String CONSUMER_SECRET ="xon3qYucPp9t3aDeNQj7ISojU5ja6vBIVupoJ6unmSqUvzAipz";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        ButterKnife.bind(this);
        mBinding.activityLogin.setOnTouchListener(this);
        mBinding.activitySvLogin.setOnTouchListener(this);

        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(CONSUMER_KEY);
        builder.setOAuthConsumerSecret(CONSUMER_SECRET);
        builder.setIncludeEmailEnabled(true);

        TwitterFactory twitterCon= new TwitterFactory(builder.build());
        twitter=twitterCon.getInstance();
    }

    @OnClick(R.id.act_login_tv_forget_paasword)
    public void forgetPassword() {
        Utility.navigationIntentWithTransitionEffect(this, ForgotPasswordActivity.class, mBinding.actTvLogin);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick(R.id.act_login_tv_create_account)
    public void createAccount() {

        Utility.navigationIntentWithTransitionEffect(this, SignUpActivity.class, mBinding.actTvLogin);
    }

    @OnClick(R.id.activity_login_iv_back)
    public void onBackPress() {
        onBackPressed();
    }

    @OnClick(R.id.act_tv_login)
    public void login() {
        if ((Utility.doubleTapTime + Utility.CLICK_INTERVAL) < System.currentTimeMillis()) {
            Utility.doubleTapTime = System.currentTimeMillis();
            if (isValid()) {
                if (Utility.haveNetworkConnection(this)) {
                    showDialog();
                    callLogin = ((MusicApp) getApplication()).getApiTask().login(
                            mBinding.actLoginEdEmail.getText().toString().trim(),
                            mBinding.actLoginEdPassword.getText().toString().trim(),
                            new APICallback(this, APICall.LOGIN_REQ_CODE, this));
                } else {
                    Utility.showRedSnackBar(this, getString(R.string.no_internet_connection));
                }
            }
        }
    }

    private boolean isValid() {
        if (mBinding.actLoginEdEmail.getText().toString().trim().length() == 0) {
            Utility.showRedSnackBar(this, getString(R.string.msg_email));
            return false;
        }

        if (!Utility.isValidEmaillId(mBinding.actLoginEdEmail.getText().toString().trim())) {
            Utility.showRedSnackBar(this, getString(R.string.msg_valid_email));
            return false;
        }

        if (mBinding.actLoginEdPassword.getText().toString().trim().length() == 0) {
            Utility.showRedSnackBar(this, getString(R.string.msg_password));
            return false;
        }

        return true;
    }

    @OnClick(R.id.act_login_iv_password)
    public void passwordClick() {
        if (isPasswordVisible) {
            set = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.password_anim_reverse);
            set.setTarget(mBinding.actLoginIvPassword);
            set.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    isPasswordVisible = false;
                    mBinding.actLoginIvPassword.setImageResource(R.drawable.ic_eye);
                    mBinding.actLoginEdPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mBinding.actLoginEdPassword.setSelection(mBinding.actLoginEdPassword.getText().toString().length());

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
            set.setTarget(mBinding.actLoginIvPassword);
            set.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    isPasswordVisible = true;
                    mBinding.actLoginIvPassword.setImageResource(R.drawable.ic_eye);
                    mBinding.actLoginEdPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mBinding.actLoginEdPassword.setSelection(mBinding.actLoginEdPassword.getText().toString().length());
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

    @OnClick(R.id.act_welcome_ll_twitter)
    public void twitterlClick(){
        new TokenGet().execute();
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (callLogin != null && !callLogin.isCanceled())
            callLogin.cancel();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Utility.hideKeyboared(LoginActivity.this);
        return false;
    }

    @Override
    public void onResponseComplete(Object clsGson, int requestCode) {
        if (requestCode == APICall.LOGIN_REQ_CODE) {
            dismissDialog();
            if (clsGson instanceof SignUpResponse) {
                SignUpResponse signUpResponse = (SignUpResponse) clsGson;
                getMyPref().setData(MyPref.Keys.TOKEN, signUpResponse.getToken());
                getMyPref().setData(MyPref.Keys.ISLOGIN, true);
                ((MusicApp) getApplication()).refreshToken();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                ActivityCompat.finishAffinity(LoginActivity.this);

            }
        }
        if (requestCode == APICall.SOCIAL_TWITTER) {
            if (clsGson instanceof SocialTwitterResponse) {
                SocialTwitterResponse socialTwitterResponse = (SocialTwitterResponse) clsGson;
                getMyPref().setData(MyPref.Keys.TOKEN, socialTwitterResponse.getToken());
                getMyPref().setData(MyPref.Keys.ISLOGIN, true);
                ((MusicApp) getApplication()).refreshToken();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                ActivityCompat.finishAffinity(LoginActivity.this);

            }
        }
    }

    @Override
    public void onResponseError(Object errorMessage, int requestCode) {
        dismissDialog();
        if (errorMessage instanceof MessageResponse) {
            MessageResponse messageResponse = (MessageResponse) errorMessage;
            Utility.showRedSnackBar(this, messageResponse.getMessage());
        }
    }

    //Login with Twitter
    private class TokenGet extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... args) {
            try {
                requestToken = twitter.getOAuthRequestToken();
                oauth_url = requestToken.getAuthorizationURL();
            } catch (TwitterException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return oauth_url;
        }
        @Override
        protected void onPostExecute(String oauth_url) {
            if(oauth_url != null){
                Log.e("URL", oauth_url);
                auth_dialog = new Dialog(LoginActivity.this);
                auth_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                auth_dialog.setContentView(R.layout.auth_dialog);
                web = (WebView)auth_dialog.findViewById(R.id.webv);
                web.getSettings().setJavaScriptEnabled(true);
                web.loadUrl(oauth_url);
                web.setWebViewClient(new WebViewClient() {
                    boolean authComplete = false;
                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon){
                        super.onPageStarted(view, url, favicon);
                    }
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        if (url.contains("oauth_verifier") && authComplete == false){
                            authComplete = true;
                            Uri uri = Uri.parse(url);
                            oauth_verifier = uri.getQueryParameter("oauth_verifier");
                            auth_dialog.dismiss();
                            new AccessTokenGet().execute();
                        }else if(url.contains("denied")){
                            auth_dialog.dismiss();
                            Utility.toast(LoginActivity.this, "Sorry !, Permission Denied");
                        }
                    }
                });
                auth_dialog.show();
                auth_dialog.setCancelable(true);
            }else{
                Utility.showErrorSnackBar(mBinding.getRoot(),"Sorry !, Network Error or Invalid Credentials");
            }
        }
    }
    private class AccessTokenGet extends AsyncTask<String, String, Boolean>  {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(LoginActivity.this);
            progress.setCancelable(false);
            progress.setMessage("Fetching Data ...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progress.setIndeterminate(true);
            progress.show();
        }
        @Override
        protected Boolean doInBackground(String... args) {
            try {
                accessToken = twitter.getOAuthAccessToken(requestToken, oauth_verifier);
                User user = twitter.verifyCredentials();
                id = user.getId();
                full_name = user.getName();
                email = user.getEmail();
                phone = user.getProfileBannerMobileURL();
                dob = user.getCreatedAt().toString();
                image_URL = user.getOriginalProfileImageURL();

            } catch (TwitterException e) {
                e.printStackTrace();
            }
            return true;
        }
        @Override
        protected void onPostExecute(Boolean response) {
            if (response) {
                progress.hide();
                if (Utility.haveNetworkConnection(LoginActivity.this)) {
                    if(image_URL == null) {
                        callTwitter = ((MusicApp) getApplication()).getApiTask().social_twitter(
                                // "" ->               dob, gender, device_token
                                id, "twt", full_name==null?"":full_name, phone==null?"":phone,"","","",email==null?"":email, new APICallback(LoginActivity.this, APICall.SOCIAL_TWITTER, LoginActivity.this));
                    }
                    else {
                        new DownloadImage().execute(Environment.getExternalStorageDirectory().getAbsolutePath() + "/profile.png");
                    }
                } else {
                    Utility.showErrorSnackBar(mBinding.getRoot(), getString(R.string.no_internet_connection));
                }
            }
        }
    }
    public String DownloadFromUrl(String fileName) {
        try {
            URL url = new URL(image_URL); //you can write here any link
            File file = new File(fileName);
            if (!file.exists())
                file.createNewFile();
            InputStream input = new BufferedInputStream(url.openStream());
            OutputStream output = new FileOutputStream(file);
            byte data[] = new byte[1024];
            int count = 0;
            while ((count = input.read(data)) != -1) {
                output.write(data, 0, count);
            }
            output.flush();
            output.close();
            input.close();
            return fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
    private  class DownloadImage extends AsyncTask <String,String,String>  {
        @Override
        protected String doInBackground(String... params) {
            try {
                return DownloadFromUrl(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }
        @Override
        protected void onPostExecute(String image) {
            super.onPostExecute(image);
            try {
                if (image.equals(""))
                    image_URL = "";
                ((MusicApp) getApplication()).getApiTask().social_twitter(
                        // "" ->               dob, gender, device_token
                        id, "twt", full_name==null?"":full_name, phone==null?"":phone,"","","",email==null?"":email,image,new APICallback(LoginActivity.this, APICall.SOCIAL_TWITTER,LoginActivity.this));
            } catch (Exception ae) {
                ae.printStackTrace();
            }
        }
    }
}
