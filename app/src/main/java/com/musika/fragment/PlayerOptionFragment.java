package com.musika.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import com.musika.MusicApp;
import com.musika.R;
import com.musika.adapter.PlaylistAdapter;
import com.musika.container.BaseContainer;
import com.musika.databinding.FragmentPlayerOptionBinding;
import com.musika.retrofit.APICall;
import com.musika.retrofit.APICallback;
import com.musika.retrofit.OnApiResponseListner;
import com.musika.retrofit.model.AddSongArtistResponse;
import com.musika.retrofit.model.AddSongPLaylistResponse;
import com.musika.retrofit.model.MessageResponse;
import com.musika.retrofit.model.PlaylistFetchData;
import com.musika.retrofit.model.PlaylistFetchResponse;
import com.musika.retrofit.model.common.MyTrack;
import com.musika.utility.Utility;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

public class PlayerOptionFragment extends DialogFragment implements OnApiResponseListner, PlaylistAdapter.OnPlaylistClickListner {
    private FragmentPlayerOptionBinding mBinding;

    private MyTrack track;
    private Call<?> callLike,callLikeFetch,callAddSongPlaylist;;

    private int totalCount;
    private AlertDialog.Builder alertDialog ;
    private AlertDialog dialog;


    public PlayerOptionFragment() {}

    @SuppressLint("ValidFragment")
    public PlayerOptionFragment(MyTrack track) {
        this.track = track;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Black_NoTitleBar);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_player_option, container, false);
        ButterKnife.bind(this, mBinding.getRoot());
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        if (track != null) {
            mBinding.actTvSongName.setText(track.getTitle());
            mBinding.actTvArtistName.setText(track.getArtistName());
            Utility.loadImage(track.getImage(), mBinding.actIvSongImage,R.drawable.default_track);

            if (track.isLike() == true) {
                mBinding.actIvLike.setImageDrawable(getResources().getDrawable(R.drawable.likered));
                mBinding.actTvLike.setText(getString(R.string.liked));
                mBinding.actTvLike.setTextColor(getResources().getColor(R.color.btn_red));
            }
            else {
                mBinding.actIvLike.setImageDrawable(getResources().getDrawable(R.drawable.ic_like));
                mBinding.actTvLike.setText(getString(R.string.like));
                mBinding.actTvLike.setTextColor(getResources().getColor(R.color.white));
            }

            if (track.getAlbumId() > 0) {
                mBinding.actLlGoAlbum.setVisibility(View.VISIBLE);
            } else {
                mBinding.actLlGoAlbum.setVisibility(View.GONE);
            }
            if (track.getArtistId() > 0) {
                mBinding.actLlGoArtist.setVisibility(View.VISIBLE);
            } else {
                mBinding.actLlGoArtist.setVisibility(View.GONE);
            }
            mBinding.actLlDelete.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @OnClick(R.id.fragment_player_option_iv_back)
    public void onBackPress() {
        dismiss();
    }

    @OnClick(R.id.ll_add_playlist)
    public void addplaylist() {
        new SongAddPlaylist(track).show(getChildFragmentManager(), "");
    }

    @OnClick(R.id.act_ll_like)
    public void llLikes() {
        if (Utility.haveNetworkConnection(getActivity())) {

        if (mBinding.actTvLike.getText().equals("Like")) {
            mBinding.actIvLike.setImageDrawable(getResources().getDrawable(R.drawable.likered));
            mBinding.actTvLike.setText(getString(R.string.liked));
            mBinding.actTvLike.setTextColor(getResources().getColor(R.color.btn_red));
            track.setLike(true);
        }
        else {
            mBinding.actIvLike.setImageDrawable(getResources().getDrawable(R.drawable.ic_like));
            mBinding.actTvLike.setText(getString(R.string.like));
            mBinding.actTvLike.setTextColor(getResources().getColor(R.color.white));
            track.setLike(false);
        }
                callLike = ((MusicApp) getActivity().getApplication()).getApiTask().likeSong(track.getId() + "",
                        new APICallback(getActivity(), APICall.LIKE_REQ_CODE, this));
            } else {
                Utility.showRedSnackBar(getActivity(), getString(R.string.no_internet_connection));
            }
    }

    @Override
    public void onResponseComplete(Object clsGson, int requestCode) {
        if (requestCode == APICall.LIKE_REQ_CODE) {
            if (clsGson instanceof MessageResponse) {
                MessageResponse messageResponse = (MessageResponse) clsGson;
            }
        }
    }

    @Override
    public void onResponseError(Object errorMessage, int requestCode) {
        if (errorMessage instanceof MessageResponse) {
            MessageResponse messageResponse = (MessageResponse) errorMessage;
        }
    }

    @Override
    public void onPlaylistOneClick(int pos) {
        dialog.dismiss();
    }
}
