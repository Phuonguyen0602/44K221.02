package com.ecdue.lim.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.ecdue.lim.R;
import com.ecdue.lim.events.LoadActivityEvent;
import com.ecdue.lim.events.SignOutEvent;
import com.ecdue.lim.utils.GoogleSignInUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends BaseActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    private FirebaseAuth auth;
    private GoogleSignInUtils googleSignInUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_scan, R.id.navigation_storage, R.id.navigation_account)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        auth = FirebaseAuth.getInstance();
        googleSignInUtils = new GoogleSignInUtils(this, auth);
        googleSignInUtils.initialize();

        Toast.makeText(this, "Welcome back", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, WelcomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(WelcomeActivity.SHOULD_FINISH, true);
        startActivity(intent);
    }
    private void signOut(){
        FirebaseAuth.getInstance().signOut();
        googleSignInUtils.getSignInClient().signOut();
        loadActivity(WelcomeActivity.class);
    }
    @Subscribe
    public void onSignOutEvent(SignOutEvent event){
        Log.d(TAG, "Signing out");
        signOut();
    }
    @Subscribe
    public void onLoadActivityEvent(LoadActivityEvent event){
        loadActivity(event.getaClass());
    }
}