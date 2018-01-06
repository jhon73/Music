package com.musika.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.musika.R;


import com.musika.databinding.ListAlbumBinding;
import com.musika.retrofit.model.home.AlbumsData;

import com.musika.utility.Utility;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {
    private ListAlbumBinding mBinding;
    private View.OnClickListener onClickListener;
    private Context context;
    private LayoutInflater inflater;
    private List<AlbumsData> albumData;
    private int position;


    public AlbumAdapter(Context context, List<AlbumsData> albumsData, View.OnClickListener onClickListener) {
        this.context = context;
        this.albumData = albumsData;
        this.onClickListener = onClickListener;
        inflater = LayoutInflater.from(context);
    }

    public AlbumAdapter( Context context, LayoutInflater inflater) {

        this.context = context;
        this.inflater = inflater;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_album, parent, false);
        return new ViewHolder(mBinding, parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mBinding.listAlbumLlMain.setTag(position);
        holder.mBinding.tvArtistName.setText(albumData.get(position).getArtist().getRealname());
        holder.mBinding.tvSongName.setText(albumData.get(position).getName());
        Utility.loadImage(albumData.get(position).getImage(), holder.mBinding.ivAlbumImage);

    }

    @Override
    public int getItemCount() {
        return albumData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ListAlbumBinding mBinding;

        public ViewHolder(ListAlbumBinding mBinding, View itemView) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
            mBinding.listAlbumLlMain.setOnClickListener(onClickListener);
        }
    }
}
