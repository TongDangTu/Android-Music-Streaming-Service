package huce.fit.mvvmpattern.api;

import java.util.HashMap;

import huce.fit.mvvmpattern.model.DataJson;
import huce.fit.mvvmpattern.model.SongInfo;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface SongInfoService {
    SongInfoService songInfoService = new Retrofit.Builder()
            .baseUrl("https://nhomhungtu.000webhostapp.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SongInfoService.class);

    @GET("songInfo/getPopular.php")
    Call<DataJson<SongInfo>> getPopular ();

    @GET("songInfo/getRandom.php")
    Call<DataJson<SongInfo>> getRandom ();

    @GET("songInfo/search.php")
    Call<DataJson<SongInfo>> search (@Query("search") String search);

    @GET("songInfo/getListSongByPlayListID.php")
    Call<DataJson<SongInfo>> getListSongByPlayListID (@Query("id") String id);

    @POST("songInfo/getFavorite.php")
    Call<DataJson<SongInfo>> getFavorite (@Body HashMap hashMap);

    @POST("song/increasePlayedTime.php")
    Call<DataJson<String>> increasePlayedTime (@Body HashMap hashMap);
}
