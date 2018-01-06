package com.musika.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.musika.MusicApp;
import com.musika.R;
import com.musika.adapter.PlaylistAdapter;
import com.musika.baseclass.BaseFragment;
import com.musika.container.BaseContainer;
import com.musika.databinding.FragmentPlayListLibraryBinding;
import com.musika.retrofit.APICall;
import com.musika.retrofit.APICallback;
import com.musika.retrofit.OnApiResponseListner;
import com.musika.retrofit.model.CreatePLaylistResponse;
import com.musika.retrofit.model.MessageResponse;
import com.musika.retrofit.model.PlaylistFetchDataEntityManager;
import com.musika.retrofit.model.PlaylistFetchResponse;
import com.musika.retrofit.model.PlaylistFetchData;
import com.musika.utility.Utility;
import com.musika.widget.CustomEditText;

import java.util.ArrayList;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

@SuppressLint("ValidFragment")
public class PlayListFragment extends BaseFragment implements PlaylistAdapter.OnPlaylistClickListner,OnApiResponseListner {
    private PlaylistAdapter playlistAdapter;
    private FragmentPlayListLibraryBinding mBinding;
    private String title;
    private int offset = 0, limit = 7;
    private LinearLayoutManager linearLayoutManager;
    private boolean isLoading;
    private boolean isMore = true;
    private int totalCount;
    private ArrayList<PlaylistFetchData> playlistArrayList;
    private Call<?> callFetchPlaylist,callCreatePlaylist;

