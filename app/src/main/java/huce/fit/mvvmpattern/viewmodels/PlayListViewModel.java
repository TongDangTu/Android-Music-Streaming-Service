package huce.fit.mvvmpattern.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import huce.fit.mvvmpattern.repository.PlayListRepository;

public class PlayListViewModel extends AndroidViewModel {
    private PlayListRepository playlist = new PlayListRepository();
    public PlayListViewModel(@NonNull Application application) {
        super(application);
    }
}
