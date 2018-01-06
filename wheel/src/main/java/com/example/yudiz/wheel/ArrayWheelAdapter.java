package com.example.yudiz.wheel;

import java.util.List;

import android.content.Context;

public class ArrayWheelAdapter<T> extends AbstractWheelTextAdapter {

	private List<String> items;
	private Context context;

	public ArrayWheelAdapter(Context context, List<String> businessNameList) {
		super(context);
		this.context = context;
		this.items = businessNameList;
	}

	public ArrayWheelAdapter(Context context, int itemResource, List<String> businessNameList) {
		super(context, itemResource, NO_RESOURCE);
		this.context = context;
		this.items = businessNameList;
	}

	public ArrayWheelAdapter(Context context, int itemResource, int itemTextResource, List<String> businessNameList) {
		super(context, itemResource, itemTextResource);
		this.context = context;
		this.items = businessNameList;
	}

	@Override
	public CharSequence getItemText(int index) {
		if (index >= 0 && index < items.size()) {
			return items.get(index);
		}
		return "";
	}

	@Override
	public int getItemsCount() {
		return items.size();
	}

	public void setList(List<String> mList) {
		items = mList;
		if (context != null)
			notifyDataChangedEvent();
	}
}
