package com.ecdue.lim.features.authentication.signin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.ecdue.lim.R;
import com.ecdue.lim.base.BaseActivity;
import com.ecdue.lim.features.main_screen.MainActivity;
import com.ecdue.lim.databinding.ActivitySignInBinding;
import com.ecdue.lim.events.BackButtonClicked;
import com.ecdue.lim.events.SignInButtonClicked;
import com.ecdue.lim.events.SignInGoogleClicked;
import com.ecdue.lim.events.SignUpButtonClicked;
import com.ecdue.lim.features.authentication.signup.SignUpActivity;
import com.ecdue.lim.features.authentication.welcome.WelcomeActivity;
import com.ecdue.lim.utils.DatabaseHelper;
import com.ecdue.lim.utils.GoogleSignInUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.Subscribe;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class SignInActivity extends BaseActivity {
    public static final String TAG = SignInActivity.class.getSimpleName();
    private ActivitySignInBinding binding;
    private SignInViewModel viewModel;
    private FirebaseAuth auth;
    private Handler handler;
    private GoogleSignInUtils googleSignInUtils;
    private ActivityResultLauncher<Intent> googleSignInLauncher;
    //region lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in);
        viewModel = new SignInViewModel();
        binding.setViewModel(viewModel);

        auth = FirebaseAuth.getInstance();
        prepareForGoogleSignIn();
        
    }

    @Override
    protected <T extends Class> void loadActivity(T c) {
        if (c == MainActivity.class)
            DatabaseHelper.initInstance(getApplicationContext());
        super.loadActivity(c);
    }

    private void prepareForGoogleSignIn(){
        googleSignInUtils = new GoogleSignInUtils(this, auth);
        googleSignInUtils.initialize();
        googleSignInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Log.d(TAG, "Signing in with Google");
                        googleSignInUtils.firebaseAuth(result.getData(), new Observer<Boolean>() {
                            @Override
                            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                                showLoadingDialog();

                            }

                            @Override
                            public void onNext(@io.reactivex.rxjava3.annotations.NonNull Boolean success) {
                                hideLoadingDialog();
                                if (success){
                                    googleSignInSuccessfully();
                                }
                            }

                            @Override
                            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
                    }
                }
        );
    }
    private void googleSignInSuccessfully(){
        Log.d(TAG, "Sign in with google successfully!");
        Log.d(TAG, (auth.getCurrentUser() != null ? auth.getCurrentUser().getEmail() : "Google fail"));
        loadActivity(MainActivity.class);
    }
    private void googleSignUpFailed(){
        Toast.makeText(this, "Google authentication failed", Toast.LENGTH_SHORT).show();
    }
    //endregion

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
            showLoadingDialog();
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    hideLoadingDialog();
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

    @Subscribe
    public void onSignInGoogleClicked(SignInGoogleClicked event){
        Log.d(TAG, "Signing in with Google");
        googleSignInLauncher.launch(googleSignInUtils.getSignInIntent());
    }
    //endregion
    private void showLoadingDialog() {
        binding.layoutSigninLoading.setVisibility(View.VISIBLE);
    }
    private void hideLoadingDialog() {
        binding.layoutSigninLoading.setVisibility(View.GONE);
    }
}