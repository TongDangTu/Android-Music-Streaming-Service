package huce.fit.mvvmpattern.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import huce.fit.mvvmpattern.model.Account;
import huce.fit.mvvmpattern.model.DataJson;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AccountService {
    AccountService accountService = new Retrofit.Builder()
            .baseUrl("https://nhomhungtu.000webhostapp.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AccountService.class);


    @POST("account/account.php")
    Call<DataJson<Account>> checkAccount (@Body Account account);

    @POST("account/accountAdd.php")
    Call<DataJson<Account>> addAccount (@Body Account account);

    @POST("account/accountUpdate.php")
    Call<DataJson<Account>> updateAccount (@Body Account account);
}
