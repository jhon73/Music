package com.musika;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.SeekBar;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.musika.retrofit.APICall;
import com.musika.retrofit.APICallback;
import com.musika.retrofit.OnApiResponseListner;
import com.musika.retrofit.WebAPI;
import com.musika.retrofit.model.MessageResponse;
import com.musika.retrofit.model.RecentPlayData;
import com.musika.retrofit.model.RecentPlayDataEntityManager;
import com.musika.retrofit.model.common.MyTrack;
import com.musika.retrofit.model.explore.Artist;
import com.musika.utility.Utility;
import com.musika.widget.CustomTextView;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;


public class MusicService extends Service implements ExoPlayer, ExoPlayer.EventListener, OnApiResponseListner {
    private int pos = 0;
    private ExoPlayer exoPlayer;
    private MediaSource mediaSource;
    private CountDownTimer timer;
    private boolean durationSet;
    private long realDurationMillis;
    private float mCurrentPosition, mLastPosition, updateTime;
    private ExtractorsFactory extractorsFactory;
    private DataSource.Factory dataSourceFactory;
    private List<MyTrack> songArrayList;
    private final IBinder musicBind = new MusicBinder();
    private String songTitle = "";
    private static final int NOTIFY_ID = 1;
    private boolean shuffle = false;
    private boolean temp = true;
    private Random rand;
    private String starthms, lasthms;
    private CustomTextView mTvStart, mTvEnd;
    private Call<?> callAddRecentPlay;
    private SeekBar mSbMusic;
    private OnSongChangeListener onSongChangeListener;


    public void onCreate() {
        super.onCreate();
        pos = 0;
        rand = new Random();
        initMusicPlayer();
    }

