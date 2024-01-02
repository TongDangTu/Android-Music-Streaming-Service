package huce.fit.mvvmpattern.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import huce.fit.mvvmpattern.model.Artist;
import huce.fit.mvvmpattern.repository.ArtistRepository;
import huce.fit.mvvmpattern.utils.Status;

public class HomeViewModel extends AndroidViewModel {
    private ArtistRepository artistRepository;
    private MutableLiveData<List<Artist>> artistListMutableLiveData = new MutableLiveData<>();

    public HomeViewModel(@NonNull Application application) {
        super(application);
        artistRepository = new ArtistRepository();
    }

    public LiveData<List<Artist>> getArtistList () {
        return artistRepository.getArtistListMutableLiveData();
    }
}
