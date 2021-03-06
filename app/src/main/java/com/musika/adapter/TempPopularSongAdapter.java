package com.musika.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.musika.R;
import com.musika.databinding.RowPopularSongBinding;
import com.musika.retrofit.model.RecentPlayData;
import com.musika.retrofit.model.Track;
import com.musika.utility.Utility;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by chirag on 30-Jun-17.
 */

public class TempPopularSongAdapter extends RecyclerView.Adapter<TempPopularSongAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private RowPopularSongBinding mBinding;
    private OnSongClickListerner onSongClickListerner;
    private ArrayList<RecentPlayData> songArrayList;

    public TempPopularSongAdapter(Context context, OnSongClickListerner onSongClickListerner, ArrayList<RecentPlayData> songArrayList) {
        this.context = context;
        this.onSongClickListerner = onSongClickListerner;
        this.songArrayList = songArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_popular_song, parent, false);
        return new ViewHolder(mBinding, parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RecentPlayData objFetchSong = songArrayList.get(position);
        holder.mBinding.rowPopularSongTvTitle.setText(objFetchSong.getTitle());
        holder.mBinding.rowPopularSongTvArtist.setText(objFetchSong.getArtist().getRealname());
        Utility.loadImage(objFetchSong.getArtist().getImage(), holder.mBinding.rowPopularSongTvImage, R.drawable.default_track);
        holder.mBinding.ivMore.setTag(position);
        holder.mBinding.rowPopularSongTvArtist.setTag(position);
        holder.mBinding.listSongMain.setTag(position);
    }

    @Override
    public int getItemCount() {
        return songArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RowPopularSongBinding mBinding;

        public ViewHolder(RowPopularSongBinding mBinding, ViewGroup parent) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
            this.mBinding.ivMore.setOnClickListener(this);
            this.mBinding.rowPopularSongTvArtist.setOnClickListener(this);
            this.mBinding.listSongMain.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view==mBinding.rowPopularSongTvArtist) {
                if (onSongClickListerner != null) {
                    int pos = (int) view.getTag();
                    onSongClickListerner.onArtistclick(pos);
                }
            }
            else if (view == mBinding.ivMore){
                if (onSongClickListerner != null) {
                    int pos = (int) view.getTag();
                    onSongClickListerner.onSongMoreClick(pos);
                }
            }
            else{
                if (onSongClickListerner != null) {
                    int pos = (int) view.getTag();
                    onSongClickListerner.onSongClick(pos);
                }
            }
        }
    }

    public interface OnSongClickListerner {
        void onSongMoreClick(int pos);
        void onArtistclick(int pos);
        void onSongClick(int pos);

    }
}
