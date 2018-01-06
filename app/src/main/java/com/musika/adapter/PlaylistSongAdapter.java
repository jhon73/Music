package com.musika.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.musika.R;
import com.musika.databinding.RowPopularSongBinding;
import com.musika.fragment.PlayListLibraryListFragment;
import com.musika.retrofit.model.Track;
import com.musika.utility.Utility;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by chirag on 30-Jun-17.
 */

public class PlaylistSongAdapter extends RecyclerView.Adapter<PlaylistSongAdapter.ViewHolder> {
    private final List<Track> songArrayList;
    private Context context;
    private RowPopularSongBinding mBinding;
    private OnSongClickListerner onSongClickListerner;



    public PlaylistSongAdapter(Context context,  PlayListLibraryListFragment onSongClickListerner, List<Track> tracks) {
        this.context = context;
        this.onSongClickListerner = onSongClickListerner;
        this.songArrayList = tracks;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_popular_song, parent, false);
        return new ViewHolder(mBinding, parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Track objFetchSong = songArrayList.get(position);
        holder.mBinding.rowPopularSongTvTitle.setText(objFetchSong.getTitle());
        holder.mBinding.rowPopularSongTvArtist.setText(objFetchSong.getArtist().getRealname());
        if (objFetchSong.getPicture() != null)
            Utility.loadImage(objFetchSong.getPicture(), holder.mBinding.rowPopularSongTvImage,R.drawable.default_track);
        else
            Utility.loadImage(objFetchSong.getArtist().getPicture(), holder.mBinding.rowPopularSongTvImage,R.drawable.default_track);
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
                if (onSongClickListerner != null) {
                    int pos = (int) view.getTag();
                    onSongClickListerner.onArtistClick(pos);
                }
            }
            else {
                if (onSongClickListerner != null) {
                    int pos = (int) view.getTag();
                    onSongClickListerner.onSongClick(pos);
                }
            }
        }
    }

    public interface OnSongClickListerner {
         void onSongClick(int pos);
        void onArtistClick(int pos);
    }
}
