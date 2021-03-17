package com.ecdue.lim.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;

import androidx.databinding.DataBindingUtil;

import com.ecdue.lim.R;
import com.ecdue.lim.databinding.ActivityWelcomeBinding;
import com.ecdue.lim.events.SignInButtonClicked;
import com.ecdue.lim.events.SignUpButtonClicked;
import com.ecdue.lim.events.SkipSignInButtonClicked;
import com.ecdue.lim.viewmodels.WelcomeViewModel;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class WelcomeActivity extends BaseActivity {
    public static final String TAG = WelcomeActivity.class.getSimpleName();
    private ActivityWelcomeBinding binding;
    private WelcomeViewModel viewModel;

    //region lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_welcome);
        viewModel = new WelcomeViewModel();
        binding.setViewModel(viewModel);

        //TODO: Check session (guest or signed in)
    }
    //endregion

    //region Event handling
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSignInOption(SignInButtonClicked message){
        // Called when user clicks "Sign in" button
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSignUpOption(SignUpButtonClicked message){
        // Called when user clicks "Sign up" button
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSkipSignInOption(SkipSignInButtonClicked message){
        // Called when user clicks "Skip for now" button
    }
    //endregion

    private void playAnimationOnClick(View v){
        v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_scale));
        v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_down));
    }
}