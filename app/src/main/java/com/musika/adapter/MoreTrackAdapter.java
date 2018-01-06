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
import com.musika.fragment.TrackFragment;
import com.musika.retrofit.model.genrictracks.GenresDataBean;
import com.musika.retrofit.model.home.OtherRowData;
import com.musika.utility.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sparken08 on 30/8/17.
 */

public class MoreTrackAdapter extends RecyclerView.Adapter<MoreTrackAdapter.ViewHolder> {
    private Context context;
    private RowPopularSongBinding mBinding;
    private LayoutInflater inflater;
    private OnSongClickListerner onSongClickListerner;
    private List<GenresDataBean> dataBeanList;


    public MoreTrackAdapter(Context context, List<GenresDataBean> data, OnSongClickListerner onClickListener) {
        this.context = context;
        this.dataBeanList = data;
        this.onSongClickListerner = onClickListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_popular_song, parent, false);
        return new ViewHolder(mBinding, parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.mBinding.rowPopularSongTvTitle.setText(dataBeanList.get(position).getTitle());
        if (dataBeanList.get(position).getArtist() != null)
            holder.mBinding.rowPopularSongTvArtist.setText(dataBeanList.get(position).getArtist().getRealname());

        if (dataBeanList.get(position).getArtist().getImage() != null)
            Utility.loadImage(dataBeanList.get(position).getArtist().getImage(), holder.mBinding.rowPopularSongTvImage, R.drawable.default_track);
        else
            holder.mBinding.rowPopularSongTvImage.setImageResource(R.drawable.default_track);

        holder.mBinding.ivMore.setTag(position);
        holder.mBinding.rowPopularSongTvArtist.setTag(position);
        holder.mBinding.listSongMain.setTag(position);

    }

    @Override
    public int getItemCount() {
        return dataBeanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RowPopularSongBinding mBinding;

        public ViewHolder(RowPopularSongBinding mBinding, View itemView) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
            this.mBinding.ivMore.setOnClickListener(this);
            this.mBinding.rowPopularSongTvArtist.setOnClickListener(this);
            this.mBinding.listSongMain.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view == mBinding.rowPopularSongTvArtist) {
                if (onSongClickListerner != null) {
                    int pos = (int) view.getTag();
                    onSongClickListerner.onArtistClick(pos);
                }
            } else if (view == mBinding.ivMore){
                if (onSongClickListerner != null) {
                    int pos = (int) view.getTag();
                    onSongClickListerner.onSongMoreClick(pos);
                }
            }else{
                if (onSongClickListerner != null) {
                    int pos = (int) view.getTag();
                    onSongClickListerner.onSongClick(pos);
                }
            }
        }
    }

    public interface OnSongClickListerner {
        void onSongMoreClick(int pos);
        void onArtistClick(int pos);
        void onSongClick(int pos);
    }

}
