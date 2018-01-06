package com.musika.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.rebound.ui.Util;
import com.musika.R;
import com.musika.databinding.RowHomeSingleSongBinding;
import com.musika.retrofit.model.home.OtherRowData;
import com.musika.utility.Utility;
import java.util.List;

/**
 * Created by yudizmacmini on 18/04/16.
 */
public class HomeSingleSongAdapter extends
        RecyclerView.Adapter<HomeSingleSongAdapter.Viewholder> {

    private Context context;
    private LayoutInflater inflater;
    private RowHomeSingleSongBinding mBinding;
    private List<OtherRowData> albumData;
    private String type;
    private int posraw;
    private OnSongClickListerner onSongClickListerner;

    public HomeSingleSongAdapter(@NonNull Context context,OnSongClickListerner onSongClickListerner, String type, List<OtherRowData> albumData,int position) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.onSongClickListerner = onSongClickListerner;
        this.type = type;
        this.albumData = albumData;
        this.posraw = position;
    }

    @Override
    public int getItemCount() {
        return albumData.size();
    }

    @Override
    public void onBindViewHolder(final Viewholder holder, int position) {

        if (type!=null && type.equals("tracks")) {

            if (albumData.get(position).getArtist() != null)
                holder.mBinding.rowHomeSingleSongTvDesc.setText(albumData.get(position).getArtist().getRealname());
            holder.mBinding.rowHomeSingleSongTvName.setText(albumData.get(position).getTitle());
        } else {
            holder.mBinding.rowHomeSingleSongTvDesc.setText(albumData.get(position).getArtist().getRealname());
            holder.mBinding.rowHomeSingleSongTvName.setText(albumData.get(position).getName());
        }
        if (albumData.get(position).getArtist().getImage() != null)
            Utility.loadImage(albumData.get(position).getArtist().getImage(), holder.mBinding.rowHomeSingleSongIv,R.drawable.default_track);
        else if (albumData.get(position).getPicture() != null)
            Utility.loadImage(albumData.get(position).getPicture(), holder.mBinding.rowHomeSingleSongIv,R.drawable.default_track);
        else
            holder.mBinding.rowHomeSingleSongIv.setImageResource(R.drawable.default_track);

        holder.mBinding.rowHomeSingleSong.setTag(position);
        holder.mBinding.rowHomeSingleSongTvDesc.setTag(position);
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int arg1) {
        // TODO Auto-generated method stub
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_home_single_song, parent, false);

        return new Viewholder(mBinding, parent);
    }

    public class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RowHomeSingleSongBinding mBinding;

        public Viewholder(RowHomeSingleSongBinding mBinding, ViewGroup parent) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
            mBinding.rowHomeSingleSong.setOnClickListener(this);
            mBinding.rowHomeSingleSongTvDesc.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view==mBinding.rowHomeSingleSongTvDesc){
                if (onSongClickListerner != null){
                    int pos = (int) view.getTag();
                    onSongClickListerner.onArtistClick(posraw,pos);
                }
            }

            else{
                if (onSongClickListerner != null){
                    int pos = (int) view.getTag();
                    onSongClickListerner.onSongClick(posraw,pos);
                }
            }

        }
    }
    public interface OnSongClickListerner {
         void onSongClick(int posraw,int pos);
        void onArtistClick(int posraw,int pos);
    }
}
