package com.musika.container;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.musika.R;
import com.musika.databinding.FragmentContainerBinding;
import com.musika.fragment.ExploreFragment;

public class ExploreContainer extends BaseContainer {


    private FragmentContainerBinding mBinding;
    private FragmentTransaction ft;
    private ExploreFragment exploreFragment;
    private boolean isCalled;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_container, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeFragment();
        isCalled = true;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isCalled && getChildFragmentManager().getBackStackEntryCount() > 0) {
            getChildFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            homeFragment();
        }
    }

    @Override
    public void replaceFragment(Fragment fragment) {
        try {
            ft = getChildFragmentManager().beginTransaction();
            ft.addToBackStack(fragment.getClass().getSimpleName());
            ft.replace(R.id.fragment_container, fragment, fragment.getClass().getSimpleName());
            ft.commit();

        } catch (Exception e) {
        }
    }

    @Override
    public void addFragment(Fragment fragment) {
        try {
            ft = getChildFragmentManager().beginTransaction();
            ft.addToBackStack(fragment.getClass().getSimpleName());
            ft.add(R.id.fragment_container, fragment, fragment.getClass().getSimpleName());
            ft.commit();

        } catch (Exception e) {
        }
    }

    public void homeFragment() {
        try {

                exploreFragment = new ExploreFragment();
                ft = getChildFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, exploreFragment, exploreFragment.getClass().getSimpleName());
                ft.commit();

        } catch (Exception e) {
        }
    }
}