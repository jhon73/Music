package com.musika.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.musika.R;
import com.musika.databinding.RowPopularSongBinding;
import com.musika.retrofit.model.explore.AlbumData;
import com.musika.retrofit.model.home.OherRowAlbum;
import com.musika.retrofit.model.home.OtherRowData;
import com.musika.utility.Utility;

import java.util.List;


/**
 * Created by chirag on 27-Jun-17.
 */

public class MoreSongAdapter extends RecyclerView.Adapter<MoreSongAdapter.ViewHolder> {
    private Context context;
    private RowPopularSongBinding mBinding;
    private LayoutInflater inflater;
    private OnSongClickListerner onSongClickListerner;
    private List<OtherRowData> data;

    public MoreSongAdapter(Context context, List<OtherRowData> data, OnSongClickListerner onClickListener) {
        this.context = context;
        this.data = data;
        this.onSongClickListerner = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_popular_song, parent, false);
        return new ViewHolder(mBinding, parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mBinding.ivMore.setTag(position);
        holder.mBinding.rowPopularSongTvTitle.setText(data.get(position).getTitle());
        if (data.get(position).getArtist() != null)
            holder.mBinding.rowPopularSongTvArtist.setText(data.get(position).getArtist().getRealname());

        if (data.get(position).getArtist().getImage() != null)
            Utility.loadImage(data.get(position).getArtist().getImage(), holder.mBinding.rowPopularSongTvImage);
        else if (data.get(position).getAlbum().getImage() != null)
            Utility.loadImage(data.get(position).getAlbum().getImage(), holder.mBinding.rowPopularSongTvImage);
        else
            holder.mBinding.rowPopularSongTvImage.setImageResource(R.drawable.default_track);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private RowPopularSongBinding mBinding;

        public ViewHolder(RowPopularSongBinding mBinding, View itemView) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
            this.mBinding.ivMore.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            if (onSongClickListerner != null) {
                int pos = (int) view.getTag();
                onSongClickListerner.onSongClick(pos);
            }
        }
    }

    public interface OnSongClickListerner {
        public void onSongClick(int pos);
    }
}
