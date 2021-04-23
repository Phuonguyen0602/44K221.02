package com.ecdue.lim.features.authentication.signin;

import android.util.Patterns;

import androidx.lifecycle.ViewModel;

import com.ecdue.lim.events.BackButtonClickedEvent;
import com.ecdue.lim.events.SignInButtonClickedEvent;
import com.ecdue.lim.events.SignInGoogleClickedEvent;
import com.ecdue.lim.events.SignUpButtonClickedEvent;

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
        EventBus.getDefault().post(new BackButtonClickedEvent(""));
    }
    public void onNoAccountPressed() {
        EventBus.getDefault().post(new SignUpButtonClickedEvent(""));
    }
    public void onSignInPressed(){
        EventBus.getDefault().post(new SignInButtonClickedEvent(""));
    }
    public void onSignInGoogleClicked(){
        EventBus.getDefault().post(new SignInGoogleClickedEvent(""));
    }
}
