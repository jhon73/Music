package com.musika.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.musika.R;
import com.musika.databinding.RowHomeSongsBinding;
import com.musika.retrofit.model.explore.Albums;
import com.musika.retrofit.model.home.OtherRowResponse;

import java.util.List;

public class HomeSongListAdapter extends
        RecyclerView.Adapter<HomeSongListAdapter.Viewholder> {

    private Context context;
    private LayoutInflater inflater;
    private RowHomeSongsBinding mBinding;
    private List<OtherRowResponse> albumDatas;
    private View.OnClickListener onClickListener;
    private HomeSingleSongAdapter.OnSongClickListerner onSongRawClickListerner;

    public HomeSongListAdapter(@NonNull Context context, List<OtherRowResponse> albumData, HomeSingleSongAdapter.OnSongClickListerner onSongRawClickListerner, View.OnClickListener onClickListener) {
//        inflater = LayoutInflater.from(context);
        this.context = context;
        this.onClickListener = onClickListener;
        this.albumDatas = albumData;
        this.onSongRawClickListerner = onSongRawClickListerner;
    }

    @Override
    public int getItemCount() {
        return albumDatas.size();
    }

    @Override
    public void onBindViewHolder(final Viewholder holder, int position) {


        holder.bindData(position);
        if (albumDatas.get(position).getType() != null && albumDatas.get(position).getType().equals("tracks")) {
            holder.mBinding.rowHomeSongLlMore.setVisibility(View.VISIBLE);
        } else
            holder.mBinding.rowHomeSongLlMore.setVisibility(View.GONE);
          }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int arg1) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_home_songs, parent, false);
        return new Viewholder(mBinding, parent);
    }



    public class Viewholder extends RecyclerView.ViewHolder {
        private RowHomeSongsBinding mBinding;
        private HomeSingleSongAdapter adapter;

        public Viewholder(RowHomeSongsBinding mBinding, ViewGroup parent) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
            this.mBinding.rowHomeSongLlMore.setOnClickListener(onClickListener);
        }

        public RowHomeSongsBinding getmBinding() {
            return mBinding;
        }
        public void bindData(int position) {
            mBinding.rowHomeSongTv.setText(albumDatas.get(position).getTitle());
            mBinding.rowHomeSongRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            adapter=new HomeSingleSongAdapter(context, onSongRawClickListerner,albumDatas.get(position).getType(), albumDatas.get(position).getData(),position);
            mBinding.rowHomeSongRv.setAdapter(adapter);
            mBinding.rowHomeSongLlMore.setTag(position);


        }
    }

}
