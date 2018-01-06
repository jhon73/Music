package com.musika.fragment;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.musika.MusicApp;
import com.musika.R;
import com.musika.adapter.PlaylistSongAdapter;
import com.musika.baseclass.BaseFragment;
import com.musika.container.BaseContainer;
import com.musika.databinding.FragmentPlayListLibraryBinding;
import com.musika.retrofit.APICall;
import com.musika.retrofit.APICallback;
import com.musika.retrofit.OnApiResponseListner;
import com.musika.retrofit.model.FetchByIdResponse;
import com.musika.retrofit.model.MessageResponse;
import com.musika.retrofit.model.Track;
import com.musika.retrofit.model.common.MyTrack;
import com.musika.utility.Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

@SuppressLint("ValidFragment")
public class PlayListLibraryListFragment extends BaseFragment implements PlaylistSongAdapter.OnSongClickListerner, OnApiResponseListner {
    private final List<Track> tracks;
    private FragmentPlayListLibraryBinding mBinding;
    private PlaylistSongAdapter songAdapter;
    private String title;
    private int id;
    private int offset = 0, limit = 7;
    private boolean isLoading;
    private boolean isMore = true;
    private int totalCount;
    private LinearLayoutManager linearLayoutManager;
    private Call<?> callFetchSongByID;


    public PlayListLibraryListFragment(String title, List<Track> tracks) {
        this.title = title;
        this.tracks = tracks;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_play_list_library, container, false);
        ButterKnife.bind(this,mBinding.getRoot());
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.frTvPlayList.setText(title);
        if(tracks!=null && tracks.size()>0) {
            linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            mBinding.frRvPlayList.setLayoutManager(linearLayoutManager);
            songAdapter = new PlaylistSongAdapter(getActivity(), this, tracks);
            mBinding.frRvPlayList.setAdapter(songAdapter);
        }
        else{
            mBinding.fragmentLikeTvNoDataFound.setVisibility(View.VISIBLE);
        }
      //  pagination();
    }

    @OnClick(R.id.fragment_playlist_iv_back)
    public void onbackpress(){
        getActivity().onBackPressed();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (callFetchSongByID != null && !callFetchSongByID.isCanceled()) {
            callFetchSongByID.cancel();
        }
    }

    private void callingApi() {
        if (Utility.haveNetworkConnection(getActivity())) {
            callFetchSongByID = ((MusicApp) getActivity().getApplication()).getApiTask()
                    .fetchSongByIdPlaylist(id, new APICallback(getActivity(), APICall.FETCH_SONG_ID_PLAYLIST_REQ_CODE, this));
        } else {
            Utility.showRedSnackBar(getActivity(), getString(R.string.no_internet_connection));
        }
    }

    private void pagination() {
        mBinding.frRvPlayList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

                if (!isLoading) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= (tracks.size()) && isMore && tracks.size() < totalCount) {
                        offset += limit;
                        isLoading = true;
                        callingApi();
                    }
                }
            }
        });
    }

    @Override
    public void onSongClick(int pos) {
        MyTrack tempTrack = new MyTrack();
        tempTrack.setId(tracks.get(pos).getId());
        tempTrack.setTitle(tracks.get(pos).getTitle());
        if (tracks.get(pos).getArtist() != null) {
            tempTrack.setArtistId(tracks.get(pos).getArtist().getId());
            tempTrack.setArtistName(tracks.get(pos).getArtist().getRealname());
            tempTrack.setImage(tracks.get(pos).getArtist().getPicture());
        }
        if (tracks.get(pos).getAlbumId() != null && tracks.get(pos).getAlbumId() > 0) {
            tempTrack.setAlbumId(tracks.get(pos).getAlbumId());
        }
        new PlayerOptionFragment(tempTrack).show(getChildFragmentManager(), "");
    }

    @Override
    public void onArtistClick(int pos) {
        ((BaseContainer) getParentFragment()).addFragment(new
                ArtistDetailsFragment(tracks.get(pos).getArtist().getIdu()
                , tracks.get(pos).getArtist().getRealname()));

    }

    @Override
    public void onResponseComplete(Object clsGson, int requestCode) {
        isLoading = false;
        mBinding.frRvArtistProgress.setVisibility(View.GONE);
        mBinding.frRvArtistPv.setVisibility(View.GONE);
        if (requestCode == APICall.FETCH_SONG_ID_PLAYLIST_REQ_CODE) {
            if (clsGson instanceof FetchByIdResponse) {
                FetchByIdResponse fetchByIdParentResponse = (FetchByIdResponse) clsGson;
                totalCount = fetchByIdParentResponse.getCount();
                if (totalCount > 0) {
                    tracks.addAll(fetchByIdParentResponse.getData().getTracks());
                    mBinding.fragmentLikeTvNoDataFound.setVisibility(View.GONE);
                    songAdapter.notifyDataSetChanged();
                } else {
                    isMore = false;
                    mBinding.frRvPlayList.setVisibility(View.GONE);
                    mBinding.fragmentLikeTvNoDataFound.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void onResponseError(Object errorMessage, int requestCode) {
        isLoading = false;
        mBinding.frRvArtistProgress.setVisibility(View.GONE);
        mBinding.frRvArtistPv.setVisibility(View.GONE);
        if (requestCode == APICall.FETCH_SONG_ID_PLAYLIST_REQ_CODE) {
            isMore = false;
            mBinding.fragmentLikeTvNoDataFound.setVisibility(View.VISIBLE);
        }
        if (errorMessage instanceof MessageResponse) {
            MessageResponse messageResponse = (MessageResponse) errorMessage;
            Utility.showRedSnackBar(getActivity(), messageResponse.getMessage());
        }
    }
}
