package com.musika.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.musika.MusicService;
import com.musika.R;
import com.musika.databinding.RowHomeSongsBinding;
import com.musika.retrofit.model.common.MyTrack;
import com.musika.retrofit.model.suggested.SuggestedData;
import com.musika.retrofit.model.suggested.SuggestedDataBean;

import java.util.List;



/**
 * Created by yudizmacmini on 18/04/16.
 */
public class HomeSuggestedSongListAdapter extends RecyclerView.Adapter<HomeSuggestedSongListAdapter.Viewholder>
        {
    private Context context;
    private RowHomeSongsBinding mBinding;
    private List<SuggestedDataBean> suggestedDataBeanArrayList;
    private List<SuggestedData> suggestedDataList;
    private HomeSuggestedSingleSongAdapter.OnSongClickListerner onSongRawClickListerner;



    public HomeSuggestedSongListAdapter(Context context) {
        this.context = context;
    }
    public HomeSuggestedSongListAdapter(Context context, List<SuggestedDataBean> suggestedDataBeanArrayList,HomeSuggestedSingleSongAdapter.OnSongClickListerner onSongRawClickListerner) {
        this.context = context;
        this.suggestedDataBeanArrayList = suggestedDataBeanArrayList;
        this.onSongRawClickListerner = onSongRawClickListerner;
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_home_songs, parent, false);
        return new Viewholder(mBinding, parent);
    }
    @Override
    public void onBindViewHolder(final Viewholder holder,int position) {
        holder.mBinding.rowHomeSongTv.setText(suggestedDataBeanArrayList.get(position).getTitle());
        holder.mBinding.rowHomeSongRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        suggestedDataList =  suggestedDataBeanArrayList.get(position).getData();
        holder.mBinding.rowHomeSongRv.setAdapter(new HomeSuggestedSingleSongAdapter(context,suggestedDataList,onSongRawClickListerner,position));
//        holder.mBinding.rowHomeSongRv.setAdapter(new HomeSuggestedSingleSongAdapter(context,suggestedDataList,position));
    }
    @Override
    public int getItemCount() {
        return suggestedDataBeanArrayList.size();
    }



    public class Viewholder extends RecyclerView.ViewHolder {
        private RowHomeSongsBinding mBinding;

        public Viewholder(RowHomeSongsBinding mBinding, ViewGroup parent) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }
    }

}
