package huce.fit.mvvmpattern.views;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.AppBarConfiguration;

import huce.fit.mvvmpattern.databinding.ActivitySignupBinding;

import huce.fit.mvvmpattern.R;
import huce.fit.mvvmpattern.utils.Status;
import huce.fit.mvvmpattern.viewmodels.SignUpViewModel;

public class SignupActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivitySignupBinding binding;

    private SignUpViewModel signUpViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        signUpViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);

        binding = DataBindingUtil.setContentView(SignupActivity.this, R.layout.activity_signup);

        binding.setLifecycleOwner(this);

        binding.setSignUpViewModel(signUpViewModel);

        processEvent();
    }

    private void processEvent() {
        signUp();
        login();
    }

    private void login() {
        signUpViewModel.getLogin().observe(this, login -> {
            if (login == true) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void signUp() {
        signUpViewModel.getSignUpStatus().observe(this, status -> {
            String message;
            switch (status) {
                case Status.signUpSuccess:
                    message = signUpViewModel.getMessage().getValue();
                    Toast.makeText(SignupActivity.this, message, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(intent);
                    break;
                case Status.emptyUsername:
                    binding.txtUsername.setError(getString(R.string.emptyUsername));
                    binding.txtUsername.requestFocus();
                    break;
                case Status.emptyPassword:
                    binding.txtPassword.setError(getString(R.string.emptypassword));
                    binding.txtPassword.requestFocus();
                    break;
                case Status.emptyConfirmPassword:
                    binding.txtConfirmPassword.setError(getString(R.string.error_input_confirm_password));
                    binding.txtConfirmPassword.requestFocus();
                    break;
                case Status.incorrectConfirmPassword:
                    binding.txtConfirmPassword.setError(getString(R.string.error_confirm_password_incorrect));
                    binding.txtConfirmPassword.requestFocus();
                    break;

                case Status.signUpFail:
                    message = signUpViewModel.getMessage().getValue();
                    if (message.contains(getString(R.string.sub_str_error_php_api)) == true) {
                        Toast.makeText(SignupActivity.this,getString(R.string.toast_signup_failed)+". "+getString(R.string.error_php_api), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(SignupActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        });
    }
}