package com.musika.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.musika.MusicApp;
import com.musika.R;
import com.musika.adapter.RowSearchSongAdapter;
import com.musika.adapter.SearchAdapter;
import com.musika.adapter.TempHomeSingleSongAdapter;
import com.musika.container.BaseContainer;
import com.musika.databinding.FragmentSearchBinding;
import com.musika.retrofit.APICall;
import com.musika.retrofit.APICallback;
import com.musika.retrofit.OnApiResponseListner;
import com.musika.retrofit.model.MessageResponse;
import com.musika.retrofit.model.SearchData;
import com.musika.retrofit.model.SearchResponse;
import com.musika.retrofit.model.Track;
import com.musika.retrofit.model.common.MyTrack;
import com.musika.retrofit.model.searchscreen.GeneresData;
import com.musika.retrofit.model.searchscreen.SearchScreenResponse;
import com.musika.retrofit.model.searchscreen.Tracks;
import com.musika.utility.Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;

public class SearchFragment extends Fragment implements View.OnClickListener, RowSearchSongAdapter.OnSongClickListerner, TextWatcher, OnApiResponseListner, View.OnTouchListener, TempHomeSingleSongAdapter.OnGoingViralClickListerner {

    private FragmentSearchBinding mBinding;
    private TempHomeSingleSongAdapter goingViralAdapter;
    private SearchAdapter generesAdapter;
    private RowSearchSongAdapter searchSongAdapter;
    private List<SearchData> searchDataList;
    private List<GeneresData> generesDataList;
    private List<Track> tracksList;
    private List<MyTrack> myTrackList;
    private Call<?> callSearch;
    private Call<?> callSearchScreen;
    private SetBottomRaw setBottomRaw;
    

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        ButterKnife.bind(this, mBinding.getRoot());
        setData();
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (Utility.haveNetworkConnection(getActivity())) {
            callSearchScreen = ((MusicApp) getActivity().getApplication()).getApiTask()
                    .searchScreeen(new APICallback(getActivity(), APICall.SEARCH_SCREEN, this));
        } else {
            mBinding.fragmentSearchRvViral.setVisibility(View.GONE);
            Utility.showRedSnackBar(getActivity(), getString(R.string.no_internet_connection));
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (callSearch != null && !callSearch.isCanceled()){
            callSearch.cancel();
        }
        if(callSearchScreen !=null && !callSearchScreen.isCanceled()) {
            callSearchScreen.cancel();
        }
    }
    private void setData() {
        searchDataList = new ArrayList<>();
        generesDataList = new ArrayList<>();


        generesAdapter = new SearchAdapter(getActivity(),generesDataList, this);
        mBinding.fragmentSearchRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.fragmentSearchRv.setAdapter(generesAdapter);
        tracksList=new ArrayList<>();
        goingViralAdapter = new TempHomeSingleSongAdapter(getActivity(),tracksList,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mBinding.fragmentSearchRvViral.setLayoutManager(layoutManager);
        mBinding.fragmentSearchRvViral.setAdapter(goingViralAdapter);

        searchSongAdapter = new RowSearchSongAdapter(getActivity(), searchDataList, this);
        mBinding.fragmentSearchRvSearch.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.fragmentSearchRvSearch.setAdapter(searchSongAdapter);

        mBinding.fragmentSearchEd.addTextChangedListener(this);
        mBinding.fragmentSearchRv.setOnTouchListener(this);
        mBinding.fragmentSearchMain.setOnTouchListener(this);
        ViewCompat.setNestedScrollingEnabled(mBinding.fragmentSearchRvViral, false);
        ViewCompat.setNestedScrollingEnabled(mBinding.fragmentSearchRv, false);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if ( id == R.id.row_search_main) {
            int pos = (int) view.getTag();
            ((BaseContainer) getParentFragment()).addFragment(new TrackFragment(generesDataList.get(pos).getName()));

        }
        if (mBinding.fragmentSearchRvSearch.getVisibility() == View.VISIBLE) {
            searchDataList.clear();
            mBinding.fragmentSearchProgress.setVisibility(View.GONE);
            searchSongAdapter.notifyDataSetChanged();
            mBinding.fragmentSearchRvSearch.setVisibility(View.GONE);
            mBinding.fragmentSearchTvMsg.setVisibility(View.GONE);
            mBinding.fragmentSearchEd.setText("");
        } else if (mBinding.fragmentSearchTvMsg.getVisibility() == View.VISIBLE) {
            searchDataList.clear();
            mBinding.fragmentSearchProgress.setVisibility(View.GONE);
            searchSongAdapter.notifyDataSetChanged();
            mBinding.fragmentSearchRvSearch.setVisibility(View.GONE);
            mBinding.fragmentSearchTvMsg.setVisibility(View.GONE);
            mBinding.fragmentSearchEd.setText("");
        } else {
        }
    }

    @Override
    public void onSongClick(int pos) {

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.length() > 2) {
            callingApi();
        } else {
            mBinding.fragmentSearchRvSearch.setVisibility(View.GONE);
            searchDataList.clear();
            searchSongAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }

    private void callingApi() {
        if (Utility.haveNetworkConnection(getActivity())) {

            mBinding.fragmentSearchProgress.setVisibility(View.VISIBLE);
            if (callSearch != null && !callSearch.isCanceled())
                callSearch.cancel();
            callSearch = ((MusicApp) getActivity().getApplication()).getApiTask().searchSong(mBinding.fragmentSearchEd.getText().toString().trim(),
                    new APICallback(getActivity(), APICall.SEARCH_CODE, this));
        } else {
            mBinding.fragmentSearchProgress.setVisibility(View.GONE);
            Utility.showRedSnackBar(getActivity(), getString(R.string.no_internet_connection));
        }
    }

    @Override
    public void onResponseComplete(Object clsGson, int requestCode) {
        mBinding.fragmentSearchProgress.setVisibility(View.GONE);
        if (requestCode == APICall.SEARCH_CODE) {
            if (clsGson instanceof SearchResponse) {
                SearchResponse searchResponse = (SearchResponse) clsGson;
                if (searchResponse.getSearchDatas().size() > 0) {
                    mBinding.fragmentSearchRvSearch.setVisibility(View.VISIBLE);
                    mBinding.fragmentSearchTvMsg.setVisibility(View.GONE);
                    searchDataList.clear();
                    searchDataList.addAll(searchResponse.getSearchDatas());
                    searchSongAdapter.notifyDataSetChanged();
                } else {
                    mBinding.fragmentSearchRvSearch.setVisibility(View.GONE);
                    mBinding.fragmentSearchTvMsg.setVisibility(View.VISIBLE);
                }
            }
        }

        if (requestCode == APICall.SEARCH_SCREEN){
            if (clsGson instanceof SearchScreenResponse) {
                SearchScreenResponse searchScreenResponse = (SearchScreenResponse) clsGson;
                mBinding.tvGeneres.setText(searchScreenResponse.getData().getGeneres().getTitle());
                mBinding.tvGoingViral.setText(searchScreenResponse.getData().getTracks().getTitle());

                if (searchScreenResponse.getData().getGeneres().getData().size() > 0) {
                    generesDataList.addAll(searchScreenResponse.getData().getGeneres().getData());
                    generesAdapter.notifyDataSetChanged();
                }
                else{
                    mBinding.fragmentSearchRv.setVisibility(View.GONE);
                }
                if (searchScreenResponse.getData().getTracks().getData().size() > 0){
                    tracksList.addAll(searchScreenResponse.getData().getTracks().getData());
                    goingViralAdapter.notifyDataSetChanged();
                }
                else{
                    mBinding.fragmentSearchRvViral.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void onResponseError(Object errorMessage, int requestCode) {
        if (!errorMessage.equals("Socket closed") && requestCode == APICall.SEARCH_CODE) {
            mBinding.fragmentSearchProgress.setVisibility(View.GONE);
        }
        if (requestCode == APICall.SEARCH_CODE) {
            searchDataList.clear();
            mBinding.fragmentSearchProgress.setVisibility(View.GONE);
            searchSongAdapter.notifyDataSetChanged();
            mBinding.fragmentSearchRvSearch.setVisibility(View.GONE);
            mBinding.fragmentSearchTvMsg.setVisibility(View.VISIBLE);
        }
        if (requestCode != APICall.SEARCH_CODE && requestCode != APICall.SEARCH_SCREEN && errorMessage instanceof MessageResponse) {
            MessageResponse messageResponse = (MessageResponse) errorMessage;
            Utility.showRedSnackBar(getActivity(), messageResponse.getMessage());
        }
        if (requestCode == APICall.SEARCH_SCREEN){
            generesDataList.clear();
            generesAdapter.notifyDataSetChanged();
            mBinding.fragmentSearchRv.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        searchDataList.clear();
        mBinding.fragmentSearchProgress.setVisibility(View.GONE);
        searchSongAdapter.notifyDataSetChanged();
        mBinding.fragmentSearchRvSearch.setVisibility(View.GONE);
        return false;
    }

    @Override
    public void onGoingViralArtistClick(int pos) {
        ((BaseContainer) getParentFragment()).addFragment(new ArtistDetailsFragment(tracksList.get(pos).getArtist().getIdu(),
                tracksList.get(pos).getArtist().getRealname()));
    }

    @Override
    public void onGoingViralSongClick(int pos) {
        setBottomRaw = (SetBottomRaw) getActivity();
        myTrackList = new ArrayList<>();
        for (int i = 0; i <tracksList.size() ; i++) {
            MyTrack myTrack = new MyTrack();
            myTrack.setId(tracksList.get(i).getId());
            myTrack.setName(tracksList.get(i).getName());
            myTrack.setTitle(tracksList.get(i).getTitle());
            if (tracksList.get(i).getArtist().getIdu() != null)
                myTrack.setArtistId(tracksList.get(i).getArtist().getIdu());
            myTrack.setArtistName(tracksList.get(i).getArtist().getRealname());
            myTrack.setImage(tracksList.get(i).getArtist().getImage());
            myTrack.setShareUrl(tracksList.get(i).getShare_url());
            myTrack.setLike(tracksList.get(i).getLiked());
            myTrack.setTotalLike(tracksList.get(i).getTotallikes());
            myTrack.setTotalPlay(tracksList.get(i).getTotalplays());
            myTrack.setPicture(tracksList.get(i).getPicture());

            myTrackList.add(i,myTrack);
        }
        setBottomRaw.onSetBottom(myTrackList,pos);
    }
    public interface SetBottomRaw{
        void onSetBottom(List<MyTrack> myTracks, int pos);
    }
}