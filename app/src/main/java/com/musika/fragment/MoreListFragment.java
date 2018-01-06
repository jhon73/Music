package com.musika.fragment;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.musika.R;
import com.musika.adapter.MoreSongAdapter;
import com.musika.databinding.FragmentMoreSongListBinding;
import com.musika.retrofit.model.common.MyTrack;
import com.musika.retrofit.model.explore.Albums;
import com.musika.retrofit.model.home.OtherRowData;
import com.musika.retrofit.model.home.OtherRowResponse;
import com.musika.retrofit.model.searchscreen.GeneresData;

import butterknife.OnClick;

@SuppressLint("ValidFragment")
public class MoreListFragment extends Fragment implements View.OnClickListener,MoreSongAdapter.OnSongClickListerner {

    private FragmentMoreSongListBinding mBinding;
    private OtherRowResponse artistData;
    private MoreSongAdapter adapter;
    public MoreListFragment(OtherRowResponse artistData) {
        this.artistData = artistData;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_more_song_list, container, false);
          return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.fragmentMoreSongTv.setText(artistData.getTitle());
        mBinding.fragmentMoreSongRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter=new MoreSongAdapter(getActivity(),artistData.getData(),this);
        mBinding.fragmentMoreSongRv.setAdapter(adapter);
    }

    @OnClick(R.id.fragment_more_song_iv_back)
    public void onBackPress(){
        getActivity().onBackPressed();
    }



    @Override
    public void onClick(View view) {

    }

    @Override
    public void onSongClick(int pos) {
        MyTrack tempTrack = new MyTrack();
        tempTrack.setId(artistData.getData().get(pos).getId());
        tempTrack.setTitle(artistData.getData().get(pos).getTitle());
        if (artistData.getData().get(pos).getArtist() != null) {
            tempTrack.setArtistId(artistData.getData().get(pos).getArtist().getIdu());
            tempTrack.setArtistName(artistData.getData().get(pos).getArtist().getRealname());
            tempTrack.setImage(artistData.getData().get(pos).getArtist().getImage());
        }
        if (artistData.getData().get(pos).getAlbum() != null) {
            tempTrack.setAlbumId(artistData.getData().get(pos).getAlbum().getId());
        }
        new PlayerOptionFragment(tempTrack).show(getChildFragmentManager(), "");

    }
}
