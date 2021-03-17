package com.ecdue.lim.viewmodels;

import com.ecdue.lim.events.BackButtonClicked;
import com.ecdue.lim.events.SignInButtonClicked;

import org.greenrobot.eventbus.EventBus;


public class SignUpViewModel extends BaseViewModel {
    public static final String TAG = SignUpViewModel.class.getSimpleName();
    public SignUpViewModel() {

    }

    public void onBackPressed(){
        EventBus.getDefault().post(new BackButtonClicked(""));
    }
    // To sign in
    public void onHaveAccountPressed(){
        EventBus.getDefault().post(new SignInButtonClicked(""));
    }
}
