package com.musika.fragment;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.musika.MusicApp;
import com.musika.R;
import com.musika.adapter.AlbumListDataAdapter;
import com.musika.adapter.AlbumSingleSongAdapter;
import com.musika.baseclass.BaseFragment;
import com.musika.container.BaseContainer;
import com.musika.databinding.FragmentAlbumDetailsBinding;
import com.musika.retrofit.APICall;
import com.musika.retrofit.APICallback;
import com.musika.retrofit.OnApiResponseListner;
import com.musika.retrofit.model.AlbumByIdResponse;
import com.musika.retrofit.model.Track;
import com.musika.retrofit.model.common.MyTrack;
import com.musika.retrofit.model.explore.AlbumData;
import com.musika.utility.Utility;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

public class AlbumDetailsFragment extends BaseFragment implements OnApiResponseListner, AlbumListDataAdapter.OnAlbumListClickListerner, View.OnClickListener {

    private FragmentAlbumDetailsBinding mBinding;
    private boolean isStart;
    private float ivBackX;
    private float imgY;
    private float toolbaHeight;
    private float imgX;
    private float imgBackY;
    private int imgWidth;
    private int imgHeight;
    private float tvFollowAlpha;
    private float tvAlbumNameY, tvAlbumNameHeight;
    private int id;
    private int offset = 0, limit = 7;
    private boolean isLoading;
    private boolean isMore = true;
    private int totalCount;
    private Call<?> callAlbumById;
    private ArrayList<Track> allTrack;
    private ArrayList<AlbumData> moreAlbumArraiList;
    private LinearLayoutManager linearLayoutManager;
    private AlbumListDataAdapter albumOfArtistAdapter;
    private AlbumSingleSongAdapter moreAlbumAdapter;
    private AlbumByIdResponse albumByIdResponse;
    private String albumname, artistname;


    @SuppressLint("ValidFragment")
    public AlbumDetailsFragment(int id, String albumname, String artistname) {
        this.id = id;
        this.albumname = albumname;
        this.artistname = artistname;
    }

    @SuppressLint("ValidFragment")
    public AlbumDetailsFragment(int id) {
        this.id = id;
    }

