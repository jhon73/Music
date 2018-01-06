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
import com.musika.adapter.MoreTrackAdapter;
import com.musika.container.BaseContainer;
import com.musika.databinding.FragmentPlayListLibraryBinding;
import com.musika.retrofit.APICall;
import com.musika.retrofit.APICallback;
import com.musika.retrofit.OnApiResponseListner;
import com.musika.retrofit.model.MessageResponse;
import com.musika.retrofit.model.common.MyTrack;
import com.musika.retrofit.model.genrictracks.GenericsResponse;
import com.musika.retrofit.model.genrictracks.GenresDataBean;
import com.musika.utility.Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;


@SuppressLint("ValidFragment")
public class TrackFragment extends Fragment implements MoreTrackAdapter.OnSongClickListerner, OnApiResponseListner, View.OnClickListener {

    private FragmentPlayListLibraryBinding mBinding;
    private int offset = 0, limit = 7;
    private MoreTrackAdapter songAdapter;
    private LinearLayoutManager linearLayoutManager;
    private String title;
    private boolean isLoading;
    private boolean isMore = true;
    private Call<?> callTrackByGeneres;
    private ArrayList<GenresDataBean> songArrayList;
    private List<MyTrack> myTrackList;
    private SetBottomRaw setBottomRaw;

    public TrackFragment(String title) {
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
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mBinding.frRvPlayList.setLayoutManager(linearLayoutManager);
        songAdapter = new MoreTrackAdapter(getActivity(), songArrayList, this);
        mBinding.frRvPlayList.setAdapter(songAdapter);
        mBinding.frRvArtistPv.setVisibility(View.VISIBLE);
        if (Utility.haveNetworkConnection(getActivity())) {
            mBinding.frRvArtistPv.setVisibility(View.VISIBLE);
            callTrackByGeneres = ((MusicApp) getActivity().getApplication()).getApiTask()
                    .trackGeneres(title,offset,limit, new APICallback(getActivity(), APICall.TRACKBYGENERES, this));
        } else {
            Utility.showRedSnackBar(getActivity(), getString(R.string.no_internet_connection));
        }
        pagination();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (callTrackByGeneres != null && !callTrackByGeneres.isCanceled()) {
            callTrackByGeneres.cancel();
        }
    }

    private void callingApi() {
        if (Utility.haveNetworkConnection(getActivity())) {
            mBinding.frRvArtistProgress.setVisibility(View.VISIBLE);
            callTrackByGeneres = ((MusicApp) getActivity().getApplication()).getApiTask()
                    .trackGeneres(title,offset,limit, new APICallback(getActivity(), APICall.TRACKBYGENERES, this));
        } else {
            mBinding.frRvArtistProgress.setVisibility(View.GONE);
            Utility.showRedSnackBar(getActivity() ,getString(R.string.no_internet_connection));
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
                            && totalItemCount >= (songArrayList.size()) && isMore )
                    {
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
    public void onResponseComplete(Object clsGson, int requestCode) {
        isLoading = false;
        mBinding.frRvArtistProgress.setVisibility(View.GONE);
        mBinding.frRvArtistPv.setVisibility(View.GONE);
        if (requestCode == APICall.TRACKBYGENERES) {
            if (clsGson instanceof GenericsResponse) {
                GenericsResponse trackFetchResponse = (GenericsResponse) clsGson;
                if (trackFetchResponse.getData().getData().size() > 0) {
                    mBinding.fragmentLikeTvNoDataFound.setVisibility(View.GONE);
                    songArrayList.addAll(trackFetchResponse.getData().getData());
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
        if (requestCode == APICall.TRACKBYGENERES) {
            isMore = false;
            if(songArrayList.size()==0)
            mBinding.fragmentLikeTvNoDataFound.setVisibility(View.VISIBLE);

        } else if (errorMessage instanceof MessageResponse) {
            MessageResponse messageResponse = (MessageResponse) errorMessage;
            Utility.showRedSnackBar(getActivity(), messageResponse.getMessage());
        }
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onSongMoreClick(int pos) {

        if (songArrayList.get(pos) != null) {
            MyTrack tempTrack = new MyTrack();
            tempTrack.setId(songArrayList.get(pos).getId());
            tempTrack.setTitle(songArrayList.get(pos).getTitle());
            if (songArrayList.get(pos).getArtist() != null) {
                tempTrack.setArtistId(songArrayList.get(pos).getArtist().getIdu());
                tempTrack.setArtistName(songArrayList.get(pos).getArtist().getRealname());
                tempTrack.setImage(songArrayList.get(pos).getArtist().getImage());
            }
            new PlayerOptionFragment(tempTrack).show(getChildFragmentManager(), "");
        }

    }

    @Override
    public void onArtistClick(int pos) {
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
    public interface SetBottomRaw{
        void onSetBottom(List<MyTrack> myTracks, int pos);
    }
}



