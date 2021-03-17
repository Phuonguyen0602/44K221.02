package com.ecdue.lim.viewmodels;

import androidx.lifecycle.ViewModel;

import com.ecdue.lim.events.BackButtonClicked;
import com.ecdue.lim.events.SignUpButtonClicked;

import org.greenrobot.eventbus.EventBus;

public class SignInViewModel extends ViewModel {
    public static final String TAG = SignInViewModel.class.getSimpleName();
    public SignInViewModel() {

    }
    public void onBackPressed(){
        EventBus.getDefault().post(new BackButtonClicked(""));
    }
    public void onNoAccountPressed() {
        EventBus.getDefault().post(new SignUpButtonClicked(""));
    }
}
