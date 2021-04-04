package com.ecdue.lim.features.main_screen.account;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ecdue.lim.events.OpenFacebookEvent;
import com.ecdue.lim.events.OpenGmailEvent;
import com.ecdue.lim.events.SignOutEvent;
import com.ecdue.lim.utils.DatabaseHelper;

import org.greenrobot.eventbus.EventBus;

public class AccountViewModel extends ViewModel {

    private MutableLiveData<String> userName = new MutableLiveData<>();

    public AccountViewModel(){

    }

    public void initialize(){
        String name = DatabaseHelper.getInstance().getUserName();
        userName.setValue(name);
    }
    public Uri getUserPicture(){
        return DatabaseHelper.getInstance().getUserPicture();
    }

    public MutableLiveData<String> getUserName() {
        return userName;
    }

    public void setUserName(MutableLiveData<String> userName) {
        this.userName = userName;
    }

    public void onSignOutClicked(){
        EventBus.getDefault().post(new SignOutEvent(""));
    }

    public void onNotificationSettingsClicked(){

    }

    public void onFacebookClicked(){
        EventBus.getDefault().post(new OpenFacebookEvent());
    }

    public void onEmailClicked(){
        EventBus.getDefault().post(new OpenGmailEvent());
    }

}
