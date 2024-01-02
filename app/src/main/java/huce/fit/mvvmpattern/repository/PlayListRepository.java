package huce.fit.mvvmpattern.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import huce.fit.mvvmpattern.api.PlaylistService;
import huce.fit.mvvmpattern.model.DataJson;
import huce.fit.mvvmpattern.model.SongInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayListRepository {
    private MutableLiveData<List<SongInfo>> playListMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<List<SongInfo>> getPlayListMutableLiveData(String query) {
        PlaylistService.playlistservice.getPlayListByID(query).enqueue(new Callback<DataJson<SongInfo>>() {
            @Override
            public void onResponse(Call<DataJson<SongInfo>> call, Response<DataJson<SongInfo>> response) {
                DataJson<SongInfo> datajson = response.body();
                if(datajson!=null){
                    if(datajson.isStatus()){
                        List<SongInfo> Songlist = datajson.getData();
                        if(Songlist.size() != 0) {
                            Log.e("Status", Songlist.size() + "|" + Songlist.get(0).getName_song() + "|" + Songlist.get(Songlist.size()).getName_song());
                        }
                        else{
                            Log.e("Status", "Songlist size = 0");
                        }
                        playListMutableLiveData.setValue(Songlist);
                    }
                }
            }

            @Override
            public void onFailure(Call<DataJson<SongInfo>> call, Throwable t) {
                Log.e("ERROR", this.getClass().getName()+"onClickLogin()->onFailure: "+t.getMessage());
            }
        });
        return playListMutableLiveData;
    }
}
