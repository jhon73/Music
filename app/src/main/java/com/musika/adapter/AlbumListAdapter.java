package com.musika.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.musika.R;
import com.musika.databinding.ListAlbumOfArtistBinding;


/**
 * Created by chirag on 30-Jun-17.
 */

public class AlbumListAdapter extends RecyclerView.Adapter<AlbumListAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private ListAlbumOfArtistBinding mBinding;

    public AlbumListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_album_of_artist,parent,false);
        return new ViewHolder(mBinding,parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ListAlbumOfArtistBinding mBinding;

        public ViewHolder(ListAlbumOfArtistBinding mBinding, ViewGroup parent) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }
    }
}
