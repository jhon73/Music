package com.musika.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sachin on 15/4/17.
 */

public class PlayerViewPagerAdapter extends FragmentStatePagerAdapter {
   private List<Fragment> homeFragments=new ArrayList<>();
    public PlayerViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return homeFragments.get(position);
    }
    public void addFragment(List<Fragment> fragments)
    {
        homeFragments.addAll(fragments);
        notifyDataSetChanged();
    }



    @Override
    public int getCount() {
        return homeFragments.size();
    }
}
