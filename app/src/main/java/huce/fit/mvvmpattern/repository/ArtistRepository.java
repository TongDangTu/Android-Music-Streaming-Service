package huce.fit.mvvmpattern.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import huce.fit.mvvmpattern.api.ArtistService;
import huce.fit.mvvmpattern.model.Artist;
import huce.fit.mvvmpattern.model.DataJson;
import huce.fit.mvvmpattern.utils.Status;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArtistRepository {
    private MutableLiveData<List<Artist>> artistListMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<List<Artist>> getArtistListMutableLiveData() {
        ArtistService.artistService.getArtistList()
                .enqueue(new Callback<DataJson<Artist>>() {
                    @Override
                    public void onResponse(Call<DataJson<Artist>> call, Response<DataJson<Artist>> response) {
                        DataJson<Artist> dataJson = response.body();
                        if (dataJson != null) {
                            if (dataJson.isStatus() == true) {
                                List<Artist> artistList = dataJson.getData();
                                artistListMutableLiveData.setValue(artistList);
                            }
                            else {

                            }
                        }
                        else {

                        }
                    }

                    @Override
                    public void onFailure(Call<DataJson<Artist>> call, Throwable t) {
                        Log.e("ERROR", this.getClass().getName()+"onClickLogin()->onFailure: "+t.getMessage());
                    }
                });
        return artistListMutableLiveData;
    }
}
