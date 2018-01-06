package com.musika.fragment;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.musika.R;
import com.musika.databinding.FragmentPlayerSongBinding;
import com.musika.retrofit.model.common.MyTrack;
import com.musika.utility.Utility;

/**
 * Created by sparken09 on 15/09/17.
 */

@SuppressLint("ValidFragment")
public class PlayerActivityItemFragment extends Fragment {

    FragmentPlayerSongBinding mBinding;
    private MyTrack myTrack;

    public PlayerActivityItemFragment(MyTrack myTrack) {
        this.myTrack = myTrack;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_player_song, container, false);
        mBinding.playerSongName.setSelected(true);

        if (myTrack.getImage() != null) {
            Utility.loadImage(myTrack.getImage(), mBinding.playerImage, R.drawable.default_img);

        } else if (myTrack.getPicture() != null) {
            Utility.loadImage(myTrack.getPicture(), mBinding.playerImage, R.drawable.default_img);

        } else {
            mBinding.playerImage.setImageResource(R.drawable.default_img);
        }
        mBinding.playerSongName.setText(myTrack.getTitle());
        mBinding.playerArtistName.setText(myTrack.getArtistName());
        return mBinding.getRoot();
    }
}
