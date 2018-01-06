package com.musika.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.musika.R;
import com.musika.adapter.ArtistDownloadListAdapter;
import com.musika.databinding.FragmentArtistListBinding;

public class ArtistListFragment extends Fragment {
    private FragmentArtistListBinding mBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_artist_list, container, false);
        View view = mBinding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        mBinding.frRvArtistList.setLayoutManager(layoutManager);
        ArtistDownloadListAdapter artistAdapter = new ArtistDownloadListAdapter(getActivity(),null);
        mBinding.frRvArtistList.setAdapter(artistAdapter);
    }
}
