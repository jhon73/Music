package com.musika.fragment;


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
import com.musika.adapter.AlbumDataAdapter;
import com.musika.container.BaseContainer;
import com.musika.databinding.FragmentAlbumListLibraryBinding;
import com.musika.retrofit.APICall;
import com.musika.retrofit.APICallback;
import com.musika.retrofit.OnApiResponseListner;
import com.musika.retrofit.model.AlbumResponse;
import com.musika.retrofit.model.MessageResponse;
import com.musika.retrofit.model.explore.AlbumData;
import com.musika.utility.Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;


public class AlbumLibraryListFragment extends Fragment implements View.OnClickListener, OnApiResponseListner, AlbumDataAdapter.OnAlbumClickListner {


    private int offset = 0, limit = 7;
    private boolean isLoading;
    private boolean isMore = true;
    private int totalCount;
    private Call<?> callAlbums;
    private LinearLayoutManager linearLayoutManager;
    private List<AlbumData> albumDataList;
    private AlbumResponse albumResponse;
    private AlbumDataAdapter albumAdapter;
    private FragmentAlbumListLibraryBinding mBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_album_list_library, container, false);
        ButterKnife.bind(this, mBinding.getRoot());
        View view = mBinding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        albumDataList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mBinding.frRvAlbumList.setLayoutManager(linearLayoutManager);
        albumAdapter = new AlbumDataAdapter(getActivity(), albumDataList, this);
        mBinding.frRvAlbumList.setAdapter(albumAdapter);
        if (Utility.haveNetworkConnection(getActivity())) {
            mBinding.frRvArtistPv.setVisibility(View.VISIBLE);
            callAlbums = ((MusicApp) getActivity().getApplication()).getApiTask().getAlbumsList(offset, limit, new APICallback(getActivity(), APICall.ALBUMS, this));
        } else {
            Utility.showRedSnackBar(getActivity(), getString(R.string.no_internet_connection));
        }
        pagination();

    }

    private void callingArtistApi() {
        if (Utility.haveNetworkConnection(getActivity())) {
            callAlbums = ((MusicApp) getActivity().getApplication()).getApiTask().getAlbumsList(offset, limit, new APICallback(getActivity(), APICall.ALBUMS, this));
        } else {
            Utility.showRedSnackBar(getActivity(), getString(R.string.no_internet_connection));
        }
    }

    @OnClick(R.id.fragment_album_list_album_iv_back)
    public void onBackPress() {
        getActivity().onBackPressed();
    }

    private void pagination() {
        mBinding.frRvAlbumList.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                            && totalItemCount >= (albumDataList.size()) && isMore && albumDataList.size() < totalCount) {
                        offset += limit;
                        isLoading = true;
                        callingArtistApi();
                    }
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (callAlbums != null && !callAlbums.isCanceled()) {
            callAlbums.cancel();
        }
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public void onResponseComplete(Object clsGson, int requestCode) {
        isLoading = false;
        mBinding.frRvArtistProgress.setVisibility(View.GONE);
        mBinding.frRvArtistPv.setVisibility(View.GONE);
        if (requestCode == APICall.ALBUMS) {
            if (clsGson instanceof AlbumResponse) {
                albumResponse = (AlbumResponse) clsGson;
                totalCount = albumResponse.getCount();
                if (totalCount > 0) {
                    albumDataList.addAll(albumResponse.getData());
                    albumAdapter.notifyDataSetChanged();
                } else {
                    isMore = false;
                    mBinding.frRvAlbumList.setVisibility(View.GONE);
                    mBinding.fragmentAlbumListAlbumNoDataFound.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void onResponseError(Object errorMessage, int requestCode) {
        isLoading = false;
        mBinding.frRvArtistProgress.setVisibility(View.GONE);
        mBinding.frRvArtistPv.setVisibility(View.GONE);
        if (requestCode == APICall.ALBUMS) {
            isMore = false;
        }
        if (errorMessage instanceof MessageResponse) {
            mBinding.fragmentAlbumListAlbumNoDataFound.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAlbumlistOneClick(int pos) {
        ((BaseContainer) getParentFragment()).addFragment(
                new AlbumDetailsFragment(albumDataList.get(pos).getId(),
                        albumDataList.get(pos).getName(), albumDataList.get(pos).getArtist().getRealname()));
    }
}
