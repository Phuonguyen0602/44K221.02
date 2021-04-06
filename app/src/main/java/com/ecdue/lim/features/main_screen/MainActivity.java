package com.ecdue.lim.features.main_screen;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.ecdue.lim.R;
import com.ecdue.lim.base.BaseAddProductActivity;
import com.ecdue.lim.events.LoadActivityEvent;
import com.ecdue.lim.events.OpenFacebookEvent;
import com.ecdue.lim.events.OpenGmailEvent;
import com.ecdue.lim.events.PopupMenuClickedEvent;
import com.ecdue.lim.events.SignOutEvent;
import com.ecdue.lim.features.authentication.welcome.WelcomeActivity;
import com.ecdue.lim.utils.AlarmUtil;
import com.ecdue.lim.utils.DatabaseHelper;
import com.ecdue.lim.utils.GoogleSignInUtils;
import com.ecdue.lim.utils.NotificationUtil;
import com.ecdue.lim.utils.ReminderBroadcast;
import com.ecdue.lim.utils.SharedPreferenceUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends BaseAddProductActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    private FirebaseAuth auth;
    private GoogleSignInUtils googleSignInUtils;
    private BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navView = findViewById(R.id.nav_view);
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
        NotificationUtil.createNotificationChannel(this,
                NotificationManager.IMPORTANCE_DEFAULT,
                true,
                "General",
                "Channel for product expiration date notifications");
    }

    @Override
    public void onBackPressed() {
        if (navView.getSelectedItemId() == R.id.navigation_home){
            Intent intent = new Intent(getContext(), WelcomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(WelcomeActivity.SHOULD_FINISH, true);
            startActivity(intent);
        }
        else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onStop() {
        Log.d(TAG, "MainActivity onStop");
        super.onStop();
    }

    private void signOut(){
        DatabaseHelper.getInstance().removeAllNotifications();
        DatabaseHelper.getInstance().setFirstTimeSignIn(true);
        DatabaseHelper.getInstance().clearData();
        SharedPreferenceUtil.setAllowNotification(getContext(), true);
        FirebaseAuth.getInstance().signOut();
        googleSignInUtils.getSignInClient().signOut();
        loadActivity(WelcomeActivity.class);
        //TODO: remove all notifications
    }
    // Event from AccountViewModel
    @Subscribe
    public void onSignOutEvent(SignOutEvent event){
        Log.d(TAG, "Signing out");
        signOut();
    }
    // Event from HomeViewModel
    @Subscribe
    public void onLoadActivityEvent(LoadActivityEvent event){
        loadActivity(event.getaClass());
    }

    @Subscribe
    public void onPopupMenuClickedEvent(PopupMenuClickedEvent event){
        PopupMenu popupMenu = new PopupMenu(getContext(), event.getClickedView());
        popupMenu.getMenuInflater().inflate(R.menu.storage_popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.btn_storage_clear_foods:
                        event.getViewModel().onDeleteExpiredFoodsClicked();
                        break;
                    case R.id.btn_storage_clear_cosmetics:
                        event.getViewModel().onDeleteExpiredCosmeticsClicked();
                        break;
                    case R.id.btn_storage_clear_medicines:
                        event.getViewModel().onDeleteExpiredMedicinesClicked();
                        break;
                    case R.id.btn_storage_clear_all:
                        event.getViewModel().onDeleteAllExpiredClick();
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }

    private void registerNotificationAlarm() {
        Intent intent = new Intent(getContext(), ReminderBroadcast.class);
        intent.putExtra(ReminderBroadcast.TITLE, "Expiration date reminder");
        intent.putExtra(ReminderBroadcast.DESCRIPTION, "Hello from the other side");
        intent.putExtra(ReminderBroadcast.NOTIFICATION_ID, 0);
        intent.setType("test1234");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long timeClicked = System.currentTimeMillis();
        alarmManager.set(AlarmManager.RTC_WAKEUP,
                timeClicked + 1000*5,
                pendingIntent);
    }

    @Subscribe
    public void onOpenFacebookPageEvent(OpenFacebookEvent event){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(getResources().getString(R.string.app_facebook)));

        startActivity(intent);
    }
    @Subscribe
    public void onOpenGmailEvent(OpenGmailEvent event){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"letitmake.lim@gmail.com"});
        intent.setType("message/rfc822");
        intent.setPackage("com.google.android.gm");

        startActivity(intent);
    }
}