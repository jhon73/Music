package com.musika.retrofit;


import com.musika.retrofit.model.AddSongPLaylistResponse;
import com.musika.retrofit.model.AlbumByIdResponse;
import com.musika.retrofit.model.AlbumResponse;
import com.musika.retrofit.model.ArtistAllDataResponse;
import com.musika.retrofit.model.ArtistDataResponse;
import com.musika.retrofit.model.ChangePasswordResponse;
import com.musika.retrofit.model.CreatePLaylistResponse;
import com.musika.retrofit.model.FetchByIdResponse;
import com.musika.retrofit.model.LikeResponse;
import com.musika.retrofit.model.MailVerifiedResponse;
import com.musika.retrofit.model.MessageResponse;
import com.musika.retrofit.model.PlaylistFetchResponse;
import com.musika.retrofit.model.RecentPlayResponse;
import com.musika.retrofit.model.SearchResponse;
import com.musika.retrofit.model.SignUpResponse;
import com.musika.retrofit.model.SocialTwitterResponse;
import com.musika.retrofit.model.genrictracks.GenericsResponse;
import com.musika.retrofit.model.home.HomeResponse;
import com.musika.retrofit.model.searchscreen.SearchScreenResponse;
import com.musika.retrofit.model.suggested.SuggestedResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APICall {

    int Token_RES_CODE = 401;
    int BAD_REQUEST = 400;
    int REGISTER_REQ_CODE = 1;
    int LOGIN_REQ_CODE = 2;
    int CHECK_MAIL = 3;
    int SEARCH_CODE = 4;
    int ARTISTS = 4;
    int ARTIST = 5;
    int HOME = 6;
    int FOLLOW = 7;
    int UNFOLLOW = 8;
    int ALBUMS = 9;
    int ALBUM = 10;
    int LIKE_FETCH_REQ_CODE = 11;

    int FETCH_PLAYLIST_REQ_CODE = 12;
    int FETCH_SONG_ID_PLAYLIST_REQ_CODE = 13;
    int CHANGE_PASSWORD_REQ_CODE = 14;
    int LOGOUT_REQ_CODE = 15;
    int SUGGESTED_REQ_CODE = 16;
    int RECENTLY_PLAYED_REQ_CODE = 17;
    int SEARCH_SCREEN = 18;
    int TRACKBYGENERES = 19;
    int LIKE_REQ_CODE = 20;
    int UNLIKE_REQ_CODE = 21;
    int PROFILE = 22;
    int SOCIAL_TWITTER = 23;
    int CREATE_PLAYLIST = 24;
    int ADDSONG_PLAYLIST = 25;
    int ADD_RECENTPLAY_SONG = 26;
    int HOME_OTHER = 27;

    int ARTIST_ALL = 28;
    int NO_DATA_FOUND = 404;

    @FormUrlEncoded
    @POST(WebAPI.REGISTER)
    Call<SignUpResponse> signUp(@Field("first_name") String first_name,
                                @Field("last_name") String last_name,
                                @Field("phone") String phone,
                                @Field("age") int dob, @Field("gender") String gender, @Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST(WebAPI.LOGIN)
    Call<SignUpResponse> login(
            @Field("email") String email, @Field("password") String password);


    @GET(WebAPI.CHECK_MAIL)
    Call<MailVerifiedResponse> checkEmail(
            @Path("emailAddress") String email);

    @GET(WebAPI.SEARCH_TERM)
    Call<SearchResponse> searchSong(
            @Path("searchterm") String searchterm);

    @GET(WebAPI.ARTISTS)
    Call<SearchResponse> artistList(@Query("offset") int offset, @Query("limit") int limit);

    @GET(WebAPI.ARTIST)
    Call<ArtistAllDataResponse> getArtist(
            @Path("artistId") int artistId, @Query("offset") int offset, @Query("limit") int limit);

    @GET(WebAPI.ARTISTLATEST)
    Call<ArtistDataResponse> getLatestArtist(
            @Path("artistId") int artistId);

    @GET(WebAPI.FOLLOW)
    Call<MessageResponse> followArtist(
            @Path("artistId") int artistId);

    @GET(WebAPI.UNFOLLOW)
    Call<MessageResponse> unFollowArtist(
            @Path("artistId") int artistId);

    @GET(WebAPI.HOME)
    Call<HomeResponse> getHome();

    @GET(WebAPI.HOME_RECORD)
    Call<HomeResponse> getHome(@Path("record") String record);

    @GET(WebAPI.ALBUMS)
    Call<AlbumResponse> albumsList(@Query("offset") int offset, @Query("limit") int limit);

    @GET(WebAPI.ALBUM)
    Call<AlbumByIdResponse> getAlbum(@Path("id") int id, @Query("offset") int offset, @Query("limit") int limit);

    @GET(WebAPI.LIKE_FETCH)
    Call<LikeResponse> likeFetch();

    @GET(WebAPI.FETCHPLAYLIST)
    Call<PlaylistFetchResponse> fetchPlaylist();

    @GET(WebAPI.FETCH_SONG_BY_ID)
    Call<FetchByIdResponse> fetchSongByIdPlaylist(@Path("id") int id);

    @FormUrlEncoded
    @POST(WebAPI.CHANGEPASSWORD)
    Call<ChangePasswordResponse> changePassword(
            @Field("old_password") String current_password,
            @Field("new_password") String new_password);

    @GET(WebAPI.LOGOUT)
    Call<MessageResponse> logout();

    @GET(WebAPI.SUGGESTED)
    Call<SuggestedResponse> getSuggested();

    @GET(WebAPI.RECENTLY_PLAYED)
    Call<RecentPlayResponse> recentPlay();

    @GET(WebAPI.SEARCH_SCREEN)
    Call<SearchScreenResponse> searchScreen();

    @GET(WebAPI.TRACK_GENRES)
    Call<GenericsResponse> trackGenres(@Path("tag") String name, @Query("offset") int offset, @Query("limit") int limit);

    @FormUrlEncoded
    @POST(WebAPI.CREATE_PLAYLIST)
    Call<CreatePLaylistResponse> createPlaylist(@Field("playlist_name") String playlistname);

    @GET(WebAPI.LIKE_SONG)
    Call<MessageResponse> likeSong(@Path("trackId") String trackId);

    @GET(WebAPI.UNLIKE_SONG)
    Call<MessageResponse> unLikeSong(@Path("trackId") String trackId);

    @GET(WebAPI.Add_RECENT_PLAY)
    Call<MessageResponse> addRecentPLaySong(@Path("trackId") int trackId);

    @GET(WebAPI.PROFILE)
    Call<ProfileResponse> getProfile();

    @FormUrlEncoded
    @POST(WebAPI.ADDSONG_PLAYLIST)
    Call<AddSongPLaylistResponse> addsong_playlist(@Field("playlist_id") int playlist_id,
                                                   @Field("song_id") int song_is);


    @FormUrlEncoded
    @POST(WebAPI.SOCIAL_TWITTER)
    Call<SocialTwitterResponse> social_twitter(
            @Field("social_token") long social_token,
            @Field("social_type") String social_type,
            @Field("full_name") String full_name,
            @Field("phone") String phone,
            @Field("dob") String dob,
            @Field("gender") String gender,
            @Field("device_token") String device_token,
            @Field("email") String email);


    @Multipart
    @POST(WebAPI.SOCIAL_TWITTER)
    Call<SocialTwitterResponse> social_twitter(
            @Part("social_token") RequestBody social_token,
            @Part("social_type") RequestBody social_type,
            @Part("full_name") RequestBody full_name,
            @Part("phone") RequestBody phone,
            @Part("dob") RequestBody dob,
            @Part("gender") RequestBody gender,
            @Part("device_token") RequestBody device_token,
            @Part("email") RequestBody email,
            @Part MultipartBody.Part image);


}