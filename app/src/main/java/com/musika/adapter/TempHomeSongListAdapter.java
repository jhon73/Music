package com.musika.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.musika.R;
import com.musika.databinding.RowHomeSongsBinding;


/**
 * Created by yudizmacmini on 18/04/16.
 */
public class TempHomeSongListAdapter extends
        RecyclerView.Adapter<TempHomeSongListAdapter.Viewholder> {

    private Context context;
    private LayoutInflater inflater;
    private RowHomeSongsBinding mBinding;

    public TempHomeSongListAdapter(@NonNull Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return 2;
    }


    @Override
    public void onBindViewHolder(final Viewholder holder, final int position) {
        holder.mBinding.rowHomeSongRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
    //    holder.mBinding.rowHomeSongRv.setAdapter(new TempHomeSingleSongAdapter(context, this));
    }


    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int arg1) {
        // TODO Auto-generated method stub
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_home_songs, parent, false);

        return new Viewholder(mBinding, parent);
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        private RowHomeSongsBinding mBinding;

        public Viewholder(RowHomeSongsBinding mBinding, ViewGroup parent) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;

        }


    }


}
