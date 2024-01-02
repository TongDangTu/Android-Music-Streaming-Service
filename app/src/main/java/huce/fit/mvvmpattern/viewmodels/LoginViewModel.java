package huce.fit.mvvmpattern.viewmodels;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import huce.fit.mvvmpattern.api.AccountService;
import huce.fit.mvvmpattern.model.Account;
import huce.fit.mvvmpattern.model.DataJson;
import huce.fit.mvvmpattern.repository.AccountRepository;
import huce.fit.mvvmpattern.utils.Status;
import huce.fit.mvvmpattern.utils.ValidatorUtil;
import huce.fit.mvvmpattern.views.LoginActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {
    private Account account = new Account();
    private AccountRepository accountRepository;
    private MutableLiveData<Boolean> signUp = new MutableLiveData<>();

    public LoginViewModel(@NonNull Application application) {
        super(application);
        accountRepository = new AccountRepository(application);
    }

    public Account getAccount() {
        return account;
    }

    public LiveData<Integer> getLoginStatus () {
        return accountRepository.getLoginStatus(account);
    }

    public LiveData<String> getMessage () {
        return accountRepository.getMessage();
    }

    public MutableLiveData<Boolean> getSignUp () {
        return signUp;
    }

    public void onClickLogin () {
        if (validateData()) {
            getLoginStatus();
            getMessage();
        }
    }

    public void onClickSignUp () {
        signUp.setValue(true);
    }

    public boolean validateData () {
        if (ValidatorUtil.emptyValue(account.getUsername())) {
            accountRepository.setLoginStatus(Status.emptyUsername);
            return false;
        } 
        else if (ValidatorUtil.emptyValue(account.getPassword())) {
            accountRepository.setLoginStatus(Status.emptyPassword);
            return false;
        }
        return true;
    }
}
