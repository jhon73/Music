package com.musika.adapter;

/**
 * Created by sachin on 8/11/17.
 */

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.musika.R;
import com.musika.databinding.ListAlbumOfArtistBinding;
import com.musika.retrofit.model.Track;
import com.musika.retrofit.model.explore.Artist;

import java.util.List;


/**
 * Created by chirag on 30-Jun-17.
 */

public class AlbumListDataAdapter extends RecyclerView.Adapter<AlbumListDataAdapter.ViewHolder> {
    private Context context;
    private ListAlbumOfArtistBinding mBinding;
    private OnAlbumListClickListerner onAlbumListClickListerner;
    private List<Track> allTrack;
    private Artist artist;

    public AlbumListDataAdapter(Context context, List<Track> allTrack, OnAlbumListClickListerner onAlbumListClickListerner) {
        this.context = context;
        this.allTrack = allTrack;
        this.onAlbumListClickListerner = onAlbumListClickListerner;
    }


    public void setArtist(Artist artist) {
        this.artist = artist;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_album_of_artist, parent, false);
        return new ViewHolder(mBinding, parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mBinding.ivMore.setTag(position);
        holder.mBinding.tvSongName.setText(allTrack.get(position).getTitle());
        holder.mBinding.tvArtistName.setText(artist.getRealname());
        holder.mBinding.tvArtistName.setTag(artist.getIdu());
    }

    @Override
    public int getItemCount() {
        return allTrack.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ListAlbumOfArtistBinding mBinding;

        public ViewHolder(ListAlbumOfArtistBinding mBinding, ViewGroup parent) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
            this.mBinding.ivMore.setOnClickListener(this);
            mBinding.tvArtistName.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mBinding.tvArtistName == view) {
                if (onAlbumListClickListerner != null) {
                    int pos = (int) view.getTag();
                    onAlbumListClickListerner.onArtistListClick(pos);
                }
            } else {
                if (onAlbumListClickListerner != null) {
                    int pos = (int) view.getTag();
                    onAlbumListClickListerner.onAlbumListClick(pos);
                }
            }

        }
    }

    public interface OnAlbumListClickListerner {
        void onAlbumListClick(int pos);

        void onArtistListClick(int pos);
    }
}
