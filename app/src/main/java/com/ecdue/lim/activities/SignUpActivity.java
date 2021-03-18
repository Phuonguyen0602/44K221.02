package com.ecdue.lim.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.ecdue.lim.R;
import com.ecdue.lim.databinding.ActivitySignUpBinding;
import com.ecdue.lim.events.BackButtonClicked;
import com.ecdue.lim.events.SignInButtonClicked;
import com.ecdue.lim.events.SignUpButtonClicked;
import com.ecdue.lim.viewmodels.SignUpViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.Subscribe;

public class SignUpActivity extends BaseActivity {
    public static final String TAG = SignUpActivity.class.getSimpleName();
    private static final int TYPING_DELAY = 1000;
    private ActivitySignUpBinding binding;
    private SignUpViewModel viewModel;
    private Handler handler;
    private FirebaseAuth auth;

    //region lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        viewModel = new SignUpViewModel();
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);

        auth = FirebaseAuth.getInstance();
        handler = new Handler(Looper.getMainLooper());
        Runnable emailValidation = () -> {
            String input = binding.edtSignupEmail.getText().toString();
            //Log.d(TAG, "Validating: " + input + (viewModel.emailValidation(input) ? " : valid" : " : invalid"));
            binding.edtSignupEmail.setError(viewModel.emailValidation(input));
        };
        Runnable passwordValidation = () -> {
            String input = binding.edtSignupPassword.getText().toString();
            //Log.d(TAG, "Validating: " + input + (viewModel.passwordValidation(input) ? " : valid" : " : invalid"));
            binding.edtSignupPassword.setError(viewModel.passwordValidation(input));
        };
        binding.edtSignupEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                handler.removeCallbacks(emailValidation);
                handler.postDelayed(emailValidation, TYPING_DELAY);
            }
        });
        binding.edtSignupPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                handler.removeCallbacks(passwordValidation);
                handler.postDelayed(passwordValidation, TYPING_DELAY);
            }
        });
    }
    //endregion
    private <T extends Class> void loadActivity(T c){
        Intent intent = new Intent(getApplicationContext(), c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    //region Event handling
    @Subscribe
    public void onBackButtonClicked(BackButtonClicked event){
        // Called when user clicks Back button
//        Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
        loadActivity(WelcomeActivity.class);
    }

    @Subscribe
    public void onSignInButtonClicked(SignInButtonClicked event){
        // Called when user clicks "Already have an account"
        loadActivity(SignInActivity.class);
//        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
//
//        startActivity(intent);
    }

    @Subscribe
    public void onSignUpButtonClicked(SignUpButtonClicked event){
        String name = binding.edtSignupName.getText().toString();
        String email = binding.edtSignupEmail.getText().toString();
        String password = binding.edtSignupPassword.getText().toString();
        binding.txtSignupErrorMess.setText("");
        if (viewModel.nameValidation(name) == null && viewModel.emailValidation(email) == null && viewModel.passwordValidation(password) == null){
            Log.d(TAG, "Registering user with name: " + binding.edtSignupName.getText().toString()
                    + " email: " + binding.edtSignupEmail.getText().toString()
                    + " password: " + binding.edtSignupPassword.getText().toString());
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.d(TAG, "Sign up successfully");
                    loadActivity(MainActivity.class);
                }
                else{
                    if (task.getException() != null) {
                        Log.d(TAG, "Failure: " + task.getException().getMessage());
                        binding.txtSignupErrorMess.setText(task.getException().getLocalizedMessage());
                    }
                    else
                        binding.txtSignupErrorMess.setText(getResources().getString(R.string.unexpected_error_occur));
                }
            }
            });
        }
        else {
            binding.edtSignupName.setError(viewModel.nameValidation(name));
            binding.edtSignupEmail.setError(viewModel.emailValidation(email));
            binding.edtSignupPassword.setError(viewModel.passwordValidation(password));
        }


    }
    //endregion
}