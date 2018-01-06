package com.musika.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.musika.R;
import com.musika.databinding.ListArtistBinding;
import com.musika.retrofit.model.SearchData;
import com.musika.utility.Utility;

import java.util.List;


/**
 * Created by chirag on 29-Jun-17.
 */

public class ArtistListAdapter extends RecyclerView.Adapter<ArtistListAdapter.ViewHolder> {
    private Context context;
    private ListArtistBinding mBinding;
    private LayoutInflater inflater;
    private View.OnClickListener onClickListener;
    private List<SearchData> searchDataList;

    public ArtistListAdapter(Context context, List<SearchData> searchDataList, View.OnClickListener onClickListener) {
        this.context = context;
        this.onClickListener = onClickListener;
        this.searchDataList = searchDataList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_artist, parent, false);
        return new ViewHolder(mBinding, parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mBinding.listArtistMain.setTag(position);
        holder.mBinding.artistTvName.setText(searchDataList.get(position).getRealname());
        holder.mBinding.artistTvUser.setText(searchDataList.get(position).getUsername());
        holder.mBinding.listArtistMain.setTag(position);
        Utility.loadImage(searchDataList.get(position).getImage(), holder.mBinding.civArtistImage,R.drawable.default_img);

    }

    @Override
    public int getItemCount() {
        return searchDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ListArtistBinding mBinding;

        public ViewHolder(ListArtistBinding mBinding, View itemView) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
            this.mBinding.listArtistMain.setOnClickListener(onClickListener);
        }

    }
}
