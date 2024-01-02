package huce.fit.mvvmpattern.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import huce.fit.mvvmpattern.R;
import huce.fit.mvvmpattern.databinding.ActivityLoginBinding;
import huce.fit.mvvmpattern.utils.Status;
import huce.fit.mvvmpattern.viewmodels.LoginViewModel;

public class LoginActivity extends AppCompatActivity {
    private LoginViewModel loginViewModel;

    private ActivityLoginBinding binding;

    private int loginflag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        binding = DataBindingUtil.setContentView(LoginActivity.this, R.layout.activity_login);

        binding.setLifecycleOwner(this);

        binding.setLoginViewModel(loginViewModel);

        processEvent();
    }

    private void processEvent() {
        login();
        signUp();
    }
    private void login() {
        loginViewModel.getLoginStatus().observe(this, status -> {
            switch (status) {
                case Status.loginSuccess:
                    //xu ly dang nhap thanh cong
                    Toast.makeText(LoginActivity.this, R.string.toast_login_success, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
                case Status.emptyUsername:
                    //thong bao Toast message hoac hien thi loi Username trong
                    binding.txtUsername.setError(getString(R.string.emptyUsername));
                    binding.txtUsername.requestFocus();
                    break;
                case Status.emptyPassword:
                    //tuong tu cho password
                    binding.txtPassword.setError(getString(R.string.emptypassword));
                    binding.txtPassword.requestFocus();
                    break;
                case Status.loginFail:
                    String message = loginViewModel.getMessage().getValue();
                    if (message.contains(getString(R.string.sub_str_error_php_api)) == true) {
                        Toast.makeText(LoginActivity.this, getString(R.string.toast_login_failed)+". "+getString(R.string.error_php_api), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        });
    }

    private void signUp() {
        loginViewModel.getSignUp().observe(this, signUp -> {
            if (signUp == true) {
                Intent intSignup = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intSignup);
            }
        });
    }
}
