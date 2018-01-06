package com.musika.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.musika.R;
import com.musika.databinding.RowExploreArtistBinding;
import com.musika.retrofit.model.home.ArtistData;
import com.musika.utility.Utility;

import java.util.List;


/**
 * Created by yudizmacmini on 18/04/16.
 */
public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.Viewholder> {

    private Context context;
    private LayoutInflater inflater;
    private int position;
    private RowExploreArtistBinding mBinding;
    private OnArtistClickListerner onArtistClickListerner;
    private List<ArtistData> artistData;

    public ArtistAdapter(@NonNull Context context, List<ArtistData> artistData, OnArtistClickListerner onArtistClickListerner) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.artistData = artistData;
        this.onArtistClickListerner = onArtistClickListerner;
    }

    @Override
    public int getItemCount() {
        return artistData.size();
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public void onBindViewHolder(final Viewholder holder, final int position) {
        holder.mBinding.rowExploreArtistMain.setTag(position);
        holder.mBinding.rowExploreArtistTvFollow.setTag(position);
        holder.mBinding.rowExploreArtistTvName.setText(artistData.get(position).getRealname());
        if (artistData.get(position).getIsFollowing()) {
            holder.mBinding.rowExploreArtistTvFollow.setText(context.getString(R.string.unfollow));
            holder.mBinding.rowExploreArtistTvFollow.setBackgroundColor(ContextCompat.getColor(context, R.color.follow_tick));
        } else {
            holder.mBinding.rowExploreArtistTvFollow.setText(context.getString(R.string.follow));
            holder.mBinding.rowExploreArtistTvFollow.setBackgroundColor(ContextCompat.getColor(context, R.color.follow_default));
        }

        holder.mBinding.rowExploreArtistTvFollowList.setText(artistData.get(position).getFollowersCount());
        Utility.loadImage(artistData.get(position).getImage(), holder.mBinding.rowExploreArtistIv,R.drawable.default_img);

    }

    @Override
    public void onViewRecycled(Viewholder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int arg1) {
        // TODO Auto-generated method stub
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_explore_artist, parent, false);

        return new Viewholder(mBinding, parent);
    }

    public class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RowExploreArtistBinding mBinding;

        public Viewholder(RowExploreArtistBinding mBinding, ViewGroup parent) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
            this.mBinding.rowExploreArtistMain.setOnClickListener(this);
            this.mBinding.rowExploreArtistTvFollow.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (view == mBinding.rowExploreArtistMain) {
                if (onArtistClickListerner != null) {
                    int pos = (int) view.getTag();
                    onArtistClickListerner.onArtistClick(pos);
                }
            } else {
                if (onArtistClickListerner != null) {
                    int pos = (int) view.getTag();
                    onArtistClickListerner.onFollowArtist(pos);
                }
            }
        }
    }

    public interface OnArtistClickListerner {
        public void onArtistClick(int pos);

        public void onFollowArtist(int pos);
    }
}