    public AlbumDetailsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_album_details, container, false);
        ButterKnife.bind(this, mBinding.getRoot());
        allTrack = new ArrayList<>();
        moreAlbumArraiList = new ArrayList<>();
        setData();
        if (Utility.haveNetworkConnection(getActivity())) {
            mBinding.frRvArtistDetailsPv.setVisibility(View.VISIBLE);
            callAlbumById = ((MusicApp) getActivity().getApplication()).getApiTask().getAlbum(id, offset, limit, new APICallback(getActivity(), APICall.ALBUM, this));
        } else {
            Utility.showRedSnackBar(getActivity(), getString(R.string.no_internet_connection));
        }
        //   pagination();
        return mBinding.getRoot();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (callAlbumById != null && !callAlbumById.isCanceled()) {
            callAlbumById.cancel();
        }
    }

    private void pagination() {
        mBinding.fragmentAlbumDetailsRvAlbumSong.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

    private void callingApi() {
        if (Utility.haveNetworkConnection(getActivity())) {
            mBinding.frRvArtistDetailsProgress.setVisibility(View.VISIBLE);
            callAlbumById = ((MusicApp) getActivity().getApplication()).getApiTask().getAlbum(id, offset, limit, new APICallback(getActivity(), APICall.ALBUM, this));
        } else {
            Utility.showRedSnackBar(getActivity(), getString(R.string.no_internet_connection));
        }
    }

    private void setData() {
        mBinding.fragmentAlbumDetailsAlbumName.setText(albumname);
        mBinding.actTvArtistName.setText(artistname);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mBinding.fragmentAlbumDetailsRvAlbumSong.setLayoutManager(linearLayoutManager);
        albumOfArtistAdapter = new AlbumListDataAdapter(getActivity(), allTrack, this);
        mBinding.fragmentAlbumDetailsRvAlbumSong.setAdapter(albumOfArtistAdapter);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mBinding.fragmentAlbumDetailsMoreAlbums.setLayoutManager(linearLayoutManager);
        moreAlbumAdapter = new AlbumSingleSongAdapter(getActivity(), moreAlbumArraiList, this);
        mBinding.fragmentAlbumDetailsMoreAlbums.setAdapter(moreAlbumAdapter);
        ViewCompat.setNestedScrollingEnabled(mBinding.fragmentAlbumDetailsRvAlbumSong, false);
        ViewCompat.setNestedScrollingEnabled(mBinding.fragmentAlbumDetailsMoreAlbums, false);

        mBinding.appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int maxoffset = appBarLayout.getTotalScrollRange();
                verticalOffset = Math.abs(verticalOffset);
                float percentage = verticalOffset / (float) maxoffset;
                if (isStart) {
                    if (convertDpToPixels(R.dimen._40sdp) < ((int) (imgWidth -
                            (imgWidth * percentage)))) {
                        CollapsingToolbarLayout.LayoutParams params =
                                new CollapsingToolbarLayout.LayoutParams((int) (imgWidth -
                                        (imgWidth * percentage)), (int) (imgHeight -
                                        (imgHeight * percentage)));
                        mBinding.fragmentAlbumDetailsIvAlbumImage.setLayoutParams(params);
                        mBinding.fragmentAlbumDetailsIvPlus.setColorFilter(getResources().getColor(R.color.white));
                        mBinding.fragmentAlbumDetailsIvBack.setColorFilter(getResources().getColor(R.color.white));
                        mBinding.actTvArtistName.setAlpha(tvFollowAlpha - (tvFollowAlpha * percentage));
                        mBinding.actTvArtistSongText.setAlpha(tvFollowAlpha - (tvFollowAlpha * percentage));
                        mBinding.actTvArtistSongNumber.setAlpha(tvFollowAlpha - (tvFollowAlpha * percentage));
                    } else {
                        CollapsingToolbarLayout.LayoutParams params =
                                new CollapsingToolbarLayout.LayoutParams(convertDpToPixels(R.dimen._40sdp), convertDpToPixels(R.dimen._40sdp));
                        mBinding.fragmentAlbumDetailsIvAlbumImage.setLayoutParams(params);
                        mBinding.fragmentAlbumDetailsIvPlus.setColorFilter(getResources().getColor(R.color.btn_red));
                        mBinding.fragmentAlbumDetailsIvBack.setColorFilter(getResources().getColor(R.color.btn_red));
                        mBinding.actTvArtistName.setAlpha(tvFollowAlpha - (tvFollowAlpha * percentage));
                        mBinding.actTvArtistSongText.setAlpha(tvFollowAlpha - (tvFollowAlpha * percentage));
                        mBinding.actTvArtistSongNumber.setAlpha(tvFollowAlpha - (tvFollowAlpha * percentage));
                    }

                    if ((imgX - (imgX * percentage)) > (ivBackX + mBinding.fragmentAlbumDetailsIvBack.getWidth() + 10)) {
                        mBinding.fragmentAlbumDetailsIvAlbumImage.setX(imgX - (imgX * percentage));
                    } else {
                        mBinding.fragmentAlbumDetailsIvAlbumImage.setX(ivBackX + mBinding.fragmentAlbumDetailsIvBack.getWidth() + 10);
                    }
                    mBinding.fragmentAlbumDetailsAlbumName.setY(tvAlbumNameY + convertDpToPixels(R.dimen._35sdp) * percentage);
                    mBinding.fragmentAlbumDetailsIvAlbumImage.setY(imgY + (verticalOffset / 2) + ((imgHeight / 4) * percentage) + convertDpToPixels(R.dimen._4sdp) + (convertDpToPixels(R.dimen._12sdp) * percentage));
                    mBinding.fragmentAlbumDetailsIvBack.setY(imgBackY + verticalOffset);
                    mBinding.fragmentAlbumDetailsIvPlus.setY(imgBackY + verticalOffset);
                }
            }
        });
    }


    @OnClick({R.id.fragment_album_details_iv_back, R.id.fragment_album_details_iv_back_tmp})
    public void onClick() {
        getActivity().onBackPressed();
    }

