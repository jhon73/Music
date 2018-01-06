package com.musika.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.musika.R;
import com.musika.retrofit.model.home.Slider;
import com.musika.utility.Utility;
import com.zanlabs.widget.infiniteviewpager.InfinitePagerAdapter;

import java.util.List;

/**
 * Created by RxRead on 2015/9/24.
 */
public class SliderPagerAdapter extends InfinitePagerAdapter {

    private final LayoutInflater mInflater;
    private final Context mContext;
    private View.OnClickListener onClickListener;
    private List<Slider> mList;


    public void setDataList(List<Slider> list, View.OnClickListener onClickListener) {
        if (list == null)
            throw new IllegalArgumentException("list can not be null or has an empty size");
        this.mList = list;
        this.onClickListener=onClickListener;
        this.notifyDataSetChanged();
    }


    public SliderPagerAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }


    @Override
    public View getView(int position, View view, ViewGroup container) {
        ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = mInflater.inflate(R.layout.row_slider, container, false);
            view.findViewById(R.id.row_slider_img).setTag(position);
            view.findViewById(R.id.row_slider_img).setOnClickListener(onClickListener);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        Slider item = mList.get(position);
        holder.position = position;
        Utility.loadImage(item.getSliderName(), holder.ivSlider);
        return view;
    }


    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }


    private static class ViewHolder {
        public int position;
        ImageView ivSlider;

        public ViewHolder(View view) {
            ivSlider = (ImageView) view.findViewById(R.id.row_slider_img);

        }
    }

}
