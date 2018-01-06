package com.musika.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.musika.R;
import com.musika.databinding.RowPlaylistBinding;
import com.musika.retrofit.model.PlaylistFetchData;
import com.musika.utility.Utility;

import java.util.ArrayList;

/**
 * Created by sparken02 on 21/7/17.
 */

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {
    private Context context;
    private RowPlaylistBinding mBinding;
    private OnPlaylistClickListner onPlaylistOneClickListerner;
    private ArrayList<PlaylistFetchData> playlistArrayList;

    public PlaylistAdapter(Context context, OnPlaylistClickListner onPlaylistOneClickListerner,ArrayList<PlaylistFetchData> plalistArrayList) {
        this.context = context;
        this.onPlaylistOneClickListerner = onPlaylistOneClickListerner;
        this.playlistArrayList = plalistArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_playlist, parent, false);
        return new ViewHolder(mBinding, parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PlaylistFetchData objfetch = playlistArrayList.get(position);
        holder.mBinding.tvPlaylistName.setText(objfetch.getName());
        holder.mBinding.listPlaylistMain.setTag(position);
    }

    @Override
    public int getItemCount() {
        return playlistArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RowPlaylistBinding mBinding;

        public ViewHolder(RowPlaylistBinding mBinding,ViewGroup parent) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
            this.mBinding.listPlaylistMain.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            if(onPlaylistOneClickListerner != null){
                int pos = (int) view.getTag();
                onPlaylistOneClickListerner.onPlaylistOneClick(pos);
            }
        }
    }
    public interface OnPlaylistClickListner {
        public void onPlaylistOneClick(int pos);
    }
}
