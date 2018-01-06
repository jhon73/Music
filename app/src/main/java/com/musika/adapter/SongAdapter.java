package com.musika.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.musika.R;
import com.musika.databinding.ListSongBinding;


/**
 * Created by chirag on 27-Jun-17.
 */

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {
    private Context context;
    private ListSongBinding mBinding;
    private LayoutInflater inflater;
    private View.OnClickListener onClickListener;
    public SongAdapter(Context context, View.OnClickListener onClickListener) {
        this.context = context;
        this.onClickListener=onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_song, parent, false);
        return new ViewHolder(mBinding,parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mBinding.ivPlus.setTag(position);
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ListSongBinding mBinding;
        public ViewHolder(ListSongBinding mBinding, View itemView) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
            this.mBinding.ivPlus.setOnClickListener(onClickListener);
        }
    }
}
