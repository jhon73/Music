package com.musika.retrofit;


import com.musika.MusicApp;
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

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;


public class ApiTask {
    private final APICall apiCall;

    public ApiTask() {
        Retrofit retrofit = MusicApp.getRetrofitInstance();
        apiCall = retrofit.create(APICall.class);
    }

    public Call<?> signUp(String name, String lname, String phone, int age, String gender, String email, String password, APICallback onApiCallback) {
        Call<SignUpResponse> call = apiCall.signUp(name, lname, phone, age, gender, email, password);
        call.enqueue(onApiCallback);
        return call;
    }

    public Call<?> login(String email, String password, APICallback onApiCallback) {
        Call<SignUpResponse> call = apiCall.login(email, password);
        call.enqueue(onApiCallback);
        return call;
    }

    public Call<?> checkEmail(String email, APICallback onApiCallback) {
        Call<MailVerifiedResponse> call = apiCall.checkEmail(email);
        call.enqueue(onApiCallback);
        return call;
    }

    public Call<?> searchSong(String search, APICallback onApiCallback) {
        Call<SearchResponse> call = apiCall.searchSong(search);
        call.enqueue(onApiCallback);
        return call;
    }

    public Call<?> changePassword(String currentPassword, String newPassword, APICallback onApiCallback) {
        Call<ChangePasswordResponse> call = apiCall.changePassword(currentPassword, newPassword);
        call.enqueue(onApiCallback);
        return call;
    }

    public Call<?> getArtistList(int offset, int limit, APICallback onApiCallback) {
        Call<SearchResponse> call = apiCall.artistList(offset, limit);
        call.enqueue(onApiCallback);
        return call;
    }

    public Call<?> getArtist(int id, int offset, int limit, APICallback onApiCallback) {
        Call<ArtistAllDataResponse> call = apiCall.getArtist(id, offset, limit);
        call.enqueue(onApiCallback);
        return call;
    }

    public Call<?> getLatestArtist(int id,APICallback onApiCallback) {
        Call<ArtistDataResponse> call = apiCall.getLatestArtist(id);
        call.enqueue(onApiCallback);
        return call;
    }

    public Call<?> followArtist(int id, APICallback onApiCallback) {
        Call<MessageResponse> call = apiCall.followArtist(id);
        call.enqueue(onApiCallback);
        return call;
    }

    public Call<?> unFollowArtist(int id, APICallback onApiCallback) {
        Call<MessageResponse> call = apiCall.unFollowArtist(id);
        call.enqueue(onApiCallback);
        return call;
    }

    public Call<?> getHome(APICallback onApiCallback) {
        Call<HomeResponse> call = apiCall.getHome();
        call.enqueue(onApiCallback);
        return call;
    }

    public Call<?> getHome(String record, APICallback onApiCallback) {
        Call<HomeResponse> call = apiCall.getHome(record);
        call.enqueue(onApiCallback);
        return call;
    }

    public Call<?> getAlbumsList(int offset, int limit, APICallback onApiCallback) {
        Call<AlbumResponse> call = apiCall.albumsList(offset, limit);
        call.enqueue(onApiCallback);
        return call;
    }

    public Call<?> getAlbum(int id, int offset, int limit, APICallback onApiCallback) {
        Call<AlbumByIdResponse> call = apiCall.getAlbum(id, offset, limit);
        call.enqueue(onApiCallback);
        return call;
    }

    public Call<?> likeFetch(APICallback onApiCallback) {
        Call<LikeResponse> call = apiCall.likeFetch();
        call.enqueue(onApiCallback);
        return call;
    }

    public Call<?> fetchPlaylist(APICallback onApiCallback) {
        Call<PlaylistFetchResponse> call = apiCall.fetchPlaylist();
        call.enqueue(onApiCallback);
        return call;
    }

    public Call<?> fetchSongByIdPlaylist(int id, APICallback onApiCallback) {
        Call<FetchByIdResponse> call = apiCall.fetchSongByIdPlaylist(id);
        call.enqueue(onApiCallback);
        return call;
    }

    public Call<?> logout(APICallback onApiCallback) {
        Call<MessageResponse> call = apiCall.logout();
        call.enqueue(onApiCallback);
        return call;
    }

    public Call<?> suggested(APICallback onApiCallback) {
        Call<SuggestedResponse> call = apiCall.getSuggested();
        call.enqueue(onApiCallback);
        return call;
    }

    public Call<?> recentPlay(APICallback onApiCallback) {
        Call<RecentPlayResponse> call = apiCall.recentPlay();
        call.enqueue(onApiCallback);
        return call;
    }

    public Call<?> searchScreeen(APICallback onApiCallback) {
        Call<SearchScreenResponse> call = apiCall.searchScreen();
        call.enqueue(onApiCallback);
        return call;
    }

    public Call<?> trackGeneres(String name, int offset, int limit, APICallback onApiCallback) {
        Call<GenericsResponse> call = apiCall.trackGenres(name, offset, limit);
        call.enqueue(onApiCallback);
        return call;
    }

    public Call<?> createPlaylist(String name, APICallback onApiCallback) {
        Call<CreatePLaylistResponse> call = apiCall.createPlaylist(name);
        call.enqueue(onApiCallback);
        return call;
    }

    public Call<?> likeSong(String trackId, APICallback onApiCallback) {
        Call<MessageResponse> call = apiCall.likeSong(trackId);
        call.enqueue(onApiCallback);
        return call;
    }

    public Call<?> unLikeSong(String trackId, APICallback onApiCallback) {
        Call<MessageResponse> call = apiCall.unLikeSong(trackId);
        call.enqueue(onApiCallback);
        return call;
    }

    public Call<?> addRecentPLaySong(int trackId, APICallback onApiCallback) {
        Call<MessageResponse> call = apiCall.addRecentPLaySong(trackId);
        call.enqueue(onApiCallback);
        return call;
    }

    public Call<?> getProfile(APICallback onApiCallback) {
        Call<ProfileResponse> call = apiCall.getProfile();
        call.enqueue(onApiCallback);
        return call;
    }

    public Call<?> addsong_playlist(int playlist_id, int song_id, APICallback onApiCallback) {
        Call<AddSongPLaylistResponse> call = apiCall.addsong_playlist(playlist_id, song_id);
        call.enqueue(onApiCallback);
        return call;
    }


    public Call<?> social_twitter(long social_token, String social_type, String full_name, String phone, String dob, String gender, String device_token, String email, APICallback onApiCallback) {
        Call<SocialTwitterResponse> call = apiCall.social_twitter(social_token, social_type, full_name, phone, dob, gender, device_token, email);
        call.enqueue(onApiCallback);
        return call;
    }

    public Call<?> social_twitter(long social_token, String social_type, String full_name, String phone,
                                  String dob, String gender, String device_token,
                                  String email, String imagePath, APICallback onApiCallback) {
        Call<SocialTwitterResponse> call = apiCall.social_twitter(toRequestBody(social_token + ""),
                toRequestBody(social_type), toRequestBody(full_name), toRequestBody(phone),
                toRequestBody(dob), toRequestBody(gender), toRequestBody(device_token),
                toRequestBody(email), getImage(imagePath));

        call.enqueue(onApiCallback);
        return call;
    }

    public RequestBody toRequestBody(String value) {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
        return body;
    }

    private MultipartBody.Part getImage(String path) {
        File newFile = new File(path);
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), newFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("vImage", newFile.getName(), reqFile);
        return body;
    }

}