    public PlayListFragment(String title) {
        this.title = title;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_play_list_library, container, false);
        ButterKnife.bind(this,mBinding.getRoot());
        return  mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.fragmentCreatePlaylist.setVisibility(View.VISIBLE);
        mBinding.frTvPlayList.setText(title);
        PlaylistFetchDataEntityManager playlistFetchDataEntityManager = new PlaylistFetchDataEntityManager();
        playlistArrayList = (ArrayList<PlaylistFetchData>) playlistFetchDataEntityManager.select().asList();
        if (playlistArrayList != null && playlistArrayList.size() == 0) {
            playlistArrayList = new ArrayList<>();
            linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            mBinding.frRvPlayList.setLayoutManager(linearLayoutManager);
            playlistAdapter = new PlaylistAdapter(getActivity(), this, playlistArrayList);
            mBinding.frRvPlayList.setAdapter(playlistAdapter);
            if (Utility.haveNetworkConnection(getActivity())) {
                mBinding.frRvArtistPv.setVisibility(View.VISIBLE);
                callFetchPlaylist = ((MusicApp) getActivity().getApplication()).getApiTask()
                        .fetchPlaylist(new APICallback(getActivity(),
                                APICall.FETCH_PLAYLIST_REQ_CODE, this));
            } else {
                Utility.showRedSnackBar(getActivity(), getString(R.string.no_internet_connection));
            }
        } else {
            linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            mBinding.frRvPlayList.setLayoutManager(linearLayoutManager);
            playlistAdapter = new PlaylistAdapter(getActivity(), this, playlistArrayList);
            mBinding.frRvPlayList.setAdapter(playlistAdapter);

        }
       //   pagination();
    }

    @OnClick(R.id.fragment_playlist_iv_back)
    public void backpress(){
        getActivity().onBackPressed();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (callFetchPlaylist != null && !callFetchPlaylist.isCanceled()) {
            callFetchPlaylist.cancel();
        }
    }

    private void callingApi() {
        if (Utility.haveNetworkConnection(getActivity())) {
            callFetchPlaylist = ((MusicApp) getActivity().getApplication()).getApiTask()
                    .fetchPlaylist(new APICallback(getActivity(),
                            APICall.FETCH_PLAYLIST_REQ_CODE, this));
        }else{
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
                            && totalItemCount >= (playlistArrayList.size()) && isMore && playlistArrayList.size() < totalCount) {
                        offset += limit;
                        isLoading = true;
                        callingApi();
                    }
                }
            }
        });
    }

    @SuppressLint("StringFormatInvalid")
    @Override
    public void onPlaylistOneClick(int pos) {
        ((BaseContainer) getParentFragment()).addFragment(
                new PlayListLibraryListFragment(playlistArrayList.get(pos).getName(),
                        playlistArrayList.get(pos).getTrackList()));
    }
    @OnClick(R.id.fragment_create_playlist)
    public void createPLaylist() {
        showChangeLangDialog();
    }

    @Override
    public void onResponseComplete(Object clsGson, int requestCode) {
        isLoading = false;
        mBinding.frRvArtistProgress.setVisibility(View.GONE);
        mBinding.frRvArtistPv.setVisibility(View.GONE);
        if (requestCode == APICall.FETCH_PLAYLIST_REQ_CODE) {
            if (clsGson instanceof PlaylistFetchResponse) {
                PlaylistFetchResponse playlistParentFetchResponse = (PlaylistFetchResponse) clsGson;
                totalCount = playlistParentFetchResponse.getCount();
                if (totalCount > 0) {
                    PlaylistFetchDataEntityManager playlistFetchDataEntityManager = new PlaylistFetchDataEntityManager();
                    playlistFetchDataEntityManager.add(playlistParentFetchResponse.getData());
                    mBinding.fragmentLikeTvNoDataFound.setVisibility(View.GONE);
                    playlistArrayList.addAll(playlistParentFetchResponse.getData());
                    playlistAdapter.notifyDataSetChanged();
                } else {
                    isMore = false;
                    mBinding.frRvPlayList.setVisibility(View.GONE);
                    mBinding.fragmentLikeTvNoDataFound.setVisibility(View.VISIBLE);
                }
            }
        }
        if (requestCode == APICall.CREATE_PLAYLIST){
            if (clsGson instanceof CreatePLaylistResponse){
                CreatePLaylistResponse createPLaylistResponse = (CreatePLaylistResponse) clsGson;
                Utility.showErrorSnackBar(mBinding.getRoot(),createPLaylistResponse.getMessage());
                playlistArrayList.clear();
                callingApi();
                playlistAdapter.notifyDataSetChanged();
//                playlistAdapter.notifyItemInserted(totalCount+1);

            }
        }
    }

    @Override
    public void onResponseError(Object errorMessage, int requestCode) {
        isLoading = false;
        mBinding.frRvArtistProgress.setVisibility(View.GONE);
        mBinding.frRvArtistPv.setVisibility(View.GONE);
        if(requestCode ==APICall.FETCH_PLAYLIST_REQ_CODE){
            isMore = false;
            mBinding.fragmentLikeTvNoDataFound.setVisibility(View.VISIBLE);
        }
        if (errorMessage instanceof MessageResponse) {
            MessageResponse messageResponse = (MessageResponse) errorMessage;
            Utility.showRedSnackBar(getActivity(),messageResponse.getMessage());
        }
    }


    public void showChangeLangDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);

        alertDialog.setTitle("Playlist");
        final CustomEditText input = new CustomEditText(getActivity());
        input.setTextColor(getResources().getColor(R.color.black));
        input.setHintTextColor(getResources().getColor(R.color.hintText));
        input.setTextSize(getResources().getDimension(R.dimen._6sdp));
        input.setPadding(10,10,10,10);
        input.setFocusable(true);
        input.setFocusableInTouchMode(true);
        input.requestFocus();
        input.setSingleLine(true);
        input.setHint(R.string.enter_playlist_name_dialog);
        alertDialog.setView(input);
        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String edtplaylistName = input.getText().toString();
                        if (edtplaylistName != null) {
                            if (Utility.haveNetworkConnection(getActivity())) {
                                callCreatePlaylist = ((MusicApp) getActivity().getApplication()).getApiTask()
                                        .createPlaylist(edtplaylistName, new APICallback(getActivity(),
                                                APICall.CREATE_PLAYLIST, PlayListFragment.this));
                            } else {
                                Utility.showRedSnackBar(getActivity(), getString(R.string.no_internet_connection));
                            }
                        } else {
                            Utility.showRedSnackBar(getActivity(), getString(R.string.enter_playlist_name));
                        }
                    }
                });

        alertDialog.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

}

