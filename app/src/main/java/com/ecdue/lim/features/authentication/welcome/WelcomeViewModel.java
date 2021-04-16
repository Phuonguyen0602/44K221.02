package com.ecdue.lim.features.authentication.welcome;

import com.ecdue.lim.events.SignInButtonClickedEvent;
import com.ecdue.lim.events.SignUpButtonClickedEvent;
import com.ecdue.lim.events.SkipSignInButtonClickedEvent;
import com.ecdue.lim.base.BaseViewModel;

import org.greenrobot.eventbus.EventBus;

public class WelcomeViewModel extends BaseViewModel {

    public WelcomeViewModel() {
    }

    public static final String TAG = WelcomeViewModel.class.getSimpleName();
    public void onSignInClicked(){
        EventBus.getDefault().post(new SignInButtonClickedEvent(""));
    }
    public void onSignUpClicked(){
        EventBus.getDefault().post(new SignUpButtonClickedEvent(""));
    }
    public void onSkipClicked() {
        EventBus.getDefault().post(new SkipSignInButtonClickedEvent(""));
    }
}
