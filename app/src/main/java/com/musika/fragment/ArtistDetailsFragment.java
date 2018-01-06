package com.musika.fragment;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.musika.MusicApp;
import com.musika.R;
import com.musika.adapter.PopularSongAdapter;
import com.musika.container.BaseContainer;
import com.musika.databinding.FramentArtistDetailsBinding;
import com.musika.interfaces.OnDataChangeListerner;
import com.musika.retrofit.APICall;
import com.musika.retrofit.APICallback;
import com.musika.retrofit.OnApiResponseListner;
import com.musika.retrofit.model.ArtistAllDataResponse;
import com.musika.retrofit.model.ArtistData;
import com.musika.retrofit.model.ArtistDataResponse;
import com.musika.retrofit.model.MessageResponse;
import com.musika.retrofit.model.Track;
import com.musika.retrofit.model.common.MyTrack;
import com.musika.utility.Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

@SuppressLint("ValidFragment")
public class ArtistDetailsFragment extends Fragment implements PopularSongAdapter.OnSongClickListerner, OnApiResponseListner {

    private FramentArtistDetailsBinding mBinding;
    private PopularSongAdapter popularSongAdapter, allSongAdapter;
    private List<Track> latestTrack;
    private List<Track> allTrack;

    private float imgX;
    private boolean isStart;
    private float imgY;
    private float imgHeight, imgWidth;
    private int toolbaHeight;
    private float ivBackX;
    private float imgBackY;
    private float tvPlayAlpha;
    private float tvFollowerAlpha;
    private float tvPlayAlphaNumber;
    private float tvFollowAlphaNumber;
    private float tvFollowAlpha;
    private float tvArtistNameY, tvArtistNameHeight;
    private int id;
    private int pos;
    private int offset = 0, limit = 7;
    private boolean isLoading;
    private boolean isMore = true;
    private int totalCount;
    private String artistname;
    private Call<?> callArtist;
    private LinearLayoutManager linearLayoutManager;
    private ArtistDataResponse artistDataResponse;
    private OnDataChangeListerner onDataChangeListerner;
    private ArtistAllDataResponse artistAllDataResponse;

    public ArtistDetailsFragment(int id) {
        this.id = id;
    }

    public ArtistDetailsFragment(int id, String name) {
        this.id = id;
        this.artistname = name;
    }


    public ArtistDetailsFragment() {
    }

