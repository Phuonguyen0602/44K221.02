package com.ecdue.lim.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.ecdue.lim.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class GoogleSignInUtils {
    public static final String TAG = GoogleSignInUtils.class.getSimpleName();
    private GoogleSignInClient signInClient;
    private final FirebaseAuth firebaseAuth;
    private final Context context;
    private final PublishSubject<Boolean> publishSubject = PublishSubject.create();

    public GoogleSignInUtils(Context context, FirebaseAuth auth){
        this.context = context;
        this.firebaseAuth = auth;
    }

    public void initialize(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        signInClient = GoogleSignIn.getClient(context, gso);
    }


    public Intent getSignInIntent(){
        return signInClient.getSignInIntent();
    }

    public void firebaseAuth(Intent data, Observer<Boolean> observer){
        publishSubject.subscribe(observer);
        Task<GoogleSignInAccount> task = com.google.android.gms.auth.api.signin.GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            // Google Sign In was successful, authenticate with Firebase
            GoogleSignInAccount account = task.getResult(ApiException.class);
            firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            // Google Sign In failed, update UI appropriately
            publishSubject.onError(e);
            Log.w(TAG, "Google sign in failed", e);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        publishSubject.onNext(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential", e);
                        publishSubject.onNext(false);
                    }
                });
    }
    //region get set
    public GoogleSignInClient getSignInClient() {
        return signInClient;
    }

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    //endregion
}
