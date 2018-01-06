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
import com.musika.databinding.RowHomeSingleSongBinding;
import com.musika.retrofit.model.explore.AlbumData;
import com.musika.utility.Utility;

import java.util.List;


/**
 * Created by yudizmacmini on 18/04/16.
 */
public class AlbumSingleSongAdapter extends RecyclerView.Adapter<AlbumSingleSongAdapter.Viewholder> {

    private Context context;
    private LayoutInflater inflater;
    private RowHomeSingleSongBinding mBinding;
    private List<AlbumData> moreAlbumDataList;
    private View.OnClickListener onClickListener;
    public AlbumSingleSongAdapter(Context context, List<AlbumData> moreAlbumDataList, View.OnClickListener onClickListener) {
        this.context = context;
        this.moreAlbumDataList = moreAlbumDataList;
        this.onClickListener=onClickListener;
    }

    public AlbumSingleSongAdapter(Context context) {
        this.context = context;
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int arg1) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_home_single_song, parent, false);
        return new Viewholder(mBinding, parent);
    }

    @Override
    public void onBindViewHolder(final Viewholder holder, final int position) {
       Utility.loadImage(moreAlbumDataList.get(position).getImage(),holder.mBinding.rowHomeSingleSongIv,R.drawable.default_track);
        holder.mBinding.rowHomeSingleSongTvName.setText(moreAlbumDataList.get(position).getName());
        holder.mBinding.rowHomeSingleSongTvDesc.setVisibility(View.GONE);
        holder.mBinding.rowHomeSingleSong.setTag(position);
    }

    @Override
    public int getItemCount() {
        return moreAlbumDataList.size();
    }



    public class Viewholder extends RecyclerView.ViewHolder {
        private RowHomeSingleSongBinding mBinding;

        public Viewholder(RowHomeSingleSongBinding mBinding, ViewGroup parent) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
            mBinding.rowHomeSingleSong.setOnClickListener(onClickListener);
        }
    }
}
