package huce.fit.mvvmpattern.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import huce.fit.mvvmpattern.model.Account;
import huce.fit.mvvmpattern.model.DataJson;
import huce.fit.mvvmpattern.repository.AccountRepository;

public class UpdateAccountViewModel extends AndroidViewModel {

    private AccountRepository acc = new AccountRepository();
    public UpdateAccountViewModel(@NonNull Application application) {
        super(application);
    }
}
