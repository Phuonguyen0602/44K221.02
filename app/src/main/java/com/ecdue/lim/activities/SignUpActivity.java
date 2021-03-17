package com.ecdue.lim.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.ecdue.lim.R;
import com.ecdue.lim.databinding.ActivitySignUpBinding;
import com.ecdue.lim.events.BackButtonClicked;
import com.ecdue.lim.events.SignInButtonClicked;
import com.ecdue.lim.viewmodels.SignUpViewModel;

import org.greenrobot.eventbus.Subscribe;

public class SignUpActivity extends BaseActivity {
    private ActivitySignUpBinding binding;
    private SignUpViewModel viewModel;

    //region lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        viewModel = new SignUpViewModel();
        binding.setViewModel(viewModel);
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
    public void onSignInButtonClicked(SignInButtonClicked event){
        // Called when user clicks "Already have an account"
        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);

        startActivity(intent);
    }
    //endregion
}