    public ArtistDetailsFragment(Integer id, String name, int pos, OnDataChangeListerner onDataChangeListerner) {
        this.id = id;
        this.pos = pos;
        this.artistname = name;
        this.onDataChangeListerner = onDataChangeListerner;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.frament_artist_details, container, false);
        ButterKnife.bind(this, mBinding.getRoot());
        setData();
        if (Utility.haveNetworkConnection(getActivity())) {
            mBinding.frRvArtistDetailsPv.setVisibility(View.VISIBLE);
            callArtist = ((MusicApp) getActivity().getApplication()).getApiTask().getLatestArtist(id, new APICallback(getActivity(), APICall.ARTIST, this));
            callArtist = ((MusicApp) getActivity().getApplication()).getApiTask().getArtist(id, offset, limit, new APICallback(getActivity(), APICall.ARTIST_ALL, this));

        } else {
            Utility.showRedSnackBar(getActivity(), getString(R.string.no_internet_connection));
        }
        pagination();
        return mBinding.getRoot();
    }

    private void callingApi() {
        if (Utility.haveNetworkConnection(getActivity())) {
            mBinding.frRvArtistDetailsProgress.setVisibility(View.VISIBLE);
            callArtist = ((MusicApp) getActivity().getApplication()).getApiTask().getArtist(id, offset, limit, new APICallback(getActivity(), APICall.ARTIST_ALL, this));

        } else {
            Utility.showRedSnackBar(getActivity(), getString(R.string.no_internet_connection));
        }
    }

    private void pagination() {
        mBinding.actRvPopularSong.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

                if (!isLoading) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= (allTrack.size()) && isMore && allTrack.size() < totalCount) {
                        offset += limit;
                        isLoading = true;
                        callingApi();


                    }
                }
            }
        });
    }


    @OnClick({R.id.toolbar,R.id.fragment_artist_iv_back})
    public void onClick() {
        getActivity().onBackPressed();
    }


    @OnClick(R.id.fragment_artist_details_tv_follow)
    public void followClick() {
        if (mBinding.fragmentArtistDetailsTvFollow.getText().toString().equals(getString(R.string.follow))) {
            if (Utility.haveNetworkConnection(getActivity())) {
                if (artistDataResponse.getData().getIsFollowing() == false) {
                    mBinding.fragmentArtistDetailsTvFollow.setText(getString(R.string.unfollow));
                    mBinding.fragmentArtistDetailsTvFollow.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.round_corner_unfollow));
                    mBinding.fragmentArtistDetailsTvFollow.setTextColor(ContextCompat.getColor(getActivity(), R.color.btn_red));
                    mBinding.fragmentArtistDetailsTvNumber.setText(artistDataResponse.getData().incrementFollower(1));
                    mBinding.fragmentArtistDetailsTvFollowers.setText(artistDataResponse.getData().getFollowersCountText());
                    artistDataResponse.getData().setIsFollowing(true);

                    if (onDataChangeListerner != null) {
                        artistDataResponse.getData().setPos(pos);
                        onDataChangeListerner.onDataChange(artistDataResponse.getData());
                    }
                }else{
                    mBinding.fragmentArtistDetailsTvFollow.setText(getString(R.string.follow));
                    mBinding.fragmentArtistDetailsTvFollow.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.round_corner_follow));
                    mBinding.fragmentArtistDetailsTvFollow.setTextColor(ContextCompat.getColor(getActivity(), R.color.new_gray_follow));
                    mBinding.fragmentArtistDetailsTvNumber.setText(artistDataResponse.getData().incrementFollower(-1));
                    mBinding.fragmentArtistDetailsTvFollowers.setText(artistDataResponse.getData().getFollowersCountText());
                    artistDataResponse.getData().setIsFollowing(false);
                    if (onDataChangeListerner != null) {
                        artistDataResponse.getData().setPos(pos);
                        onDataChangeListerner.onDataChange(artistDataResponse.getData());
                    }
                }
                callArtist = ((MusicApp) getActivity().getApplication()).getApiTask().followArtist(id,
                        new APICallback(getActivity(), APICall.FOLLOW, this));
            } else {
                Utility.showRedSnackBar(getActivity(), getString(R.string.no_internet_connection));
            }
        } else {
            if (Utility.haveNetworkConnection(getActivity())) {
                callArtist = ((MusicApp) getActivity().getApplication()).getApiTask().unFollowArtist(id,
                        new APICallback(getActivity(), APICall.UNFOLLOW, this));
            } else {
                Utility.showRedSnackBar(getActivity(), getString(R.string.no_internet_connection));
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (callArtist != null && !callArtist.isCanceled()) {
            callArtist.cancel();
            isMore = false;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isStart)
            mBinding.getRoot().post(new Runnable() {
                @Override
                public void run() {
                    tvArtistNameY = mBinding.fragmentArtistDetailsArtistName.getY();
                    tvArtistNameHeight = mBinding.fragmentArtistDetailsArtistName.getMeasuredHeight();
                    tvFollowerAlpha = mBinding.fragmentArtistDetailsTvFollowers.getAlpha();
                    tvPlayAlpha = mBinding.fragmentArtistDetailsTvPlays.getAlpha();
                    tvFollowAlpha = mBinding.fragmentArtistDetailsTvFollow.getAlpha();
                    tvPlayAlphaNumber = mBinding.fragmentArtistDetailsTvPlaysNumber.getAlpha();
                    tvFollowAlphaNumber = mBinding.fragmentArtistDetailsTvNumber.getAlpha();
                    imgX = mBinding.fragmentArtistIvSongImage.getX();
                    imgY = mBinding.fragmentArtistIvSongImage.getY();
                    toolbaHeight = mBinding.toolbar.getHeight();
                    ivBackX = mBinding.fragmentArtistIvBack.getX();
                    imgWidth = mBinding.fragmentArtistIvSongImage.getMeasuredWidth();
                    imgHeight = mBinding.fragmentArtistIvSongImage.getMeasuredHeight();
                    imgBackY = mBinding.fragmentArtistIvBack.getY();
                    Log.d("test", "X: " + mBinding.fragmentArtistIvSongImage.getX());
                    Log.d("test", "Y: " + mBinding.fragmentArtistIvSongImage.getY());
                    isStart = true;
                }
            });
    }

    private void setData() {
        mBinding.fragmentArtistDetailsArtistName.setText(artistname);
        latestTrack = new ArrayList<>();
        allTrack = new ArrayList<>();
        mBinding.actRvPopularSong.setLayoutManager(new LinearLayoutManager(getActivity()));
        popularSongAdapter = new PopularSongAdapter(getActivity(), latestTrack, this,artistname);
        mBinding.actRvPopularSong.setAdapter(popularSongAdapter);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mBinding.actRvAllSong.setLayoutManager(linearLayoutManager);
        allSongAdapter = new PopularSongAdapter(getActivity(), allTrack, this,artistname);
        mBinding.actRvAllSong.setAdapter(allSongAdapter);

        ViewCompat.setNestedScrollingEnabled(mBinding.actRvPopularSong, false);
        ViewCompat.setNestedScrollingEnabled(mBinding.actRvAllSong, false);

        mBinding.fragmentArtistDetailsAppbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int maxoffset = appBarLayout.getTotalScrollRange();
                verticalOffset = Math.abs(verticalOffset);
                float percentage = verticalOffset / (float) maxoffset;
                if (isStart) {

                    if (convertDpToPixels(R.dimen._40sdp) < ((int) (imgWidth -
                            (imgWidth * percentage)))) {
                        mBinding.fragmentArtistDetailsTvFollowers.setAlpha(tvFollowerAlpha);
                        mBinding.fragmentArtistDetailsTvFollow.setAlpha(tvFollowAlpha);
                        mBinding.fragmentArtistDetailsTvNumber.setAlpha(tvFollowAlphaNumber);
                        mBinding.fragmentArtistDetailsTvPlaysNumber.setAlpha(tvPlayAlphaNumber);
                        mBinding.fragmentArtistDetailsTvPlays.setAlpha(tvPlayAlpha);
                        CollapsingToolbarLayout.LayoutParams params =
                                new CollapsingToolbarLayout.LayoutParams((int) (imgWidth -
                                        (imgWidth * percentage)), (int) (imgHeight -
                                        (imgHeight * percentage)));
                        mBinding.fragmentArtistIvSongImage.setLayoutParams(params);
                        mBinding.fragmentArtistIvBack.setColorFilter(getResources().getColor(R.color.white));
                        mBinding.fragmentArtistIvPlus.setColorFilter(getResources().getColor(R.color.white));

                    } else {
                        mBinding.fragmentArtistDetailsTvFollowers.setAlpha(tvFollowerAlpha - (tvFollowerAlpha * percentage));
                        mBinding.fragmentArtistDetailsTvPlays.setAlpha(tvPlayAlpha - (tvPlayAlpha * percentage));
                        mBinding.fragmentArtistDetailsTvFollow.setAlpha(tvFollowAlpha - (tvFollowAlpha * percentage));
                        mBinding.fragmentArtistDetailsTvNumber.setAlpha(tvFollowAlphaNumber - (tvFollowAlphaNumber * percentage));
                        mBinding.fragmentArtistDetailsTvPlaysNumber.setAlpha(tvPlayAlphaNumber - (tvPlayAlphaNumber * percentage));
                        CollapsingToolbarLayout.LayoutParams params =
                                new CollapsingToolbarLayout.LayoutParams(convertDpToPixels(R.dimen._40sdp), convertDpToPixels(R.dimen._40sdp));
                        mBinding.fragmentArtistIvSongImage.setLayoutParams(params);
                        mBinding.fragmentArtistIvBack.setColorFilter(getResources().getColor(R.color.btn_red));
                        mBinding.fragmentArtistIvPlus.setColorFilter(getResources().getColor(R.color.btn_red));
                    }
                    if ((imgX - (imgX * percentage)) > (ivBackX + mBinding.fragmentArtistIvBack.getWidth() + 10)) {
                        mBinding.fragmentArtistIvSongImage.setX(imgX - (imgX * percentage));
                    } else {
                        mBinding.fragmentArtistIvSongImage.setX(ivBackX + mBinding.fragmentArtistIvBack.getWidth() + 10);
                    }
                    mBinding.fragmentArtistIvSongImage.setY(imgY + (verticalOffset - (convertDpToPixels(R.dimen._4sdp) * percentage) - ((toolbaHeight / 2) * percentage)));


                    if (verticalOffset < tvArtistNameY - (toolbaHeight / 2) + (tvArtistNameHeight / 2))
                        mBinding.fragmentArtistDetailsArtistName.setY(tvArtistNameY);
                    else
                        mBinding.fragmentArtistDetailsArtistName.setY(tvArtistNameY + (verticalOffset - (tvArtistNameY - (toolbaHeight / 2) + (tvArtistNameHeight / 2))));
                    mBinding.fragmentArtistIvBack.setY(imgBackY + verticalOffset);
                    mBinding.fragmentArtistIvPlus.setY(imgBackY + verticalOffset);

                }
            }
        });
    }

    public int convertDpToPixels(int dp) {
        return (int) getResources().getDimension(dp);
    }

    @Override
    public void onSongMoreClick(int pos) {
        MyTrack tempTrack = new MyTrack();
        tempTrack.setId(latestTrack.get(pos).getId());
        tempTrack.setTitle(latestTrack.get(pos).getTitle());
        tempTrack.setImage(latestTrack.get(pos).getPicture());
        if (latestTrack.get(pos).getArtist() != null) {
            tempTrack.setArtistId(latestTrack.get(pos).getArtist().getId());
            tempTrack.setArtistName(latestTrack.get(pos).getArtist().getRealname());
        }
        if (latestTrack.get(pos).getAlbumId() != null && latestTrack.get(pos).getAlbumId() > 0) {
            tempTrack.setAlbumId(latestTrack.get(pos).getAlbumId());
        }
        new PlayerOptionFragment(tempTrack).show(getChildFragmentManager(), "");
    }

    @Override
    public void onArtistclick(int pos) {
        ((BaseContainer) getParentFragment()).addFragment(new ArtistDetailsFragment(id,artistname));
    }

    @Override
    public void onSongClick(int pos) {

    }

    @Override
    public void onResponseComplete(Object clsGson, int requestCode) {
        mBinding.fragmentArtistDetailsAppbar.setVisibility(View.VISIBLE);
        mBinding.fragmentArtistDetailsSv.setVisibility(View.VISIBLE);

        isLoading = false;
        mBinding.frRvArtistDetailsProgress.setVisibility(View.GONE);
        mBinding.frRvArtistDetailsPv.setVisibility(View.GONE);

        if (requestCode == APICall.ARTIST) {
            if (clsGson instanceof ArtistDataResponse) {
                artistDataResponse = (ArtistDataResponse) clsGson;
                if (artistDataResponse.getData() != null) {
                    if (offset == 0)
                        setArtistData(artistDataResponse.getData());
                    latestTrack.addAll(artistDataResponse.getData().getTracks());
                    if (artistDataResponse.getData().getTracks().size() > 0) {
                        mBinding.tvLatestReleases.setVisibility(View.VISIBLE);
                        mBinding.tvLatestReleases.setText(artistDataResponse.getTitle());
                        mBinding.actRvPopularSong.setVisibility(View.VISIBLE);
                        popularSongAdapter.notifyItemInserted(popularSongAdapter.getItemCount());

                    } else {
                        mBinding.tvLatestReleases.setVisibility(View.GONE);
                    }
                }
            }


        }
        if (requestCode == APICall.ARTIST_ALL) {
            if (clsGson instanceof ArtistAllDataResponse) {
                artistAllDataResponse = (ArtistAllDataResponse) clsGson;
                totalCount = artistAllDataResponse.getCount();
                if (artistAllDataResponse.getData() != null) {
                    mBinding.actRvAllSong.setVisibility(View.VISIBLE);
                    allTrack.addAll(artistAllDataResponse.getData());
                    allSongAdapter.notifyItemInserted(allSongAdapter.getItemCount());
                    if (artistAllDataResponse.getData().size() > 0) {
                        mBinding.tvAllReleases.setVisibility(View.VISIBLE);
                        mBinding.tvAllReleases.setText(artistAllDataResponse.getTitle());
                    } else {
                        mBinding.tvAllReleases.setVisibility(View.GONE);
                    }
                }
            }


        }

        if (requestCode == APICall.FOLLOW) {
            if (clsGson instanceof MessageResponse) {
                MessageResponse messageResponse = (MessageResponse) clsGson;

            }
        }


    }

    private void setArtistData(ArtistData data) {
        Utility.loadImage(data.getImage(), mBinding.fragmentArtistDetialsIvBg, R.drawable.default_img);
        Utility.loadImage(data.getImage(), mBinding.fragmentArtistIvSongImage, R.drawable.default_img);
        if (data.getIsFollowing()) {
            mBinding.fragmentArtistDetailsTvFollow.setText(getString(R.string.unfollow));
            mBinding.fragmentArtistDetailsTvFollow.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.round_corner_unfollow));
            mBinding.fragmentArtistDetailsTvFollow.setTextColor(ContextCompat.getColor(getActivity(), R.color.btn_red));
        } else {
            mBinding.fragmentArtistDetailsTvFollow.setText(getString(R.string.follow));
            mBinding.fragmentArtistDetailsTvFollow.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.round_corner_follow));
            mBinding.fragmentArtistDetailsTvFollow.setTextColor(ContextCompat.getColor(getActivity(), R.color.new_gray_follow));
        }
        mBinding.fragmentArtistDetailsTvPlaysNumber.setText(data.getPlaysCount());
        mBinding.fragmentArtistDetailsTvPlays.setText(data.getPlaysCountText());
        mBinding.fragmentArtistDetailsTvNumber.setText(data.getFollowersCount());
        mBinding.fragmentArtistDetailsTvFollowers.setText(data.getFollowersCountText());
    }

    @Override
    public void onResponseError(Object errorMessage, int requestCode) {
        mBinding.frRvArtistDetailsPv.setVisibility(View.GONE);
        mBinding.frRvArtistDetailsProgress.setVisibility(View.GONE);
        if (requestCode == APICall.ARTISTS) {
            isMore = false;
        }
        if (errorMessage instanceof MessageResponse) {
            MessageResponse messageResponse = (MessageResponse) errorMessage;
        } else {
        }
    }
}
