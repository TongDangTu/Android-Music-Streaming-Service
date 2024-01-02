package huce.fit.mvvmpattern.api;

import huce.fit.mvvmpattern.model.Category;
import huce.fit.mvvmpattern.model.DataJson;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface CategoryService {
    CategoryService categoryService = new Retrofit.Builder()
            .baseUrl("https://nhomhungtu.000webhostapp.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CategoryService.class);

    @GET("category/categoryList.php")
    Call<DataJson<Category>> getCategoryList ();
}
