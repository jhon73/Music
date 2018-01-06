package com.musika;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.SeekBar;

import com.github.ppamorim.dragger.DraggerPosition;
import com.musika.adapter.PlayerViewPagerAdapter;
import com.musika.databinding.FragmentPlayerBinding;
import com.musika.fragment.PlayerActivityItemFragment;
import com.musika.helper.MyBounceInterpolator;
import com.musika.retrofit.APICall;
import com.musika.retrofit.APICallback;
import com.musika.retrofit.OnApiResponseListner;
import com.musika.retrofit.model.MessageResponse;
import com.musika.retrofit.model.common.MyTrack;
import com.musika.utility.Utility;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

public class PlayerActivity extends AppCompatActivity implements View.OnTouchListener, OnApiResponseListner, MusicService.OnSongChangeListener, ViewPager.OnPageChangeListener {

    private Context context;
    private FragmentPlayerBinding mBinding;
    private AudioManager audioManager = null;
    private MusicService musicSrv;
    private Intent playIntent;
    private List<MyTrack> myTracks;
    private List<Fragment> myTracksFragment;
    private int pos;
    private Call<?> callLike;
    private boolean temp = false;
    private PlayerViewPagerAdapter viewPagerAdapter;
    private Animation myAnimstop, myAnimlike;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.fragment_player);
        ButterKnife.bind(this);

        myAnimstop = AnimationUtils.loadAnimation(this, R.anim.bounce);
        MyBounceInterpolator interpolator1 = new MyBounceInterpolator(0.1, 10);
        myAnimstop.setInterpolator(interpolator1);

        myAnimlike = AnimationUtils.loadAnimation(this, R.anim.bounce);
        MyBounceInterpolator interpolator2 = new MyBounceInterpolator(0.1, 10);
        myAnimlike.setInterpolator(interpolator2);


        mBinding.swipeBack.setDraggerPosition(DraggerPosition.TOP);
        mBinding.swipeBack.setDraggerLimit(0.8f);
        mBinding.fragmentPlayerLlSubControl.setOnTouchListener(this);
        myTracksFragment = new ArrayList<>();
        viewPagerAdapter = new PlayerViewPagerAdapter(getSupportFragmentManager());
        mBinding.fragmentViewPager.setAdapter(viewPagerAdapter);
        mBinding.fragmentViewPager.addOnPageChangeListener(this);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        initControls();
    }

    private void initControls() {
        try {
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            mBinding.volumeSeekbar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            mBinding.volumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            progress, 0);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @OnClick(R.id.player_like)
    public void playerLike() {

            if (Utility.haveNetworkConnection(this)) {
                if (myTracks.get(pos).isLike() == false) {
                    mBinding.playerLike.setImageDrawable(getResources().getDrawable(R.drawable.likered));
                    mBinding.playerLike.startAnimation(myAnimlike);
                    myTracks.get(pos).setLike(true);
                } else {
                    mBinding.playerLike.setImageDrawable(getResources().getDrawable(R.drawable.ic_like));
                    mBinding.playerLike.startAnimation(myAnimlike);
                    myTracks.get(pos).setLike(false);
                }
                    callLike = ((MusicApp) getApplication()).getApiTask().likeSong(myTracks.get(pos).getId() + "",
                            new APICallback(this, APICall.LIKE_REQ_CODE, this));

            }else {
                Utility.showRedSnackBar(this, getString(R.string.no_internet_connection));
            }
    }


    @OnClick(R.id.player_volume)
    public void volume() {
        if (mBinding.volumeSeekbar.getVisibility() == View.VISIBLE) {
            mBinding.volumeSeekbar.setVisibility(View.GONE);
            mBinding.playerVolume.setColorFilter(ContextCompat.getColor(this, R.color.white));
        } else {
            mBinding.volumeSeekbar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
            mBinding.volumeSeekbar.setVisibility(View.VISIBLE);
            mBinding.playerVolume.setColorFilter(ContextCompat.getColor(this, R.color.btn_red));
        }
    }


    @OnClick(R.id.player_repeat)
    public void repeatMode() {
        if (!temp) {
            temp = true;
            mBinding.playerRepeat.setColorFilter(ContextCompat.getColor(this, R.color.btn_red), android.graphics.PorterDuff.Mode.MULTIPLY);
        } else {
            temp = false;
            mBinding.playerRepeat.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
        }
        musicSrv.setReapeat(temp);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN ||
                keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            mBinding.volumeSeekbar.setVisibility(View.GONE);
            mBinding.playerVolume.setColorFilter(ContextCompat.getColor(PlayerActivity.this, R.color.white));
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN ||
                keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            mBinding.volumeSeekbar.setVisibility(View.GONE);
            mBinding.playerVolume.setColorFilter(ContextCompat.getColor(PlayerActivity.this, R.color.white));
            return false;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mBinding.volumeSeekbar.setVisibility(View.GONE);
        mBinding.playerVolume.setColorFilter(ContextCompat.getColor(this, R.color.white));
        return false;
    }

    @Override
    public void onBackPressed() {
        mBinding.swipeBack.closeActivity();
    }

    @Override
    public void onResponseComplete(Object clsGson, int requestCode) {
        if (requestCode == APICall.LIKE_REQ_CODE) {
            if (clsGson instanceof MessageResponse) {
                MessageResponse messageResponse = (MessageResponse) clsGson;
            }
        }

        if (requestCode == APICall.UNLIKE_REQ_CODE) {
            if (clsGson instanceof MessageResponse) {
                MessageResponse messageResponse = (MessageResponse) clsGson;
                myTracks.get(pos).setLike(false);
                if (myTracks.get(pos).isLike()) {
                    mBinding.playerLike.setImageDrawable(getResources().getDrawable(R.drawable.likered));
                    mBinding.playerLike.startAnimation(myAnimlike);
                } else {
                    mBinding.playerLike.setImageDrawable(getResources().getDrawable(R.drawable.ic_like));
                    mBinding.playerLike.startAnimation(myAnimlike);
                }
            }
        }

    }

    @Override
    public void onResponseError(Object errorMessage, int requestCode) {

    }

    @Override
    public void onSongChange(int pos) {

        if (musicSrv.getList() != null && musicSrv.getList().size() > 0) {
            if (musicSrv != null) {
                myTracks = musicSrv.getList();
                pos = musicSrv.getPosition();
                mBinding.fragmentViewPager.setCurrentItem(pos);
                if (myTracks.get(pos).isLike() == true) {
                    mBinding.playerLike.setImageDrawable(getResources().getDrawable(R.drawable.likered));
                } else {
                    mBinding.playerLike.setImageDrawable(getResources().getDrawable(R.drawable.ic_like));
                }
                if (myTracks.get(pos).getImage() != null)
                    Utility.loadImage(myTracks.get(pos).getImage(), new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            super.onLoadingComplete(imageUri, view, loadedImage);
                            mBinding.playerBg.setImageBitmap(loadedImage);
                            new setBlurredAlbumArt().execute(loadedImage);
                        }
                    });
                else if (myTracks.get(pos).getPicture() != null)
                    Utility.loadImage(myTracks.get(pos).getPicture(), new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            super.onLoadingComplete(imageUri, view, loadedImage);
                            mBinding.playerBg.setImageBitmap(loadedImage);
                            new setBlurredAlbumArt().execute(loadedImage);
                        }
                    });
                else {
                    mBinding.playerBg.setImageResource(R.drawable.default_img);
                }

                mBinding.playerTotalLike.setText(myTracks.get(pos).getTotalLike());
                mBinding.playerTotalPlay.setText(myTracks.get(pos).getTotalPlay());

                musicSrv.setSeekBar(mBinding.playerSeekbar);
                musicSrv.setStartTime(mBinding.playerStarttime);
                musicSrv.setEndTime(mBinding.playerEndtime);
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (musicSrv != null)
            musicSrv.setPositonSong(position);
        onSongChange(position);


    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class setBlurredAlbumArt extends AsyncTask<Bitmap, Void, Drawable> {

        @Override
        protected Drawable doInBackground(Bitmap... loadedImage) {
            Drawable drawable = null;
            try {
                drawable = Utility.createBlurredImageFromBitmap(loadedImage[0], PlayerActivity.this, 6);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return drawable;
        }

        @Override
        protected void onPostExecute(Drawable result) {
            if (result != null) {
                if (mBinding.playerBg.getDrawable() != null) {
                    final TransitionDrawable td =
                            new TransitionDrawable(new Drawable[]{
                                    mBinding.playerBg.getDrawable(),
                                    result
                            });
                    mBinding.playerBg.setImageDrawable(td);
                    td.startTransition(200);

                } else {
                    mBinding.playerBg.setImageDrawable(result);
                }
            }
        }

        @Override
        protected void onPreExecute() {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("TAG", "onResume: ");
        playIntent = new Intent(this, MusicService.class);
        bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);

    }

    private ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //get service
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            musicSrv = binder.getService();
            musicSrv.setSongLister(PlayerActivity.this);
            Log.i("TAG,", "onServiceConnected: ");

            if (musicSrv.getList() != null && musicSrv.getList().size() > 0) {
                if (musicSrv != null && musicSrv.getList() != null) {


                    myTracks = musicSrv.getList();

                    for (int i = 0; i < myTracks.size(); i++) {
                        myTracksFragment.add(new PlayerActivityItemFragment(myTracks.get(i)));
                    }
                    viewPagerAdapter.addFragment(myTracksFragment);
                    mBinding.fragmentViewPager.setAdapter(viewPagerAdapter);
                    pos = musicSrv.getPosition();
                    mBinding.fragmentViewPager.setCurrentItem(pos);

                    if (myTracks.get(pos).isLike() == true) {
                        mBinding.playerLike.setImageDrawable(getResources().getDrawable(R.drawable.likered));
                    } else {
                        mBinding.playerLike.setImageDrawable(getResources().getDrawable(R.drawable.ic_like));
                    }
                    if (myTracks.get(pos).getImage() != null)
                        Utility.loadImage(myTracks.get(pos).getImage(), new SimpleImageLoadingListener() {
                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                super.onLoadingComplete(imageUri, view, loadedImage);
                                new setBlurredAlbumArt().execute(loadedImage);
                            }
                        });
                    else
                        Utility.loadImage(myTracks.get(pos).getPicture(), new SimpleImageLoadingListener() {
                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                super.onLoadingComplete(imageUri, view, loadedImage);
                                new setBlurredAlbumArt().execute(loadedImage);
                            }
                        });

                    mBinding.playerTotalLike.setText(myTracks.get(pos).getTotalLike());
                    mBinding.playerTotalPlay.setText(myTracks.get(pos).getTotalPlay());

                    musicSrv.setSeekBar(mBinding.playerSeekbar);
                    musicSrv.setStartTime(mBinding.playerStarttime);
                    musicSrv.setEndTime(mBinding.playerEndtime);


                    if (musicSrv.getPlayWhenReady() == false)
                        mBinding.playerStop.setImageResource(R.drawable.ic_play);
                    else
                        mBinding.playerStop.setImageResource(R.drawable.ic_pause);

                }
            } else {
                finish();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };

    @OnClick(R.id.player_share)
    public void shareUrl() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, myTracks.get(pos).getShareUrl().toString());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    @OnClick(R.id.player_next)
    public void next() {

        if (musicSrv != null) {
            musicSrv.playNext();
            mBinding.playerStop.startAnimation(myAnimstop);
            mBinding.playerStop.setImageResource(R.drawable.ic_pause);

        }
        if (musicSrv.getList() != null && musicSrv.getList().size() > 0) {
            if (musicSrv != null) {
                myTracks = musicSrv.getList();
                pos = musicSrv.getPosition();
                mBinding.fragmentViewPager.setCurrentItem(pos);
                if (myTracks.get(pos).isLike() == true) {
                    mBinding.playerLike.setImageDrawable(getResources().getDrawable(R.drawable.likered));
                } else {
                    mBinding.playerLike.setImageDrawable(getResources().getDrawable(R.drawable.ic_like));
                }
//                if (myTracks.get(pos).getPicture() != null)
//                    Utility.loadImage(myTracks.get(pos).getPicture(), mBinding.playerImage);
//                else
//                    Utility.loadImage(myTracks.get(pos).getImage(), mBinding.playerImage);
//                mBinding.playerSongName.setText(myTracks.get(pos).getTitle());
//                mBinding.playerArtistName.setText(myTracks.get(pos).getArtistName());

                mBinding.playerTotalLike.setText(myTracks.get(pos).getTotalLike());
                mBinding.playerTotalPlay.setText(myTracks.get(pos).getTotalPlay());

                musicSrv.setSeekBar(mBinding.playerSeekbar);
                musicSrv.setStartTime(mBinding.playerStarttime);
                musicSrv.setEndTime(mBinding.playerEndtime);
            }
        }

    }

    @OnClick(R.id.player_previous)
    public void previous() {

        if (musicSrv != null) {
            musicSrv.playPrev();
            mBinding.playerStop.startAnimation(myAnimstop);
            mBinding.playerStop.setImageResource(R.drawable.ic_pause);

        }
        if (musicSrv.getList() != null && musicSrv.getList().size() > 0) {
            if (musicSrv != null) {
                myTracks = musicSrv.getList();
                pos = musicSrv.getPosition();
                mBinding.fragmentViewPager.setCurrentItem(pos);
                if (myTracks.get(pos).isLike() == true) {
                    mBinding.playerLike.setImageDrawable(getResources().getDrawable(R.drawable.likered));
                } else {
                    mBinding.playerLike.setImageDrawable(getResources().getDrawable(R.drawable.ic_like));
                }
//                if (myTracks.get(pos).getPicture() != null)
//                    Utility.loadImage(myTracks.get(pos).getPicture(), mBinding.playerImage);
//                else
//                    Utility.loadImage(myTracks.get(pos).getImage(), mBinding.playerImage);
//                mBinding.playerSongName.setText(myTracks.get(pos).getTitle());
//                mBinding.playerArtistName.setText(myTracks.get(pos).getArtistName());
                mBinding.playerTotalLike.setText(myTracks.get(pos).getTotalLike());
                mBinding.playerTotalPlay.setText(myTracks.get(pos).getTotalPlay());

                musicSrv.setSeekBar(mBinding.playerSeekbar);
                musicSrv.setStartTime(mBinding.playerStarttime);
                musicSrv.setEndTime(mBinding.playerEndtime);
            }
        }
    }

    @OnClick(R.id.player_stop)
    public void stopSong() {


        if (musicSrv.getPlayWhenReady() == false) {
            musicSrv.startPlayer();

            mBinding.playerStop.startAnimation(myAnimstop);

            mBinding.playerStop.setImageResource(R.drawable.ic_pause);
        } else {
            musicSrv.pausePlayer();
            mBinding.playerStop.startAnimation(myAnimstop);
            mBinding.playerStop.setImageResource(R.drawable.ic_play);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(musicConnection);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


}
