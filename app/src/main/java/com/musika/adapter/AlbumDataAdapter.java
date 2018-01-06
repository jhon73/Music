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
import com.musika.databinding.ListAlbumBinding;
import com.musika.retrofit.model.explore.AlbumData;
import com.musika.utility.Utility;

import java.util.List;

/**
 * Created by chirag on 29-Jun-17.
 */

public class AlbumDataAdapter extends RecyclerView.Adapter<AlbumDataAdapter.ViewHolder> {
    private Context context;
    private ListAlbumBinding mBinding;
    private OnAlbumClickListner onAlbumClickListener;
    private List<AlbumData> albumDataList;

    public AlbumDataAdapter(Context context, List<AlbumData> albumDataList, OnAlbumClickListner onAlbumClickListener) {
        this.context = context;
        this.onAlbumClickListener = onAlbumClickListener;
        this.albumDataList = albumDataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_album, parent, false);
        return new ViewHolder(mBinding, parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mBinding.tvArtistName.setText(albumDataList.get(position).getArtist().getRealname());
        Utility.loadImage(albumDataList.get(position).getImage(), holder.mBinding.ivAlbumImage,R.drawable.default_track);
        holder.mBinding.tvSongName.setText(albumDataList.get(position).getName());
        holder.mBinding.listAlbumLlMain.setTag(position);
    }

    @Override
    public int getItemCount() {
        return albumDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ListAlbumBinding mBinding;

        public ViewHolder(ListAlbumBinding mBinding, View itemView) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
            mBinding.listAlbumLlMain.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onAlbumClickListener != null) {
                int pos = (int) view.getTag();
                onAlbumClickListener.onAlbumlistOneClick(pos);
            }
        }
    }

    public interface OnAlbumClickListner {
        public void onAlbumlistOneClick(int pos);
    }
}