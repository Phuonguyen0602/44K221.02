package com.ecdue.lim.features.authentication.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.ecdue.lim.R;
import com.ecdue.lim.base.BaseActivity;
import com.ecdue.lim.features.main_screen.MainActivity;
import com.ecdue.lim.features.authentication.signin.SignInActivity;
import com.ecdue.lim.features.authentication.signup.SignUpActivity;
import com.ecdue.lim.databinding.ActivityWelcomeBinding;
import com.ecdue.lim.events.SignInButtonClickedEvent;
import com.ecdue.lim.events.SignUpButtonClickedEvent;
import com.ecdue.lim.events.SkipSignInButtonClickedEvent;
import com.ecdue.lim.utils.DatabaseHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class WelcomeActivity extends BaseActivity {
    public static final String TAG = WelcomeActivity.class.getSimpleName();
    public static final String SHOULD_FINISH = "SHOULD_FINISH";
    private FirebaseAuth auth;
    private ActivityWelcomeBinding binding;
    private WelcomeViewModel viewModel;

    //region lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_welcome);
        viewModel = new WelcomeViewModel();
        binding.setViewModel(viewModel);


        auth = FirebaseAuth.getInstance();
        if (getIntent() != null){
            // This is executed when user close the app but not sign out
            if (getIntent().getBooleanExtra(SHOULD_FINISH, false)) {
                DatabaseHelper.getInstance().clearData();
                finish();
            }
        }
    }
    @Override
    protected void onStart(){
        super.onStart();
        if (auth.getCurrentUser() != null){
            Intent intent = new Intent(this, MainActivity.class);
            DatabaseHelper.initInstance(getApplicationContext());
            DatabaseHelper.getInstance().setFirstTimeSignIn(false);
            startActivity(intent);
        }
    }

    @Override
    protected <T extends Class> void loadActivity(T c) {
        if (c == MainActivity.class)
            DatabaseHelper.initInstance(getApplicationContext());
        super.loadActivity(c);
    }

    @Override
    protected void onStop(){
        super.onStop();
        // For debugging
    }
    //endregion

    //region Event handling
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSignInOption(SignInButtonClickedEvent message){
        // Called when user clicks "Sign in" button
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSignUpOption(SignUpButtonClickedEvent message){
        // Called when user clicks "Sign up" button
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSkipSignInOption(SkipSignInButtonClickedEvent message){
        // Called when user clicks "Skip for now" button
        showLoadingDialog();
        auth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                hideLoadingDialog();
                if (task.isSuccessful()){
                    loadActivity(MainActivity.class);
                    Log.d(TAG, "Anonymous user id: " + auth.getCurrentUser().getUid());
                }
                else
                    makeToast(task.getException().getLocalizedMessage());
            }
        });
    }
    //endregion
    private void makeToast(String m){
        Toast.makeText(this, m, Toast.LENGTH_SHORT).show();
    }
    private void playAnimationOnClick(View v){
        v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_scale));
        v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_down));
    }
    private void showLoadingDialog() {
        binding.layoutSigninLoading.setVisibility(View.VISIBLE);
    }
    private void hideLoadingDialog() {
        binding.layoutSigninLoading.setVisibility(View.GONE);
    }
}