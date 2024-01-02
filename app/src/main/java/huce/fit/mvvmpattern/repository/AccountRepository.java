package huce.fit.mvvmpattern.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import huce.fit.mvvmpattern.api.AccountService;
import huce.fit.mvvmpattern.model.Account;
import huce.fit.mvvmpattern.model.DataJson;
import huce.fit.mvvmpattern.utils.Status;
import huce.fit.mvvmpattern.utils.Tmp;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountRepository {
    private Application application;
    private MutableLiveData<Integer> loginStatus = new MutableLiveData<>();
    private MutableLiveData<Integer> UpdateStatus = new MutableLiveData<>();
    private MutableLiveData<Integer> signUpStatus = new MutableLiveData<>();
    private MutableLiveData<String> message = new MutableLiveData<>();
//    private MutableLiveData<Integer> acc = new MutableLiveData<>();
//    private MutableLiveData<Boolean> st = new MutableLiveData<>();

    public AccountRepository(Application application) {
        this.application = application;
    }
    public AccountRepository(){}

    public MutableLiveData<Integer> getLoginStatus(Account account) {
        if (account.getUsername()!=null && account.getPassword()!=null) {
            AccountService.accountService.checkAccount(account)
                    .enqueue(new Callback<DataJson<Account>>() {
                        @Override
                        public void onResponse(Call<DataJson<Account>> call, Response<DataJson<Account>> response) {
                            DataJson<Account> dataJson = response.body();
                            if (dataJson != null) {
                                message.setValue(dataJson.getMessage());
                                if (dataJson.isStatus() == true) {

                                    loginStatus.setValue(Status.loginSuccess);
                                    Tmp.current_username = account.getUsername();
                                }
                                else {
                                    loginStatus.setValue(Status.loginFail);
                                }
                            }
                            else {
                                loginStatus.setValue(Status.loginFail);
                            }
                        }

                        @Override
                        public void onFailure(Call<DataJson<Account>> call, Throwable t) {
                            Log.e("ERROR", this.getClass().getName()+"onClickLogin()->onFailure: "+t.getMessage());
                            message.setValue("No internet connection");
                            loginStatus.setValue(Status.loginFail);
                        }
                    });
        }
        return loginStatus;
    }


    // dont use binding
//    public MutableLiveData<Integer> getLoginStatus2(Account account) {
//        if (account.getUsername()!=null && account.getPassword()!=null) {
//            Log.e("str_pass_repo",account.getPassword());
//            AccountService.accountService.checkAccount(account)
//                    .enqueue(new Callback<DataJson<Account>>() {
//                        @Override
//                        public void onResponse(Call<DataJson<Account>> call, Response<DataJson<Account>> response) {
//                            DataJson<Account> dataJson = response.body();
//                            if (dataJson != null) {
//                               // message.setValue(dataJson.getMessage());
//                                if (dataJson.isStatus()) {
//                                    loginStatus.setValue(Status.loginSuccess);
//                                    Log.e("status",dataJson.isStatus()+"|"+dataJson.getMessage().toString()+"|"+"ngon");
//                                }
//                                else {
//                                    Log.e("status",dataJson.isStatus()+"|"+dataJson.getMessage().toString()+"|"+"lỗi");
//                                    loginStatus.setValue(Status.loginFail);
//                                }
//                            }
//                            else {
//                                Log.e("status",dataJson.isStatus()+"|"+dataJson.getMessage().toString());
//                                loginStatus.setValue(Status.updateFail);
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<DataJson<Account>> call, Throwable t) {
//                            Log.e("ERROR", this.getClass().getName()+"onClickLogin()->onFailure: "+t.getMessage());
//                            message.setValue("No internet connection");
//                            loginStatus.setValue(Status.updateFail);
//                        }
//                    });
//        }
//        else{
//            loginStatus.setValue(Status.emptyPassword);
//        }
//        return loginStatus;
//    }


    public void setLoginStatus(Integer loginStatus) {
        this.loginStatus.setValue(loginStatus);
    }

    public MutableLiveData<Integer> getSignUpStatus (Account account) {
        if (account.getUsername() != null && account.getPassword() != null) {
            AccountService.accountService.addAccount(account)
                    .enqueue(new Callback<DataJson<Account>>() {
                        @Override
                        public void onResponse(Call<DataJson<Account>> call, Response<DataJson<Account>> response) {
                            DataJson<Account> dataJson = response.body();
                            if (dataJson != null) {
                                message.setValue(dataJson.getMessage());
                                if (dataJson.isStatus() == true) {
                                    signUpStatus.setValue(Status.signUpSuccess);
                                }
                                else {
                                    loginStatus.setValue(Status.signUpFail);
                                }
                            }
                            else {
                                loginStatus.setValue(Status.signUpFail);
                            }
                        }

                        @Override
                        public void onFailure(Call<DataJson<Account>> call, Throwable t) {
                            Log.e("ERROR", this.getClass().getName()+"onClickLogin()->onFailure: "+t.getMessage());
                            message.setValue("No internet connection");
                            signUpStatus.setValue(Status.signUpFail);
                        }
                    });
        }
        return signUpStatus;
    }
//    public MutableLiveData<Integer> changePassword(Account account){
//        if(account.getPassword()!=null && account.getUsername() != null){
////            Log.e("account",account.getPassword()+" "+account.getUsername());
//            AccountService.accountService.updateAccount(account)
//                    .enqueue(new Callback<DataJson<Account>>() {
//                        @Override
//                        public void onResponse(Call<DataJson<Account>> call, Response<DataJson<Account>> response) {
//                            DataJson<Account> dj = response.body();
//                            if(dj!=null){
//                                message.setValue(dj.getMessage());
//                                if(dj.isStatus() == true) {
//                                    acc.setValue(Status.updateSuccess);
//                                }
//                                else{
//                                    acc.setValue(Status.updateFail);
//                                }
//                            }
//                            else{
//                                acc.setValue(Status.updateFail);
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<DataJson<Account>> call, Throwable t) {
//                            Log.e("ERROR", this.getClass().getName()+"->onFailure: "+t.getMessage());
//                            message.setValue("No internet connection");
//                            acc.setValue(Status.updateFail);
//                        }
//                    });
//        }
//        return acc;
//    }
//
    public void setSignUpStatus (Integer signUpStatus) {
        this.signUpStatus.setValue(signUpStatus);
    }

    public MutableLiveData<String> getMessage () {
        return message;
    }

    public void setMessage (String message) {
        this.message.setValue(message);
    }
}
