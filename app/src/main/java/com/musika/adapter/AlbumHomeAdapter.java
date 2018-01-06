package com.musika.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.musika.R;
import com.musika.container.BaseContainer;
import com.musika.databinding.ListAlbumHomeBinding;

import com.musika.fragment.ArtistDetailsFragment;
import com.musika.retrofit.model.explore.AlbumData;
import com.musika.retrofit.model.home.AlbumsData;
import com.musika.utility.Utility;

import java.util.List;

public class AlbumHomeAdapter extends RecyclerView.Adapter<AlbumHomeAdapter.ViewHolder> {
    private ListAlbumHomeBinding mBinding;
    private View.OnClickListener onClickListener;
    private Context context;
    private LayoutInflater inflater;
    private List<AlbumsData> albumData;
    private int position;

    public AlbumHomeAdapter(Context context, List<AlbumsData> albumsData, View.OnClickListener onClickListener) {
        this.context = context;
        this.albumData = albumsData;
        this.onClickListener = onClickListener;
        inflater = LayoutInflater.from(context);
    }

    public AlbumHomeAdapter(Context context, LayoutInflater inflater) {
        this.context = context;
        this.inflater = inflater;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_album_home, parent, false);
        return new ViewHolder(mBinding, parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mBinding.listAlbumLlHomealbum.setTag(position);
        holder.mBinding.tvSongName.setText(albumData.get(position).getArtist().getRealname());
        holder.mBinding.tvArtistName.setText(albumData.get(position).getName());
        Utility.loadImage(albumData.get(position).getImage(), holder.mBinding.ivAlbumImage,R.drawable.default_img);
        holder.mBinding.tvSongName.setTag(position);

    }

    @Override
    public int getItemCount() {
        return albumData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        private final ListAlbumHomeBinding mBinding;

        public ViewHolder(ListAlbumHomeBinding mBinding, View itemView) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
            mBinding.listAlbumLlHomealbum.setOnClickListener(onClickListener);
            mBinding.tvSongName.setOnClickListener(onClickListener);
        }


    }
}
