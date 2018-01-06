package com.musika.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.musika.R;
import com.musika.databinding.RowPopularSongBinding;
import com.musika.databinding.RowSearchBinding;
import com.musika.retrofit.model.searchscreen.GeneresData;

import java.util.List;


/**
 * Created by chirag on 30-Jun-17.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private RowSearchBinding mBinding;
    private View.OnClickListener onClickListener;
    private List<GeneresData> generesDataList;

    public SearchAdapter(Context context, List<GeneresData> generesDataList, View.OnClickListener onClickListener) {
        this.context = context;
        this.generesDataList = generesDataList;
        this.onClickListener=onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_search,parent,false);
        return new ViewHolder(mBinding,parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mBinding.tvGeneresName.setText(generesDataList.get(position).getName());
        mBinding.rowSearchMain.setTag(position);

    }

    @Override
    public int getItemCount() {
        return generesDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RowSearchBinding mBinding;
        public ViewHolder(RowSearchBinding mBinding, ViewGroup parent) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
            this.mBinding.rowSearchMain.setOnClickListener(onClickListener);
        }
    }
}
