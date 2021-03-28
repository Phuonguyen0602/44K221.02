package com.ecdue.lim.features.authentication.signin;

import android.util.Patterns;

import androidx.lifecycle.ViewModel;

import com.ecdue.lim.events.BackButtonClicked;
import com.ecdue.lim.events.SignInButtonClicked;
import com.ecdue.lim.events.SignInGoogleClicked;
import com.ecdue.lim.events.SignUpButtonClicked;

import org.greenrobot.eventbus.EventBus;

public class SignInViewModel extends ViewModel {
    public static final String TAG = SignInViewModel.class.getSimpleName();
    public SignInViewModel() {

    }

    public String emailValidation(String email){
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return "Wrong email format!";
        }
        else
            return null;
    }
    public String passwordValidation(String password){
        if (password.length() < 6)
            return "At least 6 characters!";
        else
            return null;
    }

    public void onBackPressed(){
        EventBus.getDefault().post(new BackButtonClicked(""));
    }
    public void onNoAccountPressed() {
        EventBus.getDefault().post(new SignUpButtonClicked(""));
    }
    public void onSignInPressed(){
        EventBus.getDefault().post(new SignInButtonClicked(""));
    }
    public void onSignInGoogleClicked(){
        EventBus.getDefault().post(new SignInGoogleClicked(""));
    }
}
