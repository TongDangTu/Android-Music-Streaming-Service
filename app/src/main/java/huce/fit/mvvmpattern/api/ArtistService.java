package huce.fit.mvvmpattern.api;

import huce.fit.mvvmpattern.model.Artist;
import huce.fit.mvvmpattern.model.DataJson;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface ArtistService {
    ArtistService artistService = new Retrofit.Builder()
            .baseUrl("https://nhomhungtu.000webhostapp.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ArtistService.class);

    @GET("artist/artistList.php")
    Call<DataJson<Artist>> getArtistList ();
}
