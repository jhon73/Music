package com.musika.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.musika.R;
import com.musika.databinding.RowHomeSingleSongBinding;
import com.musika.retrofit.model.suggested.SuggestedData;
import com.musika.utility.Utility;

import java.util.List;


/**
 * Created by yudizmacmini on 18/04/16.
 */
public class HomeSuggestedSingleSongAdapter extends RecyclerView.Adapter<HomeSuggestedSingleSongAdapter.Viewholder> {

    private Context context;
    private LayoutInflater inflater;
    private RowHomeSingleSongBinding mBinding;
    private List<SuggestedData> suggestedDataList;
    private OnSongClickListerner onSongClickListerner;
    private int posraw;

    public HomeSuggestedSingleSongAdapter(Context context, List<SuggestedData> suggestedDataList, OnSongClickListerner onSongClickListerner,int pos) {
        this.context = context;
        this.suggestedDataList = suggestedDataList;
        this.onSongClickListerner = onSongClickListerner;
        this.posraw = pos;
    }

    public HomeSuggestedSingleSongAdapter(Context context) {
        this.context = context;
    }

    public HomeSuggestedSingleSongAdapter(Context context, List<SuggestedData> suggestedDataList, int position) {
        this.context = context;
        this.suggestedDataList = suggestedDataList;
        this.posraw = position;
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int arg1) {
        // TODO Auto-generated method stub
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_home_single_song, parent, false);
        return new Viewholder(mBinding, parent);
    }

    @Override
    public void onBindViewHolder(final Viewholder holder,int position) {
        if(suggestedDataList.get(position).getArtist().getImage()!=null)
        Utility.loadImage(suggestedDataList.get(position).getArtist().getImage(), holder.mBinding.rowHomeSingleSongIv,R.drawable.default_track);
           else if (suggestedDataList.get(position).getPicture() != null)
            Utility.loadImage(suggestedDataList.get(position).getPicture(), holder.mBinding.rowHomeSingleSongIv,R.drawable.default_track);
        else
            holder.mBinding.rowHomeSingleSongIv.setImageResource(R.drawable.default_track);


        holder.mBinding.rowHomeSingleSongTvName.setText(suggestedDataList.get(position).getTitle());
        holder.mBinding.rowHomeSingleSongTvDesc.setText(suggestedDataList.get(position).getArtist().getRealname());
        holder.mBinding.rowHomeSingleSong.setTag(position);
        holder.mBinding.rowHomeSingleSongTvDesc.setTag(position);

    }

    @Override
    public int getItemCount() {
        return suggestedDataList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RowHomeSingleSongBinding mBinding;

        public Viewholder(RowHomeSingleSongBinding mBinding, ViewGroup parent) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
            this.mBinding.rowHomeSingleSong.setOnClickListener(this);
            this.mBinding.rowHomeSingleSongTvDesc.setOnClickListener(this);

        }
        @Override
        public void onClick(View view) {
            if(view==mBinding.rowHomeSingleSongTvDesc){
                if (onSongClickListerner != null) {
                    int pos = (int) view.getTag();
                    onSongClickListerner.onArtistClick(posraw, pos);
                }
            }
            else {
                if (onSongClickListerner != null) {
                    int pos = (int) view.getTag();
                    onSongClickListerner.onSongClick(posraw, pos);
                }
            }
        }
    }

    public interface OnSongClickListerner {
         void onSongClick(int posraw,int pos);
         void onArtistClick(int posraw,int pos);
    }
}
