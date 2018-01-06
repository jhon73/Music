package com.musika.retrofit;


public class WebAPI {

    public static final String HEADER_KEY = "token";
    public static final String DEVICE_TYPE = "Android";
    public static final String BASE_URL = "http://smubuafrica.com/rest/public/api/";

    public static final String LOGIN = "login";
    public static final String REGISTER = "signup";
    public static final String CHECK_MAIL = "checkEmail/{emailAddress}";
    public static final String SEARCH_TERM = "search/{searchterm}";
    public static final String SEARCH_SCREEN = "searchscreen";
    public static final String HOME = "home";
    public static final String HOME_RECORD = "home/{record}";
    public static final String ARTISTS = "followedArtists";
    public static final String ARTIST = "artist/{artistId}";
    public static final String ARTISTLATEST = "artist-latest/{artistId}";
    public static final String FOLLOW = "followArtist/{artistId}";
    public static final String UNFOLLOW = "unfollowArtist/{artistId}";
    public static final String ALBUMS = "albums";
    public static final String ALBUM = "album/{id}";
    public static final String LIKE_FETCH = "likes";
    public static final String FETCHPLAYLIST = "playlists";
    public static final String FETCH_SONG_BY_ID = "playlist/{id}";
    public static final String CHANGEPASSWORD = "change-passowrd";
    public static final String LOGOUT = "logout";
    public static final String SUGGESTED = "suggested";
    public static final String RECENTLY_PLAYED = "recently-played";
    public static final String TRACK_GENRES = "tracks-by-genere/{tag}";
    public static final String LIKE_SONG = "like/{trackId}";
    public static final String UNLIKE_SONG = "unlike/{trackId}";
    public static final String PROFILE = "myProfile";
    public static final String SOCIAL_TWITTER = "social-signup";
    public static final String CREATE_PLAYLIST = "create-playlist";
    public static final String ADDSONG_PLAYLIST = "add-playlist-song";
    public static final String Add_RECENT_PLAY = "addtrackview/{trackId}";

    /////
    public static final String TRACK_FILE = "http://www.smubuafrica.com/uploads/tracks/";
    public static final String TRACK_PICTURE = "http://www.smubuafrica.com/uploads/media/";
    public static final String ARTIST_PROFILE_PICTURE = "http://www.smubuafrica.com/uploads/avatars/";
    public static final String ARTIST_COVER_PICTURE = "http://www.smubuafrica.com/uploads/covers/";
    public static final String ALBUM_PICTURE = "http://www.smubuafrica.com/requests/albums/";
    public static final String SLIDER_PICTURE = "http://www.smubuafrica.com/app-admin/public/assets/photos/sliderImages/";
    public static final String SHARE_URL = "http://www.smubuafrica.com/index.php";


}
