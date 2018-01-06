package com.musika.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.musika.MusicApp;
import com.musika.R;
import com.musika.adapter.LikeSongAdapter;
import com.musika.adapter.PlaylistAdapter;
import com.musika.databinding.SongAddFragmentdialogBinding;
import com.musika.retrofit.APICall;
import com.musika.retrofit.APICallback;
import com.musika.retrofit.OnApiResponseListner;
import com.musika.retrofit.model.AddSongArtistResponse;
import com.musika.retrofit.model.AddSongPLaylistResponse;
import com.musika.retrofit.model.CreatePLaylistResponse;
import com.musika.retrofit.model.MessageResponse;
import com.musika.retrofit.model.PlaylistFetchData;
import com.musika.retrofit.model.PlaylistFetchResponse;
import com.musika.retrofit.model.common.MyTrack;
import com.musika.utility.Utility;
import com.musika.widget.CustomEditText;
import com.rey.material.app.DialogFragment;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * Created by sparken09 on 07/09/17.
 */

@SuppressLint("ValidFragment")
public class SongAddPlaylist extends android.support.v4.app.DialogFragment implements OnApiResponseListner, PlaylistAdapter.OnPlaylistClickListner {

    private SongAddFragmentdialogBinding mBinding;
    private MyTrack track;
    private PlaylistAdapter playlistAdapter;
    private RecyclerView myRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<PlaylistFetchData> playlistArrayList;
    private Call<?> callFetchPlaylist,callAddSongPlaylist,callCreatePlaylist;
    private int totalCount;

    public SongAddPlaylist(MyTrack track) {
        this.track = track;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.song_add_fragmentdialog, container,false);
        ButterKnife.bind(this, mBinding.getRoot());
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        playlistArrayList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mBinding.recyclerAddSong.setLayoutManager(linearLayoutManager);
        playlistAdapter = new PlaylistAdapter(getActivity(), this, playlistArrayList);
        mBinding.recyclerAddSong.setAdapter(playlistAdapter);
        ViewCompat.setNestedScrollingEnabled(mBinding.recyclerAddSong, false);


        if (Utility.haveNetworkConnection(getActivity())) {
            callFetchPlaylist = ((MusicApp) getActivity().getApplication()).getApiTask()
                    .fetchPlaylist(new APICallback(getActivity(),
                            APICall.FETCH_PLAYLIST_REQ_CODE, this));
        }else{
            Utility.showRedSnackBar(getActivity(), getString(R.string.no_internet_connection));
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

    @OnClick(R.id.fragment_playlist_iv_back)
    public void onBackPress(){
        dismiss();
    }
    @OnClick(R.id.new_playlist_addsong)
    public void addNewPlaylist(){
        showChangeLangDialog();
    }
    @OnClick(R.id.cancel_addsong)
    public void caneldAddsong(){
     dismiss();
    }

    @Override
    public void onPause() {
        super.onPause();
//        if (callArtist != null && !callArtist.isCanceled()) {
//            callArtist.cancel();
//        }
    }

    @Override
    public void onResponseComplete(Object clsGson, int requestCode) {
        if (requestCode == APICall.FETCH_PLAYLIST_REQ_CODE) {
            if (clsGson instanceof PlaylistFetchResponse) {
                PlaylistFetchResponse playlistParentFetchResponse = (PlaylistFetchResponse) clsGson;
                totalCount = playlistParentFetchResponse.getCount();
                if (totalCount > 0) {
                    playlistArrayList.clear();
                    playlistArrayList.addAll(playlistParentFetchResponse.getData());
                    playlistAdapter.notifyDataSetChanged();
                }
            }
        }

        if (requestCode == APICall.ADDSONG_PLAYLIST){
            if (clsGson instanceof AddSongArtistResponse){

            }
        }
        if (requestCode == APICall.CREATE_PLAYLIST){
            if (clsGson instanceof CreatePLaylistResponse){
               playlistArrayList.clear();
                callingApi();
                playlistAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onResponseError(Object errorMessage, int requestCode) {
        if (errorMessage instanceof MessageResponse) {
            MessageResponse messageResponse = (MessageResponse) errorMessage;
            Utility.showErrorSnackBar(mBinding.getRoot(), messageResponse.getMessage());
        } else {
            Utility.showErrorSnackBar(mBinding.getRoot(), errorMessage.toString());
        }
    }

    @Override
    public void onPlaylistOneClick(int pos) {
        dismiss();
        if (Utility.haveNetworkConnection(getActivity())) {
            callAddSongPlaylist = ((MusicApp) getActivity().getApplication()).getApiTask()
                    .addsong_playlist(playlistArrayList.get(pos).getId(),track.getId(),new APICallback(getActivity(),
                            APICall.ADDSONG_PLAYLIST, this));
        }else{
            Utility.showRedSnackBar(getActivity(), getString(R.string.no_internet_connection));
        }
    }
    public void showChangeLangDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity(),R.style.MyDialogTheme);
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
                        String  edtplaylistName = input.getText().toString();
                        if (edtplaylistName != null) {
                            if (Utility.haveNetworkConnection(getActivity())) {
                                callCreatePlaylist = ((MusicApp) getActivity().getApplication()).getApiTask()
                                        .createPlaylist(edtplaylistName, new APICallback(getActivity(),
                                                APICall.CREATE_PLAYLIST, SongAddPlaylist.this));
                            } else {
                                Utility.showRedSnackBar(getActivity(), getString(R.string.no_internet_connection));
                            }
                        }else{
                            Utility.showRedSnackBar(getActivity(),getString(R.string.enter_playlist_name));
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
