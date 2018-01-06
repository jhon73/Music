package com.musika.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.musika.R;
import com.musika.adapter.ViewPagerAdapter;
import com.musika.databinding.FragmentDownloadBinding;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class DownloadFragment extends Fragment {
    private FragmentDownloadBinding mBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_download, container, false);
        ButterKnife.bind(this, mBinding.getRoot());
        setupViewPager();
        return mBinding.getRoot();
    }

    @OnClick(R.id.act_download_iv_back)
    public void backPress() {
        getActivity().onBackPressed();
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new SongsListFragment(), "Songs");
        adapter.addFragment(new AlbumListFragment(), "Albums");
        adapter.addFragment(new ArtistListFragment(), "Artists");
        mBinding.viewpager.setAdapter(adapter);
        mBinding.tabs.setupWithViewPager(mBinding.viewpager);
    }
}
