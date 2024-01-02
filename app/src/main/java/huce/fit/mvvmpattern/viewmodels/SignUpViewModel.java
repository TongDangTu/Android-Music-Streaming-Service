package huce.fit.mvvmpattern.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import huce.fit.mvvmpattern.model.Account;
import huce.fit.mvvmpattern.repository.AccountRepository;
import huce.fit.mvvmpattern.utils.Status;
import huce.fit.mvvmpattern.utils.ValidatorUtil;

public class SignUpViewModel extends AndroidViewModel {
    private Account account = new Account();
    private String confirmPassword = new String();
    private AccountRepository accountRepository;
    private MutableLiveData<Boolean> login = new MutableLiveData<>();

    public SignUpViewModel(@NonNull Application application) {
        super(application);
        accountRepository = new AccountRepository(application);
    }

    public Account getAccount() {
        return account;
    }

    public String getConfirmPassword () {
        return confirmPassword;
    }

    public void setConfirmPassword (String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public LiveData<Integer> getSignUpStatus () {
        return accountRepository.getSignUpStatus(account);
    }

    public LiveData<String> getMessage () {
        return accountRepository.getMessage();
    }

    public MutableLiveData<Boolean> getLogin () { return login; }

    public void onclickSignUp() {
        if (validateData()) {
            getSignUpStatus();
        };
    }

    public void onClickLogin () {
        login.setValue(true);
    }

    private boolean validateData() {

        if (ValidatorUtil.emptyValue(account.getUsername())) {
            accountRepository.setSignUpStatus(Status.emptyUsername);
            return false;
        }
        else if (ValidatorUtil.emptyValue(account.getPassword())) {
            accountRepository.setSignUpStatus(Status.emptyPassword);
            return false;
        }
        else if (ValidatorUtil.emptyValue(confirmPassword)) {
            accountRepository.setSignUpStatus(Status.emptyConfirmPassword);
            return false;
        }
        else if (account.getPassword().equals(confirmPassword) == false) {
            accountRepository.setSignUpStatus(Status.incorrectConfirmPassword);
            return false;
        }
        return true;
    }
}