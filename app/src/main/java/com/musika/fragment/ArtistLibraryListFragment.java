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
import com.musika.adapter.ArtistListAdapter;
import com.musika.container.BaseContainer;
import com.musika.databinding.FragmentArtistListLibraryBinding;
import com.musika.retrofit.APICall;
import com.musika.retrofit.APICallback;
import com.musika.retrofit.OnApiResponseListner;
import com.musika.retrofit.model.MessageResponse;
import com.musika.retrofit.model.SearchData;
import com.musika.retrofit.model.SearchResponse;
import com.musika.utility.Utility;
import java.util.ArrayList;
import java.util.List;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

public class ArtistLibraryListFragment extends Fragment implements View.OnClickListener, OnApiResponseListner {
    private FragmentArtistListLibraryBinding mBinding;
    private int offset = 0, limit = 7;
    private boolean isLoading;
    private boolean isMore = true;
    private int totalCount;
    private Call<?> callArtist;
    private LinearLayoutManager linearLayoutManager;
    private List<SearchData> searchDataList;
    private ArtistListAdapter artistAdapter;
    private SearchResponse searchResponse;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_artist_list_library, container, false);
        ButterKnife.bind(this, mBinding.getRoot());
        View view = mBinding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchDataList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mBinding.frRvArtistList.setLayoutManager(linearLayoutManager);
        artistAdapter = new ArtistListAdapter(getActivity(), searchDataList, this);
        mBinding.frRvArtistList.setAdapter(artistAdapter);
        mBinding.frRvArtistPv.setVisibility(View.VISIBLE);
        if (Utility.haveNetworkConnection(getActivity())) {
            callArtist = ((MusicApp) getActivity().getApplication()).getApiTask().getArtistList(offset, limit
                    , new APICallback(getActivity(), APICall.ARTISTS, this));
        } else {
            Utility.showRedSnackBar(getActivity(), getString(R.string.no_internet_connection));
        }
        pagination();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (callArtist != null && !callArtist.isCanceled()) {
            callArtist.cancel();
        }
    }

    private void callingArtistApi() {
        if (Utility.haveNetworkConnection(getActivity())) {
            mBinding.frRvArtistProgress.setVisibility(View.VISIBLE);
            callArtist = ((MusicApp) getActivity().getApplication()).getApiTask().getArtistList(offset, limit
                    , new APICallback(getActivity(), APICall.ARTISTS, this));
        } else {
            mBinding.frRvArtistProgress.setVisibility(View.GONE);
            Utility.showRedSnackBar(getActivity(), getString(R.string.no_internet_connection));
        }
    }

    private void pagination() {
        mBinding.frRvArtistList.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                            && totalItemCount >= (searchDataList.size()) && isMore && searchDataList.size() < totalCount)
                    {
                        offset += limit;
                        isLoading = true;
                        callingArtistApi();
                    }
                }
            }
        });
    }

    @OnClick(R.id.fragment_artist_list_artist_iv_back)
    public void onBackPress() {
        getActivity().onBackPressed();
    }

    @Override
    public void onClick(View view) {
        int pos = (int) view.getTag();
        ((BaseContainer) getParentFragment()).addFragment(new ArtistDetailsFragment(searchDataList.get(pos).getId(),
                searchDataList.get(pos).getRealname()));

    }

    @Override
    public void onResponseComplete(Object clsGson, int requestCode) {
        isLoading = false;
        mBinding.frRvArtistProgress.setVisibility(View.GONE);
        mBinding.frRvArtistPv.setVisibility(View.GONE);
        if (requestCode == APICall.ARTISTS) {
            if (clsGson instanceof SearchResponse) {
                searchResponse = (SearchResponse) clsGson;
                totalCount = searchResponse.getCount();
                if (searchResponse.getSearchDatas().size() > 0) {
                    searchDataList.addAll(searchResponse.getSearchDatas());
                    artistAdapter.notifyDataSetChanged();
                } else {
                    isMore = false;
                }
            }

        }
    }

    @Override
    public void onResponseError(Object errorMessage, int requestCode) {
        isLoading = false;
        mBinding.frRvArtistProgress.setVisibility(View.GONE);
        mBinding.frRvArtistPv.setVisibility(View.GONE);
        if (requestCode == APICall.ARTISTS) {
            isMore = false;
        }
        if (errorMessage instanceof MessageResponse) {
//            mBinding.fragmentArtistNoDataFound.setVisi

        } else {
//            Utility.showRedSnackBar(getActivity(), errorMessage.toString());
        }
    }
}
