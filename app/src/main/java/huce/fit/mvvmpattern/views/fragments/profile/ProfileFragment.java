package huce.fit.mvvmpattern.views.fragments.profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import huce.fit.mvvmpattern.R;
import huce.fit.mvvmpattern.api.AccountService;
import huce.fit.mvvmpattern.model.Account;
import huce.fit.mvvmpattern.model.DataJson;
import huce.fit.mvvmpattern.services.MediaService;
import huce.fit.mvvmpattern.utils.Status;
import huce.fit.mvvmpattern.utils.Tmp;
import huce.fit.mvvmpattern.viewmodels.LoginViewModel;
import huce.fit.mvvmpattern.viewmodels.UpdateAccountViewModel;
import huce.fit.mvvmpattern.views.LoginActivity;
import huce.fit.mvvmpattern.views.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment implements LifecycleOwner{
    private MainActivity mainActivity;
    private Button btnLogout;
    private FloatingActionButton btnChangeUsername;
    private TextInputEditText current_password;
    private TextInputEditText new_password;
    private TextInputEditText renew_password;
    private TextView Username;
    private UpdateAccountViewModel UpdateAccount;
    private LoginViewModel login;
    private Boolean one = true;

    Account acc = new Account();
    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_profile,container,false);
        UpdateAccount = new ViewModelProvider(this).get(UpdateAccountViewModel.class);
        login = new ViewModelProvider(this).get(LoginViewModel.class);
        mainActivity = (MainActivity) getActivity();
        btnLogout = view.findViewById(R.id.btn_logout);
        btnChangeUsername = view.findViewById(R.id.btnChangeUsername);
        btnLogout.setOnClickListener(view_ -> {
            Tmp.current_username = "";
            mainActivity.getOnBackPressedDispatcher().onBackPressed();
        });

        current_password = view.findViewById(R.id.edtCurrentPassword);
        new_password = view.findViewById(R.id.edtNewPassword);
        renew_password = view.findViewById(R.id.edtPasswordIncorrect);

        Username = view.findViewById(R.id.username);
        Username.setText(Tmp.current_username);
        btnChangeUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View viewDialog = LayoutInflater.from(mainActivity).inflate(R.layout.dialog_change_password,null);
                TextInputEditText edtCurrentPassword = viewDialog.findViewById(R.id.edtCurrentPassword);
                TextInputEditText edtNewPassword = viewDialog.findViewById(R.id.edtNewPassword);

                TextInputEditText edtPasswordIncorrect = viewDialog.findViewById(R.id.edtPasswordIncorrect);
                @SuppressLint("FragmentLiveDataObserve") AlertDialog alertDialog = new MaterialAlertDialogBuilder(mainActivity)
                        .setTitle("Change Password")
                        .setView(viewDialog)
                        .setPositiveButton("Change", (dialog, which) -> {
//                            Tmp.check_status = true;
                            String strCurrentPassword = edtCurrentPassword.getText().toString();
                            String strNewPassword = edtNewPassword.getText().toString();
                            String strPasswordIncorrect = edtPasswordIncorrect.getText().toString();
                            acc.setUsername(Tmp.current_username);
                            acc.setPassword(strCurrentPassword);
                            Log.e("str",strCurrentPassword+" "+strNewPassword+" "+strPasswordIncorrect);
                            if(!strNewPassword.equals(strPasswordIncorrect)){
                                Toast.makeText(mainActivity,R.string.error_confirm_password_incorrect,Toast.LENGTH_SHORT).show();
                            }
                            else if (strNewPassword.equals("")){
                                Toast.makeText(mainActivity,R.string.error_input_new_password,Toast.LENGTH_SHORT).show();
                            }
                            else {
                                AccountService.accountService.checkAccount(acc).enqueue(new Callback<DataJson<Account>>() {
                                    @Override
                                    public void onResponse(Call<DataJson<Account>> call, Response<DataJson<Account>> response) {
                                        DataJson<Account> dataJson = response.body();
                                        if (dataJson != null) {
                                            if (dataJson.isStatus() == true) {
                                                acc.setPassword(strNewPassword);
                                                AccountService.accountService.updateAccount(acc).enqueue(new Callback<DataJson<Account>>() {
                                                    @Override
                                                    public void onResponse(Call<DataJson<Account>> call, Response<DataJson<Account>> response) {
                                                        DataJson<Account> dj = response.body();
                                                        if (dj != null) {
                                                            if (dj.isStatus() == true) {
                                                                Toast.makeText(mainActivity, R.string.toast_update_success, Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                Toast.makeText(mainActivity, R.string.toast_failed_update, Toast.LENGTH_SHORT).show();
                                                            }
                                                        } else {
                                                            Toast.makeText(mainActivity, R.string.toast_failed_update + ",some error occuring!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<DataJson<Account>> call, Throwable t) {
                                                        Log.e("ERROR", this.getClass().getName() + "onClickLogin()->onFailure: " + t.getMessage());
                                                        Toast.makeText(mainActivity, "No internet connection!", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            } else {
                                                Toast.makeText(mainActivity, R.string.error_check_failed_password, Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(mainActivity, R.string.error_php_api, Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<DataJson<Account>> call, Throwable t) {
                                        Log.e("ERROR", this.getClass().getName() + "onClickLogin()->onFailure: " + t.getMessage());
                                        Toast.makeText(mainActivity, "No internet connection!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .create();
                alertDialog.show();
            }
        });
        return view;
    }
}