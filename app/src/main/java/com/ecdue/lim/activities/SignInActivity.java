package com.ecdue.lim.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.ecdue.lim.R;
import com.ecdue.lim.databinding.ActivitySignInBinding;
import com.ecdue.lim.events.BackButtonClicked;
import com.ecdue.lim.events.SignUpButtonClicked;
import com.ecdue.lim.viewmodels.SignInViewModel;

import org.greenrobot.eventbus.Subscribe;

public class SignInActivity extends BaseActivity {
    private ActivitySignInBinding binding;
    private SignInViewModel viewModel;
    //region lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in);
        viewModel = new SignInViewModel();
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
    public void onSignUpButtonClicked(SignUpButtonClicked event){
        // Called when user clicks "Don't have an account?"
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);

        startActivity(intent);
    }
    //endregion

}