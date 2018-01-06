package com.musika.fragment;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.musika.MainActivity;
import com.musika.MusicApp;
import com.musika.MusicService;
import com.musika.R;
import com.musika.adapter.AlbumHomeAdapter;
import com.musika.adapter.ArtistAdapter;
import com.musika.adapter.HomeSingleSongAdapter;
import com.musika.adapter.HomeSongListAdapter;
import com.musika.adapter.SliderPagerAdapter;
import com.musika.container.BaseContainer;
import com.musika.databinding.FragmentExploreBinding;
import com.musika.enums.SliderType;
import com.musika.interfaces.OnDataChangeListerner;
import com.musika.retrofit.APICall;
import com.musika.retrofit.APICallback;
import com.musika.retrofit.OnApiResponseListner;
import com.musika.retrofit.model.MessageResponse;
import com.musika.retrofit.model.common.MyTrack;
import com.musika.retrofit.model.home.AlbumsData;
import com.musika.retrofit.model.home.ArtistData;
import com.musika.retrofit.model.home.HomeData;
import com.musika.retrofit.model.home.HomeResponse;
import com.musika.retrofit.model.home.OtherRowResponse;
import com.musika.retrofit.model.home.Slider;
import com.musika.utility.Utility;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class ExploreFragment extends Fragment implements ArtistAdapter.OnArtistClickListerner,
        OnApiResponseListner,
        View.OnClickListener,
        OnDataChangeListerner, HomeSingleSongAdapter.OnSongClickListerner {

    private FragmentExploreBinding mBinding;
    private List<Slider> sliderList;
    private List<ArtistData> artistData;
    private List<AlbumsData> albumsData;
    private SliderPagerAdapter sliderAdapter;
    private ArtistAdapter artistAdapter;
    private AlbumHomeAdapter albumAdapter;
    private HomeSongListAdapter songListAdapter;
    private Call<?> callHome, callArtist;
    private int lastPos = -1;
    private List<OtherRowResponse> moreData;
    private List<MyTrack> myTrackList;
    private SetBottomRaw setBottomRaw;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_explore, container, false);
        setpData();
        if (Utility.haveNetworkConnection(getActivity())) {
            mBinding.fragmentExploreSv.setVisibility(View.INVISIBLE);
            mBinding.frExplorePv.setVisibility(View.VISIBLE);
            callHome = ((MusicApp) getActivity().getApplication()).getApiTask().getHome("1",
                    new APICallback(getActivity(), APICall.HOME, this));
        } else {
            Utility.showRedSnackBar(getActivity(), getString(R.string.no_internet_connection));
        }
        return mBinding.getRoot();
    }



    private void setpData() {
        mBinding.fragmentViewpagerHeader.setOffscreenPageLimit(3);
        sliderList = new ArrayList<>();
        sliderAdapter = new SliderPagerAdapter(getActivity());
        sliderAdapter.setDataList(sliderList, this);
        mBinding.fragmentViewpagerHeader.setAdapter(sliderAdapter);
        artistData = new ArrayList<>();
        albumsData = new ArrayList<>();
        moreData = new ArrayList<>();

        artistAdapter = new ArtistAdapter(getActivity(), artistData, this);
        mBinding.fragmentExploreRvArtist.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mBinding.fragmentExploreRvArtist.setAdapter(artistAdapter);
        albumAdapter = new AlbumHomeAdapter(getActivity(), albumsData, this);
        mBinding.fragmentExploreRvAlbum.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mBinding.fragmentExploreRvAlbum.setAdapter(albumAdapter);
        songListAdapter = new HomeSongListAdapter(getActivity(), moreData, this,this);
        mBinding.fragmentExploreRvMusic.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.fragmentExploreRvMusic.setAdapter(songListAdapter);
        ViewCompat.setNestedScrollingEnabled(mBinding.fragmentExploreRvArtist, false);
        ViewCompat.setNestedScrollingEnabled(mBinding.fragmentExploreRvMusic, false);
        ViewCompat.setNestedScrollingEnabled(mBinding.fragmentExploreRvAlbum, false);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (callHome != null && !callHome.isCanceled())
            callHome.cancel();
    }

    @Override
    public void onArtistClick(int pos) {
        ((BaseContainer) getParentFragment()).addFragment(new ArtistDetailsFragment(artistData.get(pos).getId(), artistData.get(pos).getRealname(), pos, this));
    }

    @Override
    public void onFollowArtist(int pos) {
        lastPos = pos;

            if (Utility.haveNetworkConnection(getActivity())) {
                if (artistData.get(pos).getIsFollowing()) {
                    artistData.get(lastPos).updateCount(1);
                    artistData.get(lastPos).toggleFollowing();
                    artistAdapter.notifyItemChanged(lastPos);
                }else{
                    artistData.get(lastPos).updateCount(-1);
                    artistData.get(lastPos).toggleFollowing();
                    artistAdapter.notifyItemChanged(lastPos);
                }

                callArtist = ((MusicApp) getActivity().getApplication()).getApiTask().followArtist(artistData.get(pos).getId(),
                        new APICallback(getActivity(), APICall.FOLLOW, this));
            } else {
                Utility.showRedSnackBar(getActivity(), getString(R.string.no_internet_connection));
            }


    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onResponseComplete(Object clsGson, int requestCode) {
        mBinding.fragmentExploreSv.setVisibility(View.VISIBLE);
        mBinding.frExplorePv.setVisibility(View.GONE);

        if (requestCode == APICall.HOME) {
            if (clsGson instanceof HomeResponse) {
                HomeResponse HomeResponse = (HomeResponse) clsGson;
                if (HomeResponse.getData() != null) {
                    HomeData homeData = HomeResponse.getData();

                    if (homeData.getSliders() != null && homeData.getSliders().size() > 0 && sliderList.size() == 0) {
                        sliderList.addAll(homeData.getSliders());
                        sliderAdapter.setDataList(sliderList, this);
                        sliderAdapter.notifyDataSetChanged();
                        mBinding.fragmentViewpagerHeader.setVisibility(View.VISIBLE);
                    } else {
                        mBinding.fragmentViewpagerHeader.setVisibility(View.GONE);
                    }
                    if (homeData.getArtists() != null && homeData.getArtists().getData() != null
                            && homeData.getArtists().getData().size() > 0) {
                        mBinding.tvMoreArtists.setVisibility(View.VISIBLE);
                        artistData.addAll(homeData.getArtists().getData());
                        artistAdapter.notifyItemInserted(artistData.size());
                        mBinding.fragmentExploreRvArtist.setVisibility(View.VISIBLE);
                    } else {
                        mBinding.fragmentExploreRvArtist.setVisibility(View.GONE);
                    }

                    if (homeData.getAlbums() != null) {
                        mBinding.tvMoreAlbums.setVisibility(View.VISIBLE);
                        mBinding.tvMoreAlbums.setText(homeData.getAlbums().getTitle());

                        albumsData.addAll(homeData.getAlbums().getData());
                        albumAdapter.notifyItemInserted(albumsData.size());
                        mBinding.fragmentExploreRvAlbum.setVisibility(View.VISIBLE);
                    } else {
                        mBinding.fragmentExploreRvAlbum.setVisibility(View.GONE);
                    }

                    if (homeData.getOtherRow() != null && homeData.getOtherRow() != null) {
                        moreData.addAll(homeData.getOtherRow());
                        songListAdapter.notifyDataSetChanged();
                        mBinding.fragmentExploreRvMusic.setVisibility(View.VISIBLE);
                    } else {
                        mBinding.fragmentExploreRvMusic.setVisibility(View.GONE);
                    }
                }
                callHome = ((MusicApp) getActivity().getApplication()).getApiTask().getHome("2", new APICallback(getActivity(), APICall.HOME_OTHER, this));
            }
        }

        if (requestCode == APICall.HOME_OTHER) {
            if (clsGson instanceof HomeResponse) {
                HomeResponse HomeResponse = (HomeResponse) clsGson;
                if (HomeResponse.getData() != null) {
                    HomeData homeData = HomeResponse.getData();


                    if (homeData.getArtists() != null && homeData.getArtists().getData() != null
                            && homeData.getArtists().getData().size() > 0) {
                        artistData.addAll(homeData.getArtists().getData());
                        artistAdapter.notifyItemInserted(artistData.size());
                    }
                    if (homeData.getAlbums() != null) {
                        albumsData.addAll(homeData.getAlbums().getData());
                        albumAdapter.notifyItemInserted(albumsData.size());
                    }
                    if (homeData.getOtherRow() != null && homeData.getOtherRow() != null) {
                        for (int i = 0; i < moreData.size(); i++) {
                            moreData.get(i).getData().addAll(homeData.getOtherRow().get(i).getData());
                        }
                        for (int i = 0; i < mBinding.fragmentExploreRvMusic.getAdapter().getItemCount(); i++) {
                            HomeSongListAdapter.Viewholder data = (HomeSongListAdapter.Viewholder) mBinding.fragmentExploreRvMusic.findViewHolderForAdapterPosition(i);
                            data.getmBinding().rowHomeSongRv.getAdapter().notifyItemInserted(moreData.get(i).getData().size());
                        }
                    }
                }


            }
        }

        if (requestCode == APICall.FOLLOW) {
            if (clsGson instanceof MessageResponse) {
                MessageResponse messageResponse = (MessageResponse) clsGson;

            }
        }

        if (requestCode == APICall.UNFOLLOW) {
            if (clsGson instanceof MessageResponse) {
                MessageResponse messageResponse = (MessageResponse) clsGson;

            }
        }
    }

    @Override
    public void onResponseError(Object errorMessage, int requestCode) {
        if (requestCode == APICall.HOME) {
            mBinding.fragmentExploreSv.setVisibility(View.INVISIBLE);
            mBinding.frExplorePv.setVisibility(View.GONE);
            mBinding.tvNoDataFound.setVisibility(View.VISIBLE);
        }
        if (errorMessage instanceof MessageResponse) {
            MessageResponse messageResponse = (MessageResponse) errorMessage;
            Utility.showRedSnackBar(getActivity(), messageResponse.getMessage());
        }
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        if (id == R.id.row_slider_img) {
            int pos = (int) view.getTag();
            try {
                if (sliderList.get(pos).getType().equals(SliderType.ARTIST.val)) {
                    ((BaseContainer) getParentFragment()).addFragment(new ArtistDetailsFragment(Integer.parseInt(sliderList.get(pos).getValue())));
                } else if (sliderList.get(pos).getType().equals(SliderType.ARTIST.val)) {
                    ((BaseContainer) getParentFragment()).addFragment(new AlbumDetailsFragment(Integer.parseInt(sliderList.get(pos).getValue())));
                } else if (sliderList.get(pos).getType().equals(SliderType.EXTERNAL.val)) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(sliderList.get(pos).getValue()));
                    startActivity(i);
                }
            } catch (NumberFormatException ae) {
                ae.printStackTrace();
            }

        } else if (id == R.id.list_album_ll_homealbum) {
            int pos = (int) view.getTag();
            ((BaseContainer) getParentFragment()).addFragment(new AlbumDetailsFragment(albumsData.get(pos).getId(),
                    albumsData.get(pos).getName(), albumsData.get(pos).getArtist().getRealname()));
        }
        else if(id==R.id.tv_song_name){
            int pos = (int) view.getTag();
            ((BaseContainer) getParentFragment()).addFragment(new ArtistDetailsFragment(albumsData.get(pos).getArtist().getIdu()
                    , albumsData.get(pos).getArtist().getRealname()));
        }
        else {
            int pos = (int) view.getTag();
            ((BaseContainer) getParentFragment()).addFragment(new MoreListFragment(moreData.get(pos)));
        }
    }

    @Override
    public void onDataChange(Object object) {
        if (object instanceof com.musika.retrofit.model.ArtistData) {
            com.musika.retrofit.model.ArtistData newArtist = (com.musika.retrofit.model.ArtistData) object;
            if (newArtist.getPos() > -1) {
                artistData.get(newArtist.getPos()).setFollowersCount(newArtist.getFollowersCounts());
                artistData.get(newArtist.getPos()).setIsFollowing(newArtist.getIsFollowing());
                artistAdapter.notifyItemChanged(newArtist.getPos());
            }
        }
    }


    @Override
    public void onSongClick(int posraw, int pos) {
        setBottomRaw = (SetBottomRaw) getActivity();

        myTrackList = new ArrayList<>();
        for (int i = 0; i < moreData.get(posraw).getData().size(); i++) {
            MyTrack myTrack = new MyTrack();
            myTrack.setId(moreData.get(posraw).getData().get(i).getId());
            myTrack.setName(moreData.get(posraw).getData().get(i).getName());
            myTrack.setTitle(moreData.get(posraw).getData().get(i).getTitle());
            if (moreData.get(posraw).getData().get(i).getArtist().getIdu() != null)
                myTrack.setArtistId(moreData.get(posraw).getData().get(i).getArtist().getIdu());
            myTrack.setArtistName(moreData.get(posraw).getData().get(i).getArtist().getRealname());
            myTrack.setImage(moreData.get(posraw).getData().get(i).getArtist().getImage());
            myTrack.setShareUrl(moreData.get(posraw).getData().get(i).getShare_url());
            myTrack.setLike(moreData.get(posraw).getData().get(i).isLiked());
            myTrack.setTotalLike(moreData.get(posraw).getData().get(i).getLikes());
            myTrack.setTotalPlay(moreData.get(posraw).getData().get(i).getPlays());
            myTrack.setPicture(moreData.get(posraw).getData().get(i).getPicture());
            myTrackList.add(i, myTrack);
        }
        setBottomRaw.onSetBottom(myTrackList, pos);
    }

    @Override
    public void onArtistClick(int posraw, int pos) {

        ((BaseContainer) getParentFragment()).addFragment(new
                ArtistDetailsFragment(moreData.get(posraw).getData().get(pos).getArtist().getIdu()
                , moreData.get(posraw).getData().get(pos).getArtist().getRealname()));

    }

    public interface SetBottomRaw {
        void onSetBottom(List<MyTrack> myTracks, int pos);
    }
}