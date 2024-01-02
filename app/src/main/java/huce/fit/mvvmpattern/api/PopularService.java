package huce.fit.mvvmpattern.api;

import huce.fit.mvvmpattern.model.DataJson;
import huce.fit.mvvmpattern.model.Popular;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface PopularService {
    PopularService popularService = new Retrofit.Builder()
            .baseUrl("https://nhomhungtu.000webhostapp.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PopularService.class);

    @GET("other/popularList.php")
    Call<DataJson<Popular>> getPopularList ();
}
