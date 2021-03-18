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
import com.ecdue.lim.databinding.ActivitySignInBinding;
import com.ecdue.lim.events.BackButtonClicked;
import com.ecdue.lim.events.SignInButtonClicked;
import com.ecdue.lim.events.SignUpButtonClicked;
import com.ecdue.lim.viewmodels.SignInViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.Subscribe;

public class SignInActivity extends BaseActivity {
    public static final String TAG = SignInActivity.class.getSimpleName();
    private ActivitySignInBinding binding;
    private SignInViewModel viewModel;
    private FirebaseAuth auth;
    private Handler handler;
    //region lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in);
        viewModel = new SignInViewModel();
        binding.setViewModel(viewModel);

        auth = FirebaseAuth.getInstance();
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
        Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    @Subscribe
    public void onSignUpButtonClicked(SignUpButtonClicked event){
        // Called when user clicks "Don't have an account?"
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);

        startActivity(intent);
    }
    @Subscribe
    public void onSignInButtonClicked(SignInButtonClicked event){
        String email = binding.edtSigninEmail.getText().toString();
        String password = binding.edtSigninPassword.getText().toString();
        if (viewModel.emailValidation(email) == null && viewModel.passwordValidation(password) == null) {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Log.d(TAG, "Sign in successfully");
                        loadActivity(MainActivity.class);
                    }
                    else{
                        if (task.getException() != null) {
                            Log.d(TAG, "Failure: " + task.getException().getMessage());
                            binding.txtSigninErrorMess.setText(task.getException().getLocalizedMessage());
                        }
                        else
                            binding.txtSigninErrorMess.setText(getResources().getString(R.string.unexpected_error_occur));
                    }
                }
            });
        }
        else {
            binding.edtSigninEmail.setError(viewModel.emailValidation(email));
            binding.edtSigninPassword.setError(viewModel.passwordValidation(password));
        }
    }
    //endregion

}