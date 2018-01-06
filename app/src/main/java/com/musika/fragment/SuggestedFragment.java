package com.musika.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.musika.MusicApp;
import com.musika.R;
import com.musika.adapter.HomeSuggestedSingleSongAdapter;
import com.musika.adapter.HomeSuggestedSongListAdapter;
import com.musika.baseclass.BaseFragment;
import com.musika.container.BaseContainer;
import com.musika.databinding.FragmentSuggestionBinding;
import com.musika.retrofit.APICall;
import com.musika.retrofit.APICallback;
import com.musika.retrofit.OnApiResponseListner;

import com.musika.retrofit.model.MessageResponse;
import com.musika.retrofit.model.common.MyTrack;
import com.musika.retrofit.model.suggested.SuggestedDataBean;
import com.musika.retrofit.model.suggested.SuggestedResponse;
import com.musika.utility.Utility;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;

public class SuggestedFragment extends BaseFragment implements OnApiResponseListner,
        HomeSuggestedSingleSongAdapter.OnSongClickListerner{
    private FragmentSuggestionBinding mBinding;
    private HomeSuggestedSongListAdapter songListAdapter;
    private Call<?> callSuggetsed;
    private LinearLayoutManager linearLayoutManager;
    private List<SuggestedDataBean> suggestedDataBeanArrayList;
    private List<MyTrack> myTrackList;
    private SetBottomRaw setBottomRaw;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_suggestion, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        suggestedDataBeanArrayList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mBinding.fragmentSuggestedRvMusic.setLayoutManager(linearLayoutManager);
        songListAdapter = new HomeSuggestedSongListAdapter(getActivity(), suggestedDataBeanArrayList,this);
        mBinding.fragmentSuggestedRvMusic.setAdapter(songListAdapter);
        ViewCompat.setNestedScrollingEnabled(mBinding.fragmentSuggestedRvMusic, false);
        if (Utility.haveNetworkConnection(getActivity())) {
            mBinding.frSuggestedPv.setVisibility(View.VISIBLE);
            callSuggetsed = ((MusicApp) getActivity().getApplication()).getApiTask().suggested(new APICallback(getActivity(), APICall.SUGGESTED_REQ_CODE, this));
        } else {
            Utility.showRedSnackBar(getActivity(), getString(R.string.no_internet_connection));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (callSuggetsed != null && !callSuggetsed.isCanceled()) {
            callSuggetsed.cancel();
        }
    }
    @Override
    public void onResponseComplete(Object clsGson, int requestCode) {
        mBinding.frSuggestedPv.setVisibility(View.GONE);
        if (requestCode == APICall.SUGGESTED_REQ_CODE) {
            if (clsGson instanceof SuggestedResponse) {
                SuggestedResponse suggestedResponse = (SuggestedResponse) clsGson;
                suggestedDataBeanArrayList.addAll(suggestedResponse.getData());
                songListAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onResponseError(Object errorMessage, int requestCode) {
        mBinding.frSuggestedPv.setVisibility(View.GONE);
        mBinding.tvNoDataFound.setVisibility(View.VISIBLE);
        if (errorMessage instanceof MessageResponse) {
//            MessageResponse messageResponse = (MessageResponse) errorMessage;
//            Utility.showRedSnackBar(getActivity(), messageResponse.getMessage());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onSongClick(int posraw, int pos) {
        setBottomRaw = (SetBottomRaw) getActivity();
        myTrackList = new ArrayList<>();
        for (int i = 0; i <suggestedDataBeanArrayList.get(posraw).getData().size() ; i++) {
            MyTrack myTrack = new MyTrack();
            myTrack.setId(suggestedDataBeanArrayList.get(posraw).getData().get(i).getId());
            myTrack.setName(suggestedDataBeanArrayList.get(posraw).getData().get(i).getName());
            myTrack.setTitle(suggestedDataBeanArrayList.get(posraw).getData().get(i).getTitle());
            if (suggestedDataBeanArrayList.get(posraw).getData().get(i).getArtist().getIdu() != null)
                myTrack.setArtistId(suggestedDataBeanArrayList.get(posraw).getData().get(i).getArtist().getIdu());
            myTrack.setArtistName(suggestedDataBeanArrayList.get(posraw).getData().get(i).getArtist().getRealname());
            myTrack.setImage(suggestedDataBeanArrayList.get(posraw).getData().get(i).getArtist().getImage());
            myTrack.setShareUrl(suggestedDataBeanArrayList.get(posraw).getData().get(i).getShare_url());
            myTrack.setLike(suggestedDataBeanArrayList.get(posraw).getData().get(i).isLiked());
            myTrack.setTotalLike(suggestedDataBeanArrayList.get(posraw).getData().get(i).getLikes());
            myTrack.setTotalPlay(suggestedDataBeanArrayList.get(posraw).getData().get(i).getPlays());
            myTrack.setPicture(suggestedDataBeanArrayList.get(posraw).getData().get(i).getPicture());

            myTrackList.add(i,myTrack);
        }
        setBottomRaw.onSetBottom(myTrackList,pos);
    }

    @Override
    public void onArtistClick(int posraw, int pos) {
        ((BaseContainer) getParentFragment()).addFragment(new
                ArtistDetailsFragment(suggestedDataBeanArrayList.get(posraw).getData().get(pos).getArtist().getIdu()
                , suggestedDataBeanArrayList.get(posraw).getData().get(pos).getArtist().getRealname()));


    }

    public interface SetBottomRaw{
         void onSetBottom(List<MyTrack> myTracks, int pos);
    }
}