    public void initMusicPlayer() {
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        extractorsFactory = new DefaultExtractorsFactory();
        TrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(trackSelectionFactory);
        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
        dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "mediaPlayerSample"), defaultBandwidthMeter);
        exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
        exoPlayer.addListener(this);
    }

    public void setList(List<MyTrack> theSongs, int pos) {
        this.songArrayList = theSongs;
        this.pos = pos;
        playSong();
    }

    public List<MyTrack> getList() {
        return songArrayList;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public int getPosition() {
        return pos;
    }

    public void setReapeat(boolean temp) {
        this.temp = temp;
    }

    @Override
    public void addListener(EventListener listener) {
    }

    @Override
    public void removeListener(EventListener listener) {
    }

    @Override
    public int getPlaybackState() {
        return 0;
    }

    @Override
    public void prepare(MediaSource mediaSource) {
    }

    @Override
    public void prepare(MediaSource mediaSource, boolean resetPosition, boolean resetState) {
    }

    @Override
    public void setPlayWhenReady(boolean playWhenReady) {
    }

    @Override
    public boolean getPlayWhenReady() {
        return exoPlayer.getPlayWhenReady();
    }

    @Override
    public boolean isLoading() {
        return false;
    }

    @Override
    public void seekToDefaultPosition() {
    }

    @Override
    public void seekToDefaultPosition(int windowIndex) {
    }

    @Override
    public void seekTo(long positionMs) {
    }

    @Override
    public void seekTo(int windowIndex, long positionMs) {
    }

    @Override
    public void setPlaybackParameters(@Nullable PlaybackParameters playbackParameters) {
    }

    @Override
    public PlaybackParameters getPlaybackParameters() {
        return null;
    }

    @Override
    public void stop() {
    }

    @Override
    public void release() {
    }

    @Override
    public void sendMessages(ExoPlayerMessage... messages) {
    }

    @Override
    public void blockingSendMessages(ExoPlayerMessage... messages) {
    }

    @Override
    public int getRendererCount() {
        return 0;
    }

    @Override
    public int getRendererType(int index) {
        return 0;
    }

    @Override
    public TrackGroupArray getCurrentTrackGroups() {
        return null;
    }

    @Override
    public TrackSelectionArray getCurrentTrackSelections() {
        return null;
    }

    @Override
    public Object getCurrentManifest() {
        return null;
    }

    @Override
    public Timeline getCurrentTimeline() {
        return null;
    }

    @Override
    public int getCurrentPeriodIndex() {
        return 0;
    }

    @Override
    public int getCurrentWindowIndex() {
        return 0;
    }

    @Override
    public long getDuration() {
        return exoPlayer.getDuration();
    }

    @Override
    public long getCurrentPosition() {
        return exoPlayer.getCurrentPosition();
    }


    @Override
    public long getBufferedPosition() {
        return exoPlayer.getBufferedPosition();
    }

    @Override
    public int getBufferedPercentage() {
        return 0;
    }

    @Override
    public boolean isCurrentWindowDynamic() {
        return false;
    }

    @Override
    public boolean isCurrentWindowSeekable() {
        return false;
    }

    // Exoplayer EventListener

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == ExoPlayer.STATE_READY && !durationSet) {
            setTimer();
        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
    }

    @Override
    public void onPositionDiscontinuity() {
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
    }

    @Override
    public void onResponseComplete(Object clsGson, int requestCode) {
        if (requestCode == APICall.ADD_RECENTPLAY_SONG) {
            if (clsGson instanceof MessageResponse) {
                RecentPlayDataEntityManager recentPlayDataEntityManager = new RecentPlayDataEntityManager();
                RecentPlayData recentPlayData = new RecentPlayData();
                recentPlayData.setId(songArrayList.get(pos).getId());
                recentPlayData.setName(songArrayList.get(pos).getName());
                recentPlayData.setTitle(songArrayList.get(pos).getTitle());
                recentPlayData.setPicture(songArrayList.get(pos).getPicture());
                recentPlayData.setTotallikes(songArrayList.get(pos).getTotalLike());
                recentPlayData.setTotalplays(songArrayList.get(pos).getTotalPlay());
                recentPlayData.setShare_url(songArrayList.get(pos).getShareUrl());
                recentPlayData.setLiked(songArrayList.get(pos).isLike());

                Artist artist = new Artist();
                artist.setIdu(songArrayList.get(pos).getArtistId());
                artist.setRealname(songArrayList.get(pos).getArtistName());
                if (songArrayList.get(pos).getImage() != null)
                    artist.setImage(songArrayList.get(pos).getImage());
                else
                    artist.setImage(songArrayList.get(pos).getPicture());
                artist.setImage(songArrayList.get(pos).getImage());
                recentPlayData.setArtist(artist);
                recentPlayDataEntityManager.add(recentPlayData);

            }
        }
    }

    @Override
    public void onResponseError(Object errorMessage, int requestCode) {
        if (errorMessage instanceof MessageResponse) {
            MessageResponse messageResponse = (MessageResponse) errorMessage;
        }
    }

    public void setSeekBar(SeekBar seekBar) {
        this.mSbMusic = seekBar;

        mSbMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mSbMusic != null) {
                    int playPositionInMillisecconds = (int) ((realDurationMillis / 100) * mSbMusic.getProgress());
                    exoPlayer.seekTo(playPositionInMillisecconds);
                }
            }
        });
    }

    public void setStartTime(CustomTextView tvstart) {
        this.mTvStart = tvstart;
    }

    public void setEndTime(CustomTextView tvEnd) {
        this.mTvEnd = tvEnd;
    }


    public void setSongLister(OnSongChangeListener onSongChangeListener) {
        this.onSongChangeListener = onSongChangeListener;
    }

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // exoPlayer.stop();
        //   exoPlayer.release();
        return false;
    }

    public void playSong() {
        if (exoPlayer.getPlayWhenReady() == true) {
            exoPlayer.stop();
        }
        try {
            Uri uri = songUrl();
            if (uri != null) {
                mediaSource = new ExtractorMediaSource(uri, dataSourceFactory, extractorsFactory, null, null);
                exoPlayer.prepare(mediaSource);
                durationSet = false;
                exoPlayer.setPlayWhenReady(true);
                callApi();
            }
        } catch (Exception e) {
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }
    }

    private void setTimer() {
        realDurationMillis = getDuration();
        durationSet = true;
        timer = new CountDownTimer(realDurationMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mLastPosition = realDurationMillis;
                mCurrentPosition = exoPlayer.getCurrentPosition();
                updateTime = (mCurrentPosition / mLastPosition) * 100;
                if (mSbMusic != null)
                    mSbMusic.setProgress((int) updateTime);
                starthms = String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes((long) mCurrentPosition),
                        TimeUnit.MILLISECONDS.toSeconds((long) mCurrentPosition) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) mCurrentPosition)));
                if (mTvStart != null)
                    mTvStart.setText(starthms);
                if (mLastPosition > -1 && exoPlayer.getDuration() > -1) {
                    Utility.log(mLastPosition + "last position");
                    lasthms = String.format("%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes((long) exoPlayer.getDuration()),
                            TimeUnit.MILLISECONDS.toSeconds((long) exoPlayer.getDuration()) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) exoPlayer.getDuration())));
                } else {
                    lasthms = "00:00";
                }
                if (mTvEnd != null) {

                    mTvEnd.setText(lasthms);

                }
                if (!lasthms.equals("00:00") && starthms.equals(lasthms)) {
                    exoPlayer.stop();
                    if (temp) {
                        if (pos < songArrayList.size() - 1)
                            pos = pos + 1;
                        else
                            pos = 0;
                    }
                    if (onSongChangeListener != null)
                        onSongChangeListener.onSongChange(pos);
                    Uri uri = songUrl();
                    if (uri != null) {
                        mediaSource = new ExtractorMediaSource(uri, dataSourceFactory, extractorsFactory, null, null);
                        exoPlayer.prepare(mediaSource);
                        durationSet = false;
                        exoPlayer.setPlayWhenReady(true);
                    }
                }
                if (mSbMusic != null)
                    mSbMusic.setSecondaryProgress(exoPlayer.getBufferedPercentage());
            }

            @Override
            public void onFinish() {
            }
        };
        timer.start();
    }

    public void callApi() {
        if (Utility.haveNetworkConnection(this)) {
            callAddRecentPlay = ((MusicApp) getApplication()).getApiTask().addRecentPLaySong(songArrayList.get(pos).getId(),
                    new APICallback(this, APICall.ADD_RECENTPLAY_SONG, this));
        }
    }

