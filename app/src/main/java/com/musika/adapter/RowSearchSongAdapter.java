package com.musika.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.musika.R;
import com.musika.databinding.RowSearchSongBinding;
import com.musika.retrofit.model.SearchData;
import com.musika.utility.Utility;

import java.util.List;


/**
 * Created by chirag on 30-Jun-17.
 */

public class RowSearchSongAdapter extends RecyclerView.Adapter<RowSearchSongAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private RowSearchSongBinding mBinding;
    private OnSongClickListerner onSongClickListerner;
    private List<SearchData> dataList;

    public RowSearchSongAdapter(Context context, List<SearchData> dataList, OnSongClickListerner onSongClickListerner) {
        this.context = context;
        this.dataList = dataList;
        this.onSongClickListerner = onSongClickListerner;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_search_song, parent, false);
        return new ViewHolder(mBinding, parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (dataList.get(position).getType().equals("Artist")) {
            holder.mBinding.rowSearchSongTvSongName.setCapitalWord(dataList.get(position).getRealname());
        } else {
            holder.mBinding.rowSearchSongTvSongName.setText(dataList.get(position).getTitle());

        }

        Utility.loadImage(dataList.get(position).getPicture(), holder.mBinding.rowSearchSongIv,R.drawable.default_track);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RowSearchSongBinding mBinding;

        public ViewHolder(RowSearchSongBinding mBinding, ViewGroup parent) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;

        }

        @Override
        public void onClick(View view) {

        }
    }

    public interface OnSongClickListerner {
        public void onSongClick(int pos);
    }
}
