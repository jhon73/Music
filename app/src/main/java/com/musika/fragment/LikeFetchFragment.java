package com.musika.fragment;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.musika.MusicApp;
import com.musika.R;
import com.musika.adapter.LikeSongAdapter;
import com.musika.container.BaseContainer;
import com.musika.databinding.FragmentPlayListLibraryBinding;
import com.musika.retrofit.APICall;
import com.musika.retrofit.APICallback;
import com.musika.retrofit.OnApiResponseListner;
import com.musika.retrofit.model.LikeData;
import com.musika.retrofit.model.LikeDataEntityManager;
import com.musika.retrofit.model.LikeResponse;
import com.musika.retrofit.model.MessageResponse;
import com.musika.retrofit.model.common.MyTrack;
import com.musika.utility.Utility;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * Created by sparken02 on 22/7/17.
 */
@SuppressLint("ValidFragment")
public class LikeFetchFragment extends Fragment implements LikeSongAdapter.OnLikeSongClickListerner, OnApiResponseListner {
    private FragmentPlayListLibraryBinding mBinding;
    private int offset = 0, limit = 7;
    private LikeSongAdapter songAdapter;
    private LinearLayoutManager linearLayoutManager;
    private String title;
    private boolean isLoading;
    private boolean isMore = true;
    private int totalCount;
    private Call<?> callLikeFetch;
    private ArrayList<LikeData> songArrayList;

    public LikeFetchFragment(String title) {
        this.title = title;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_play_list_library, container, false);
        ButterKnife.bind(this, mBinding.getRoot());
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.frTvPlayList.setText(title);
        songArrayList = new ArrayList<>();
        LikeDataEntityManager likeDataEntityManager = new LikeDataEntityManager();
        songArrayList = (ArrayList<LikeData>) likeDataEntityManager.select().asList();
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mBinding.frRvPlayList.setLayoutManager(linearLayoutManager);
        songAdapter = new LikeSongAdapter(getActivity(), this, songArrayList);
        mBinding.frRvPlayList.setAdapter(songAdapter);
//        mBinding.frRvArtistPv.setVisibility(View.VISIBLE);
//        if (Utility.haveNetworkConnection(getActivity())) {
//            callLikeFetch = ((MusicApp) getActivity().getApplication()).getApiTask()
//                    .likeFetch(new APICallback(getActivity(), APICall.LIKE_FETCH_REQ_CODE, this));
//        } else {
//            mBinding.frRvArtistProgress.setVisibility(View.GONE);
//            Utility.showRedSnackBar(getActivity(), getString(R.string.no_internet_connection));
//        }
//        pagination();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (callLikeFetch != null && !callLikeFetch.isCanceled()) {
            callLikeFetch.cancel();
        }
    }

    private void callingApi() {
        if (Utility.haveNetworkConnection(getActivity())) {
            callLikeFetch = ((MusicApp) getActivity().getApplication()).getApiTask()
                    .likeFetch(new APICallback(getActivity(), APICall.LIKE_FETCH_REQ_CODE, this));
        } else {
            mBinding.frRvArtistProgress.setVisibility(View.GONE);
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
                            && totalItemCount >= (songArrayList.size()) && isMore && songArrayList.size() < totalCount) {
                        offset += limit;
                        isLoading = true;
                        callingApi();
                    }
                }
            }
        });
    }

    @OnClick(R.id.fragment_playlist_iv_back)
    public void onBackPress() {
        getActivity().onBackPressed();
    }

    @Override
    public void onLikeSongClick(int pos) {
        MyTrack tempTrack = new MyTrack();
        tempTrack.setId(songArrayList.get(pos).getId());
        tempTrack.setTitle(songArrayList.get(pos).getTitle());
        if (songArrayList.get(pos).getArtist() != null) {
            tempTrack.setArtistId(songArrayList.get(pos).getArtist().getIdu());
            tempTrack.setArtistName(songArrayList.get(pos).getArtist().getRealname());
            tempTrack.setImage(songArrayList.get(pos).getArtist().getImage());
        }
        if (songArrayList.get(pos).getAlbum() != null) {
            tempTrack.setAlbumId(songArrayList.get(pos).getAlbum().getId());
        }
        new PlayerOptionFragment(tempTrack).show(getChildFragmentManager(), "");
    }

    @Override
    public void onArtistClick(int pos) {
        ((BaseContainer) getParentFragment()).addFragment(new
                ArtistDetailsFragment(songArrayList.get(pos).getArtist().getIdu()
                , songArrayList.get(pos).getArtist().getRealname()));

    }

    @Override
    public void onResponseComplete(Object clsGson, int requestCode) {
        isLoading = false;
        mBinding.frRvArtistProgress.setVisibility(View.GONE);
        mBinding.frRvArtistPv.setVisibility(View.GONE);
        if (requestCode == APICall.LIKE_FETCH_REQ_CODE) {
            if (clsGson instanceof LikeResponse) {
                LikeResponse likeFetchResponse = (LikeResponse) clsGson;
                totalCount = likeFetchResponse.getData().size();
                if (totalCount > 0) {
                    mBinding.fragmentLikeTvNoDataFound.setVisibility(View.GONE);
                    songArrayList.addAll(likeFetchResponse.getData());
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
        if (requestCode == APICall.LIKE_FETCH_REQ_CODE) {
            isMore = false;
            mBinding.fragmentLikeTvNoDataFound.setVisibility(View.VISIBLE);

        } else if (errorMessage instanceof MessageResponse) {
            MessageResponse messageResponse = (MessageResponse) errorMessage;
            Utility.showRedSnackBar(getActivity(), messageResponse.getMessage());
        }
    }
}

