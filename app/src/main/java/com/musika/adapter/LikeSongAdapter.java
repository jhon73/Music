package com.musika.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.musika.R;
import com.musika.databinding.RowPopularSongBinding;
import com.musika.retrofit.model.LikeData;
import com.musika.utility.Utility;

import java.util.ArrayList;

/**
 * Created by chirag on 30-Jun-17.
 */

public class LikeSongAdapter extends RecyclerView.Adapter<LikeSongAdapter.ViewHolder> {
    private Context context;
    private RowPopularSongBinding mBinding;
    private OnLikeSongClickListerner onLikeSongClickListerner;
    private ArrayList<LikeData> songArrayList;

    public LikeSongAdapter(Context context, OnLikeSongClickListerner onLikeSongClickListerner, ArrayList<LikeData> songArrayList) {
        this.context = context;
        this.onLikeSongClickListerner = onLikeSongClickListerner;
        this.songArrayList = songArrayList;
    }

    public LikeSongAdapter(Context context, OnLikeSongClickListerner onLikeSongClickListerner) {
        this.context = context;
        this.onLikeSongClickListerner = onLikeSongClickListerner;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_popular_song, parent, false);
        return new ViewHolder(mBinding, parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mBinding.rowPopularSongTvTitle.setText(songArrayList.get(position).getTitle());
        holder.mBinding.rowPopularSongTvArtist.setText(songArrayList.get(position).getArtist().getRealname());
        Utility.loadImage(songArrayList.get(position).getArtist().getImage(), holder.mBinding.rowPopularSongTvImage, R.drawable.default_track);
        holder.mBinding.ivMore.setTag(position);
        holder.mBinding.rowPopularSongTvArtist.setTag(position);
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
        }

        @Override
        public void onClick(View view) {
            if(view==mBinding.rowPopularSongTvArtist) {
                if (onLikeSongClickListerner != null) {
                    int pos = (int) view.getTag();
                    onLikeSongClickListerner.onArtistClick(pos);
                }
            }
            else{
                if (onLikeSongClickListerner != null) {
                    int pos = (int) view.getTag();
                    onLikeSongClickListerner.onLikeSongClick(pos);
                }
            }
        }
    }

    public interface OnLikeSongClickListerner {
        void onLikeSongClick(int pos);
        void onArtistClick(int pos);
    }
}
