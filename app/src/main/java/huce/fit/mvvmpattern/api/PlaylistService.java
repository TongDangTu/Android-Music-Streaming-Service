package huce.fit.mvvmpattern.api;

import huce.fit.mvvmpattern.model.Artist;
import huce.fit.mvvmpattern.model.DataJson;
import huce.fit.mvvmpattern.model.SongInfo;
import huce.fit.mvvmpattern.utils.Tmp;
import huce.fit.mvvmpattern.views.fragments.library.itemPlaylist.Playlist;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PlaylistService {
    PlaylistService playlistservice = new Retrofit.Builder()
            .baseUrl("https://nhomhungtu.000webhostapp.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PlaylistService.class);
    @GET("playlist/playlistUser.php")
    Call<DataJson<Playlist>> getPlayListList (@Query("username") String current_username);
    @GET("playlist/playlistGet.php")
    Call<DataJson<SongInfo>> getPlayListByID (@Query("id") String id);
    @POST("playlist/playlistAdd.php")
    Call<DataJson<Playlist>> PlaylistAdd(@Body Playlist playlist);
    @POST("playlist/playlistUpdate.php")
    Call<DataJson<Playlist>> PlaylistUpdate(@Body Playlist playlist);
    @POST("playlist/playlistDel.php")
    Call<DataJson<Playlist>> PlaylistDel(@Body Playlist playlist);
}