//	@Override
//	public boolean onError(MediaPlayer mp, int what, int extra) {
//		Log.v("MUSIC PLAYER", "Playback Error");
//		mp.reset();
//		return false;
//	}
//
//	@Override
//	public void onPrepared(MediaPlayer mp) {
//		//start playback
//		mp.start();
//		//notification
//		Intent notIntent = new Intent(this, MainActivity.class);
//		notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		PendingIntent pendInt = PendingIntent.getActivity(this, 0,
//				notIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//		Notification.Builder builder = new Notification.Builder(this);
//
//		builder.setContentIntent(pendInt)
////		.setSmallIcon(R.drawable.play)
//		.setTicker(songTitle)
//		.setOngoing(true)
//		.setContentTitle("Playing")
//		.setContentText(songTitle);
//		Notification not = builder.build();
//		startForeground(NOTIFY_ID, not);
//	}

    //playback methods
//	public int getPosn(){
//		return player.getCurrentPosition();
//	}
//
//	public int getDur(){
//		return player.getDuration();
//	}
//
//	public boolean isPng(){
//		return player.isPlaying();
//	}
//
//	public void pausePlayer(){
//		player.pause();
//	}
//
//	public void seek(int posn){
//		player.seekTo(posn);
//	}
//
//	public void go(){
//		player.start();
//	}

    //skip to previous track
    public void playPrev() {
        pos--;
        if (pos < 0)
            pos = songArrayList.size() - 1;
        playSong();
    }


    public void setPositonSong(int pos) {
        if (this.pos != pos) {
            playSong();
            this.pos = pos;
        }
    }

    //skip to next
    public void playNext() {

        pos++;
        if (pos >= songArrayList.size())
            pos = 0;
        playSong();
    }


    public void fastForward() {
        exoPlayer.seekTo(exoPlayer.getCurrentPosition() + 5000);
    }

    public void backward() {
        exoPlayer.seekTo(exoPlayer.getCurrentPosition() - 5000);
    }

    @Override
    public void onDestroy() {
//        stopForeground(true);
    }

    //toggle shuffle
    public void setShuffle() {
        if (shuffle) shuffle = false;
        else shuffle = true;
    }

    public Uri songUrl() {
        String title = null;
        try {
            String name = new String(new MCrypt().decrypt(songArrayList.get(pos).getName())).trim();
            title = WebAPI.TRACK_FILE + name;
            Uri uri = Uri.parse(title);
            return uri;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public void pausePlayer() {
        exoPlayer.setPlayWhenReady(false);
        exoPlayer.getPlaybackState();
    }

    public void startPlayer() {
        exoPlayer.setPlayWhenReady(true);
        exoPlayer.getPlaybackState();
    }


    interface OnSongChangeListener {
        void onSongChange(int pos);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        exoPlayer.release();
        exoPlayer.stop();
        stopSelf();
    }
}
