package com.musika.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.musika.MusicApp;
import com.musika.R;
import com.musika.SettingActivity;
import com.musika.adapter.TempPopularSongAdapter;
import com.musika.container.BaseContainer;
import com.musika.databinding.FragmentLibraryBinding;
import com.musika.retrofit.APICall;
import com.musika.retrofit.APICallback;
import com.musika.retrofit.OnApiResponseListner;
import com.musika.retrofit.model.MessageResponse;
import com.musika.retrofit.model.RecentPlayData;
import com.musika.retrofit.model.RecentPlayDataEntityManager;
import com.musika.retrofit.model.RecentPlayResponse;
import com.musika.retrofit.model.common.MyTrack;
import com.musika.utility.Utility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

public class LibraryFragment extends Fragment implements TempPopularSongAdapter.OnSongClickListerner, OnApiResponseListner {


    private FragmentLibraryBinding mBinding;
    private TempPopularSongAdapter songListAdapter;
    private Call<?> callRecentPlay;
    private ArrayList<RecentPlayData> songArrayList;
    private List<MyTrack> myTrackList;
    private SetBottomRaw setBottomRaw;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_library, container, false);
        ButterKnife.bind(this, mBinding.getRoot());
        setpData();
        return mBinding.getRoot();
    }

    private void setpData() {
        ViewCompat.setNestedScrollingEnabled(mBinding.actRvPopularSong, false);
        RecentPlayDataEntityManager recentPlayDataEntity = new RecentPlayDataEntityManager();
        if (recentPlayDataEntity.count() <= 10) {
            songArrayList = (ArrayList<RecentPlayData>) recentPlayDataEntity.select().limit(recentPlayDataEntity.count() - 10, recentPlayDataEntity.count()).asList();
        } else {
            songArrayList = (ArrayList<RecentPlayData>) recentPlayDataEntity.select().limit(recentPlayDataEntity.count() - 10, recentPlayDataEntity.count()).asList();
        }

        if (songArrayList == null || songArrayList.size() == 0) {
            songArrayList = new ArrayList<>();
            songListAdapter = new TempPopularSongAdapter(getActivity(), this, songArrayList);
            mBinding.actRvPopularSong.setLayoutManager(new LinearLayoutManager(getActivity()));
            mBinding.actRvPopularSong.setAdapter(songListAdapter);
            callingApi();
        } else {
            Collections.reverse(songArrayList);
            songListAdapter = new TempPopularSongAdapter(getActivity(), this, songArrayList);
            mBinding.actRvPopularSong.setLayoutManager(new LinearLayoutManager(getActivity()));
            mBinding.actRvPopularSong.setAdapter(songListAdapter);
            songListAdapter.notifyDataSetChanged();
        }
    }

    private void callingApi() {
        if (Utility.haveNetworkConnection(getActivity())) {
            callRecentPlay = ((MusicApp) getActivity().getApplication()).getApiTask()
                    .recentPlay(new APICallback(getActivity(), APICall.RECENTLY_PLAYED_REQ_CODE, this));
        } else {
            Utility.showRedSnackBar(getActivity(), getString(R.string.no_internet_connection));
        }
    }

    @OnClick(R.id.fragment_library_rl_download)
    public void downloadClick() {
        //   ((BaseContainer) getParentFragment()).addFragment(new DownloadFragment());
    }

    @OnClick(R.id.fragment_library_rl_artist)
    public void artistClick() {
        ((BaseContainer) getParentFragment()).addFragment(new ArtistLibraryListFragment());
    }

    @OnClick(R.id.fragment_library_rl_album)
    public void albumClick() {
        ((BaseContainer) getParentFragment()).addFragment(new AlbumLibraryListFragment());
    }

    @OnClick(R.id.fragment_library_rl_play_list)
    public void playListClick() {
        ((BaseContainer) getParentFragment()).addFragment(new PlayListFragment(getString(R.string.playlists)));
    }

    @OnClick(R.id.fragment_library_rl_like)
    public void likeClick() {
        ((BaseContainer) getParentFragment()).addFragment(new LikeFetchFragment(getString(R.string.likes)));
    }

    @OnClick(R.id.fragment_library_setting)
    public void settingClick() {
        Utility.navigationIntent(getActivity(), SettingActivity.class);
    }

    @Override
    public void onSongMoreClick(int pos) {
        MyTrack tempTrack = new MyTrack();
        tempTrack.setId(songArrayList.get(pos).getId());
        tempTrack.setTitle(songArrayList.get(pos).getTitle());
        if (songArrayList.get(pos).getArtist() != null) {
            if (songArrayList.get(pos).getArtist().getIdu() != null) {
                tempTrack.setArtistId(songArrayList.get(pos).getArtist().getIdu());
            }
            tempTrack.setArtistName(songArrayList.get(pos).getArtist().getRealname());
            tempTrack.setImage(songArrayList.get(pos).getArtist().getImage());
        }
        new PlayerOptionFragment(tempTrack).show(getChildFragmentManager(), "");
    }

    @Override
    public void onArtistclick(int pos) {
        ((BaseContainer) getParentFragment()).addFragment(new
                ArtistDetailsFragment(songArrayList.get(pos).getArtist().getIdu()
                , songArrayList.get(pos).getArtist().getRealname()));

    }

    @Override
    public void onSongClick(int pos) {
        setBottomRaw = (SetBottomRaw) getActivity();
        myTrackList = new ArrayList<>();
        for (int i = 0; i <songArrayList.size() ; i++) {
            MyTrack myTrack = new MyTrack();
            myTrack.setId(songArrayList.get(i).getId());
            myTrack.setName(songArrayList.get(i).getName());
            myTrack.setTitle(songArrayList.get(i).getTitle());
            if (songArrayList.get(i).getArtist().getIdu() != null)
                myTrack.setArtistId(songArrayList.get(i).getArtist().getIdu());
            myTrack.setArtistName(songArrayList.get(i).getArtist().getRealname());
            myTrack.setImage(songArrayList.get(i).getArtist().getImage());
            myTrack.setShareUrl(songArrayList.get(i).getShare_url());
            myTrack.setLike(songArrayList.get(i).getLiked());
            myTrack.setTotalLike(songArrayList.get(i).getTotallikes());
            myTrack.setTotalPlay(songArrayList.get(i).getTotalplays());
            myTrack.setPicture(songArrayList.get(i).getPicture());

            myTrackList.add(i,myTrack);
        }
        setBottomRaw.onSetBottom(myTrackList,pos);
    }

    @Override
    public void onResponseComplete(Object clsGson, int requestCode) {
        if (requestCode == APICall.RECENTLY_PLAYED_REQ_CODE) {
            if (clsGson instanceof RecentPlayResponse) {
                RecentPlayResponse recentPlayResponse = (RecentPlayResponse) clsGson;
                if (recentPlayResponse.getData() != null) {
                    RecentPlayDataEntityManager recentPlayDataEntity = new RecentPlayDataEntityManager();
                    recentPlayDataEntity.add(recentPlayResponse.getData());
                    mBinding.actTvRecentPlaySong.setVisibility(View.VISIBLE);
                    songArrayList.addAll(recentPlayResponse.getData());
                    songListAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void onResponseError(Object errorMessage, int requestCode) {
        if (errorMessage instanceof MessageResponse) {
            MessageResponse messageResponse = (MessageResponse) errorMessage;

        }
    }
    public interface SetBottomRaw{
        void onSetBottom(List<MyTrack> myTracks, int pos);
    }
}