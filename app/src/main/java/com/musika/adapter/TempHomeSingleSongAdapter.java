package com.musika.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.rebound.ui.Util;
import com.musika.R;
import com.musika.databinding.RowHomeSingleSongBinding;
import com.musika.fragment.SearchFragment;
import com.musika.retrofit.model.SearchData;
import com.musika.retrofit.model.Track;
import com.musika.retrofit.model.explore.AlbumData;
import com.musika.utility.Utility;

import java.util.List;

import butterknife.OnClick;


/**
 * Created by yudizmacmini on 18/04/16.
 */
public class TempHomeSingleSongAdapter extends
        RecyclerView.Adapter<TempHomeSingleSongAdapter.Viewholder> {

    private Context context;
    private LayoutInflater inflater;
    private RowHomeSingleSongBinding mBinding;
    private OnGoingViralClickListerner onSongClickListerner;
    private List<Track> trackList;


    public TempHomeSingleSongAdapter(@NonNull Context context, List<Track> tracksList, OnGoingViralClickListerner onGoingViralClickListerner) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.trackList = tracksList;
        this.onSongClickListerner = onGoingViralClickListerner;
    }


    @Override
    public int getItemCount() {
        return trackList.size();
    }

    @Override
    public void onBindViewHolder(final Viewholder holder,int position) {
        holder.mBinding.rowHomeSingleSongTvDesc.setTag(position);
        holder.mBinding.rowHomeSingleSong.setTag(position);
        holder.mBinding.rowHomeSingleSongTvName.setText(trackList.get(position).getTitle());
        holder.mBinding.rowHomeSingleSongTvDesc.setText(trackList.get(position).getArtist().getRealname());
        if (trackList.get(position).getArtist().getImage() != null)
            Utility.loadImage(trackList.get(position).getArtist().getImage(),holder.mBinding.rowHomeSingleSongIv,R.drawable.default_track);
        else
            Utility.loadImage(trackList.get(position).getPicture(),holder.mBinding.rowHomeSingleSongIv,R.drawable.default_track);
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int arg1) {
        // TODO Auto-generated method stub
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_home_single_song, parent, false);
        return new Viewholder(mBinding, parent);
    }

    public class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private RowHomeSingleSongBinding mBinding;

        public Viewholder(RowHomeSingleSongBinding mBinding, ViewGroup parent) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
            this.mBinding.rowHomeSingleSongTvDesc.setOnClickListener(this);
            this.mBinding.rowHomeSingleSong.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            if (view == mBinding.rowHomeSingleSongTvDesc) {
                if (onSongClickListerner != null) {
                    int pos = (int) view.getTag();
                    onSongClickListerner.onGoingViralArtistClick(pos);
                }
            }
            else{
                if (onSongClickListerner != null) {
                    int pos = (int) view.getTag();
                    onSongClickListerner.onGoingViralSongClick(pos);
                }
            }

        }
    }
    public interface OnGoingViralClickListerner {
         void onGoingViralArtistClick(int pos);
         void onGoingViralSongClick(int pos);
    }

}
