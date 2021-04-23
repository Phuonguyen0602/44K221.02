package com.ecdue.lim.features.authentication.signup;

import android.util.Log;
import android.util.Patterns;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.ecdue.lim.events.BackButtonClickedEvent;
import com.ecdue.lim.events.SignInButtonClickedEvent;
import com.ecdue.lim.events.SignInGoogleClickedEvent;
import com.ecdue.lim.events.SignUpButtonClickedEvent;
import com.ecdue.lim.base.BaseViewModel;
import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.EventBus;


public class SignUpViewModel extends BaseViewModel {
    public static final String TAG = SignUpViewModel.class.getSimpleName();
    public MutableLiveData<String> inputName = new MutableLiveData<>("");
    public MediatorLiveData<Boolean> mediatorLiveData = new MediatorLiveData<>();
    private MutableLiveData<String> inputEmail = new MutableLiveData<>("");
    private MutableLiveData<String> inputPassword = new MutableLiveData<>("");
    private FirebaseAuth auth;
    public SignUpViewModel() {
    }
    //region Getter & Setter
    public MutableLiveData<String> getInputName() {
        return inputName;
    }

    public void setInputName(MutableLiveData<String> inputName) {
        this.inputName = inputName;
    }

    public MutableLiveData<String> getInputEmail() {
        return inputEmail;
    }

    public void setInputEmail(MutableLiveData<String> inputEmail) {
        this.inputEmail = inputEmail;
    }

    public MutableLiveData<String> getInputPassword() {
        return inputPassword;
    }

    public void setInputPassword(MutableLiveData<String> inputPassword) {
        this.inputPassword = inputPassword;
    }
    //endregion

    //region Logic interfaces
    public String nameValidation(String name){
        if (name.equals(""))
            return "This field must not be empty!";
        else
            return null;
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
    //endregion

    public void onBackPressed(){
        EventBus.getDefault().post(new BackButtonClickedEvent(""));
    }
    // To sign in
    public void onHaveAccountPressed(){
        EventBus.getDefault().post(new SignInButtonClickedEvent(""));
    }

    public void onSignUpPressed(){
        Log.d(TAG, "Name: " + inputName.getValue() + " Email: " + inputEmail.getValue() + " Password: " + inputPassword.getValue());
        EventBus.getDefault().post(new SignUpButtonClickedEvent(""));
    }
    public void onGoogleSignUpPressed(){
        EventBus.getDefault().post(new SignInGoogleClickedEvent(""));
    }
}
