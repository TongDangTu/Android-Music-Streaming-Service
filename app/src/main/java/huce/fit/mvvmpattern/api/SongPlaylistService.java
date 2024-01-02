package huce.fit.mvvmpattern.api;

import java.util.HashMap;

import huce.fit.mvvmpattern.model.DataJson;
import huce.fit.mvvmpattern.model.SongPlaylist;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SongPlaylistService {
    SongPlaylistService songPlaylistService = new Retrofit.Builder()
            .baseUrl("https://nhomhungtu.000webhostapp.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SongPlaylistService.class);

    @POST("song_playlist/song_playlist_Add_Favorite.php")
    Call<DataJson<SongPlaylist>> addToFavorite (@Body HashMap hashMap);

    @POST("song_playlist/song_playlist_Add.php")
    Call<DataJson<SongPlaylist>> addToPlaylist (@Body HashMap hashMap);

    @POST("song_playlist/song_playlist_Delete_Favorite.php")
    Call<DataJson<SongPlaylist>> deleteSongFromFavorite (@Body HashMap hashMap);

    @POST("song_playlist/song_playlist_Delete.php")
    Call<DataJson<SongPlaylist>> deleteSongFromPlaylist (@Body HashMap hashMap);
}