//    @OnClick({R.id.fragment_album_details_iv_plus, R.id.fragment_album_details_iv_plus_tmp})
//    public void plusClick() {
////        new PlayerOptionFragment().show(getChildFragmentManager(), "");
//    }

    public int convertDpToPixels(int dp) {
        if (getActivity() != null) {
            return (int) getResources().getDimension(dp);
        }
        return -1;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!isStart) mBinding.getRoot().post(new Runnable() {
            @Override
            public void run() {
                isStart = true;
                tvFollowAlpha = mBinding.actTvArtistName.getAlpha();
                tvAlbumNameY = mBinding.fragmentAlbumDetailsAlbumName.getY();
                tvAlbumNameHeight = mBinding.fragmentAlbumDetailsAlbumName.getMeasuredHeight();
                imgWidth = mBinding.fragmentAlbumDetailsIvAlbumImage.getMeasuredWidth();
                imgHeight = mBinding.fragmentAlbumDetailsIvAlbumImage.getMeasuredHeight();
                imgX = mBinding.fragmentAlbumDetailsIvAlbumImage.getX();
                imgY = mBinding.fragmentAlbumDetailsIvAlbumImage.getY();
                toolbaHeight = mBinding.toolbar.getHeight();
                ivBackX = mBinding.fragmentAlbumDetailsIvBack.getX();
                imgBackY = mBinding.fragmentAlbumDetailsIvBack.getY();
            }
        });
    }

    @Override
    public void onResponseComplete(Object clsGson, int requestCode) {
        mBinding.fragmentArtistDetailsSv.setVisibility(View.VISIBLE);
        mBinding.appbar.setVisibility(View.VISIBLE);
        mBinding.frRvArtistDetailsProgress.setVisibility(View.GONE);
        mBinding.frRvArtistDetailsPv.setVisibility(View.GONE);
        isLoading = false;
        if (requestCode == APICall.ALBUM) {
            if (clsGson instanceof AlbumByIdResponse) {
                albumByIdResponse = (AlbumByIdResponse) clsGson;
                albumOfArtistAdapter.setArtist(albumByIdResponse.getData().getArtist());
                totalCount = albumByIdResponse.getData().getTracks().size();
                if (albumByIdResponse.getData() != null) {
                    if (offset == 0)
                        setUserData(albumByIdResponse);
                    allTrack.addAll(albumByIdResponse.getData().getTracks());
                    albumOfArtistAdapter.notifyDataSetChanged();
                    if (albumByIdResponse.getData().getOtherAlbums() != null && albumByIdResponse.getData().getOtherAlbums().size() > 0) {
                        mBinding.fragmentAlbumDetailsTvMoreAlbum.setVisibility(View.VISIBLE);
                        moreAlbumArraiList.addAll(albumByIdResponse.getData().getOtherAlbums());
                        moreAlbumAdapter.notifyDataSetChanged();
                    } else {
                        mBinding.fragmentAlbumDetailsTvMoreAlbum.setVisibility(View.GONE);
                    }
                } else {
                    mBinding.fragmentAlbumDetailsRvAlbumSong.setVisibility(View.GONE);
                    mBinding.fragmentAlbumDetailsAlbumNoDataFound.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void setUserData(AlbumByIdResponse albumByIdResponse) {

        Utility.loadImage(albumByIdResponse.getData().getArtist().getCover(), mBinding.fragmentAlbumDetialsIvBg, R.drawable.default_img);
        Utility.loadImage(albumByIdResponse.getData().getImage(), mBinding.fragmentAlbumDetailsIvAlbumImage, R.drawable.default_img);
        if (albumByIdResponse.getData() != null && albumByIdResponse.getData().getTracks() != null) {
            mBinding.actTvArtistSongNumber.setText(albumByIdResponse.getData().getTracks().size() + "");
            if (albumByIdResponse.getData().getTracks().size() > 1) {
                mBinding.actTvArtistSongText.setText("Songs");
            } else {
                mBinding.actTvArtistSongText.setText("Song");
            }
        }

    }

    @Override
    public void onResponseError(Object errorMessage, int requestCode) {
        mBinding.frRvArtistDetailsPv.setVisibility(View.GONE);
        mBinding.frRvArtistDetailsProgress.setVisibility(View.GONE);
        if (requestCode == APICall.ALBUM) {
            isMore = false;
        }

    }

    @Override
    public void onAlbumListClick(int pos) {
        MyTrack tempTrack = new MyTrack();
        tempTrack.setId(allTrack.get(pos).getId());
        tempTrack.setTitle(allTrack.get(pos).getTitle());
        if (allTrack.get(pos).getPicture() != null) {
            tempTrack.setImage(allTrack.get(pos).getPicture());
        } else {
            tempTrack.setImage(albumByIdResponse.getData().getImage());
        }
        if (allTrack.get(pos).getArtist() != null) {
            tempTrack.setArtistId(allTrack.get(pos).getArtist().getId());
            tempTrack.setArtistName(allTrack.get(pos).getArtist().getRealname());
        }
        if (allTrack.get(pos).getAlbumId() != null && allTrack.get(pos).getAlbumId() > 0) {
            tempTrack.setAlbumId(allTrack.get(pos).getAlbumId());
        }
        new PlayerOptionFragment(tempTrack).show(getChildFragmentManager(), "");
    }

    @Override
    public void onArtistListClick(int pos) {
        ((BaseContainer) getParentFragment()).addFragment(new ArtistDetailsFragment(albumByIdResponse.getData().getArtist().getIdu()
                , albumByIdResponse.getData().getArtist().getRealname()));

    }

    @Override
    public void onClick(View view) {
        int pos = (int) view.getTag();
        ((BaseContainer) getParentFragment()).addFragment(new AlbumDetailsFragment(albumByIdResponse.getData().getOtherAlbums().get(pos).getId()));

    }
}
