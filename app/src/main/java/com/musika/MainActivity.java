package com.musika;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.musika.adapter.PagerAdapter;
import com.musika.baseclass.BaseAppCompactActivity;
import com.musika.container.ExploreContainer;
import com.musika.container.LibraryContainer;
import com.musika.container.SearchContainer;
import com.musika.container.SuggestedContainer;
import com.musika.databinding.ActivityMainBinding;
import com.musika.databinding.CustomTablayoutBinding;
import com.musika.fragment.ExploreFragment;
import com.musika.fragment.LibraryFragment;
import com.musika.fragment.SearchFragment;
import com.musika.fragment.SuggestedFragment;
import com.musika.fragment.TrackFragment;
import com.musika.retrofit.APICall;
import com.musika.retrofit.APICallback;
import com.musika.retrofit.OnApiResponseListner;
import com.musika.retrofit.ProfileResponse;
import com.musika.retrofit.model.LikeData;
import com.musika.retrofit.model.LikeDataEntityManager;
import com.musika.retrofit.model.LikeResponse;
import com.musika.retrofit.model.MessageResponse;
import com.musika.retrofit.model.common.MyTrack;
import com.musika.utility.Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;


public class MainActivity extends BaseAppCompactActivity implements TabLayout.OnTabSelectedListener, OnApiResponseListner,
        SuggestedFragment.SetBottomRaw, ExploreFragment.SetBottomRaw, MusicService.OnSongChangeListener,
        SearchFragment.SetBottomRaw, TrackFragment.SetBottomRaw, LibraryFragment.SetBottomRaw {

    ActivityMainBinding mBinding;
    private PagerAdapter pagerAdapter;
    private List<Fragment> fragmentList;
    private Call<?> callProfile, callLike;
    private Intent playIntent;
    private MusicService musicSrv;
    private List<MyTrack> myTracks;
    private int pos;
    private Call<?> callLikeFetch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        ButterKnife.bind(this);
        setUpTabFragment();
        setupTabIcons();
        if (Utility.haveNetworkConnection(this)) {
            callProfile = ((MusicApp) getApplication()).getApiTask().getProfile(
                    new APICallback(this, APICall.PROFILE, this));
        }

        playIntent = new Intent(this, MusicService.class);
        startService(playIntent);

        callinLikeSong();
    }

    private void callinLikeSong() {
        LikeDataEntityManager likeDataEntityManager = new LikeDataEntityManager();
        List<LikeData> likeDatas = likeDataEntityManager.select().asList();
        if (likeDatas.size() == 0) {
            if (Utility.haveNetworkConnection(this)) {
                callLikeFetch = ((MusicApp) getApplication()).getApiTask()
                        .likeFetch(new APICallback(this, APICall.LIKE_FETCH_REQ_CODE, this));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //get service
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            musicSrv = binder.getService();



            musicSrv.setSongLister(MainActivity.this);
            if (musicSrv.getList() != null && musicSrv.getList().size() > 0) {
                onSetViewBottom(musicSrv.getList().get(musicSrv.getPosition()));




            } else {
                mBinding.activityMainBottomSong.setVisibility(View.GONE);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };

    public MusicService setMyService() {
        return musicSrv;
    }


    private void setUpTabFragment() {
        fragmentList = new ArrayList<>();
        fragmentList.add(new ExploreContainer());
        fragmentList.add(new SuggestedContainer());
        fragmentList.add(new LibraryContainer());
        fragmentList.add(new SearchContainer());
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pagerAdapter.addAllFragment(fragmentList);
        mBinding.activityMainVp.setAdapter(pagerAdapter);
        mBinding.activityMainVp.setOffscreenPageLimit(pagerAdapter.getCount());
        mBinding.activityMainTabLayout.setupWithViewPager(mBinding.activityMainVp);
        mBinding.activityMainTabLayout.addOnTabSelectedListener(this);
    }

//    @OnClick(R.id.activity_main_bottom_song)
//    public void onBottomClick() {
        @OnClick({R.id.activity_main_bottom_image,R.id.activity_ll_bottom})
        public void bottomclick(){
        if ((Utility.doubleTapTime + Utility.CLICK_INTERVAL) < System.currentTimeMillis()) {
            Utility.doubleTapTime = System.currentTimeMillis();
            Utility.navigationIntent(this, PlayerActivity.class);

        }
    }

    private void setupTabIcons() {

        CustomTablayoutBinding tabOne = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.custom_tablayout, null, false);
        tabOne.customTabLayoutTv.setText(getString(R.string.explore));
        tabOne.customTabLayoutIv.setImageResource(R.drawable.ic_compass_new);
        tabOne.customTabLayoutTv.setTextColor(ContextCompat.getColor(this, R.color.btn_red));
        tabOne.customTabLayoutIv.setColorFilter(ContextCompat.getColor(this, R.color.btn_red));

        mBinding.activityMainTabLayout.getTabAt(0).setCustomView(tabOne.getRoot());

        CustomTablayoutBinding tabTwo = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.custom_tablayout, null, false);
        tabTwo.customTabLayoutTv.setText(getString(R.string.suggested));
        tabTwo.customTabLayoutIv.setImageResource(R.drawable.ic_suggested_new);
        tabTwo.customTabLayoutTv.setTextColor(ContextCompat.getColor(this, R.color.font_gray));
        tabTwo.customTabLayoutIv.setColorFilter(ContextCompat.getColor(this, R.color.font_gray));

        mBinding.activityMainTabLayout.getTabAt(1).setCustomView(tabTwo.getRoot());

        CustomTablayoutBinding tabThree = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.custom_tablayout, null, false);
        tabThree.customTabLayoutTv.setText(getString(R.string.library));
        tabThree.customTabLayoutIv.setImageResource(R.drawable.ic_music_new);
        tabThree.customTabLayoutTv.setTextColor(ContextCompat.getColor(this, R.color.font_gray));
        tabThree.customTabLayoutIv.setColorFilter(ContextCompat.getColor(this, R.color.font_gray));
        mBinding.activityMainTabLayout.getTabAt(2).setCustomView(tabThree.getRoot());

        CustomTablayoutBinding tabFour = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.custom_tablayout, null, false);
        tabFour.customTabLayoutTv.setText(getString(R.string.search));
        tabFour.customTabLayoutIv.setImageResource(R.drawable.ic_search_black);
        tabFour.customTabLayoutTv.setTextColor(ContextCompat.getColor(this, R.color.font_gray));
        tabFour.customTabLayoutIv.setColorFilter(ContextCompat.getColor(this, R.color.font_gray));
        mBinding.activityMainTabLayout.getTabAt(3).setCustomView(tabFour.getRoot());
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        CustomTablayoutBinding selectedTab = DataBindingUtil.getBinding(tab.getCustomView());
        selectedTab.customTabLayoutTv.setTextColor(ContextCompat.getColor(this, R.color.btn_red));
        selectedTab.customTabLayoutIv.setColorFilter(ContextCompat.getColor(this, R.color.btn_red));
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        CustomTablayoutBinding unSelectedTab = DataBindingUtil.getBinding(tab.getCustomView());
        unSelectedTab.customTabLayoutTv.setTextColor(ContextCompat.getColor(this, R.color.font_gray));
        unSelectedTab.customTabLayoutIv.setColorFilter(ContextCompat.getColor(this, R.color.font_gray));
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }

    @Override
    public void onBackPressed() {
        int pos = mBinding.activityMainVp.getCurrentItem();
        if (pagerAdapter.getItem(pos).getChildFragmentManager().getBackStackEntryCount() > 0)
            pagerAdapter.getItem(pos).getChildFragmentManager().popBackStack();
        else
            super.onBackPressed();
    }

    @Override
    public void onResponseComplete(Object clsGson, int requestCode) {
        if (requestCode == APICall.PROFILE) {
            if (clsGson instanceof ProfileResponse) {
                ProfileResponse profileResponse = (ProfileResponse) clsGson;
                if (profileResponse.getData() != null) {
                    getMyPref().setProfileData(profileResponse.getData());
                }
            }
        }

        if (requestCode == APICall.LIKE_REQ_CODE) {
            if (clsGson instanceof MessageResponse) {
                MessageResponse messageResponse = (MessageResponse) clsGson;
            }
        }

        if (requestCode == APICall.LIKE_FETCH_REQ_CODE) {
            if (clsGson instanceof LikeResponse) {
                LikeResponse likeFetchResponse = (LikeResponse) clsGson;
                LikeDataEntityManager likeDataEntityManager = new LikeDataEntityManager();
                likeDataEntityManager.add(likeFetchResponse.getData());
            }
        }

    }

    @Override
    public void onResponseError(Object errorMessage, int requestCode) {

    }

    @Override
    public void onSetBottom(List<MyTrack> myTracks, int pos) {
        musicSrv.setList(myTracks, pos);
        musicSrv.setSeekBar(mBinding.activityMainBottomSeekbar);
        mBinding.activityMainBottomSong.setVisibility(View.VISIBLE);
        this.myTracks = myTracks;
        this.pos = pos;

        if (myTracks.get(pos).getImage() != null)
            Utility.loadImage(myTracks.get(pos).getImage(), mBinding.activityMainBottomImage, R.drawable.default_track);
        else
            Utility.loadImage(myTracks.get(pos).getPicture(), mBinding.activityMainBottomImage, R.drawable.default_track);

        if (myTracks.get(pos).isLike() == true)
            mBinding.activityMainBottomLike.setImageDrawable(getResources().getDrawable(R.drawable.likered));
        else
            mBinding.activityMainBottomLike.setImageDrawable(getResources().getDrawable(R.drawable.ic_heart_outline));

        mBinding.activityMainBottomSongName.setText(myTracks.get(pos).getTitle());
        mBinding.activityMainBottomArtistName.setText(myTracks.get(pos).getArtistName());
    }

    public void onSetViewBottom(MyTrack myTracks) {
        musicSrv.setSeekBar(mBinding.activityMainBottomSeekbar);
        mBinding.activityMainBottomSong.setVisibility(View.VISIBLE);
        if (myTracks.getImage() != null)
            Utility.loadImage(myTracks.getImage(), mBinding.activityMainBottomImage, R.drawable.default_track);
        else
            Utility.loadImage(myTracks.getPicture(), mBinding.activityMainBottomImage, R.drawable.default_track);

        if (musicSrv.getPlayWhenReady() == false)
            mBinding.activityMainBottomPause.setImageResource(R.drawable.ic_play_red);
        else
            mBinding.activityMainBottomPause.setImageResource(R.drawable.ic_pause_red);
        if (myTracks.isLike() == true)
            mBinding.activityMainBottomLike.setImageDrawable(getResources().getDrawable(R.drawable.likered));
        else
            mBinding.activityMainBottomLike.setImageDrawable(getResources().getDrawable(R.drawable.ic_heart_outline));

        mBinding.activityMainBottomSongName.setText(myTracks.getTitle());
        mBinding.activityMainBottomArtistName.setText(myTracks.getArtistName());
    }

    @OnClick(R.id.activity_main_bottom_like)
    public void bottomLike() {
//
            if (Utility.haveNetworkConnection(this)) {

                if (myTracks.get(pos).isLike() == false) {
                    mBinding.activityMainBottomLike.setImageDrawable(getResources().getDrawable(R.drawable.likered));
                    myTracks.get(pos).setLike(true);
                }
                else {
                    mBinding.activityMainBottomLike.setImageDrawable(getResources().getDrawable(R.drawable.ic_heart_outline));
                    myTracks.get(pos).setLike(false);
                }
                callLike = ((MusicApp) getApplication()).getApiTask().likeSong(myTracks.get(pos).getId() + "",
                        new APICallback(this, APICall.LIKE_REQ_CODE, this));
            } else {
                Utility.showRedSnackBar(this, getString(R.string.no_internet_connection));
            }

    }

    @OnClick(R.id.activity_main_bottom_pause)
    public void redPause() {
        if (musicSrv.getPlayWhenReady() == false) {
            musicSrv.startPlayer();
            mBinding.activityMainBottomPause.setImageResource(R.drawable.ic_pause_red);
        } else {
            musicSrv.pausePlayer();
            mBinding.activityMainBottomPause.setImageResource(R.drawable.ic_play_red);
        }
    }

    @Override
    public void onSongChange(int pos) {
        mBinding.activityMainBottomPause.setImageResource(R.drawable.ic_pause_red);
        musicSrv.setSeekBar(mBinding.activityMainBottomSeekbar);
        mBinding.activityMainBottomSong.setVisibility(View.VISIBLE);
        if (myTracks.get(pos).getImage() != null)
            Utility.loadImage(myTracks.get(pos).getImage(), mBinding.activityMainBottomImage, R.drawable.default_track);
        else
            Utility.loadImage(myTracks.get(pos).getPicture(), mBinding.activityMainBottomImage, R.drawable.default_track);

        mBinding.activityMainBottomSongName.setText(myTracks.get(pos).getTitle());
        mBinding.activityMainBottomArtistName.setText(myTracks.get(pos).getArtistName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        musicSrv.setSongLister(null);
        unbindService(musicConnection);

    }
}
