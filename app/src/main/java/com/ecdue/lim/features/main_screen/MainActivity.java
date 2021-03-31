package com.ecdue.lim.features.main_screen;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.res.ResourcesCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.ecdue.lim.R;
import com.ecdue.lim.base.BaseActivity;
import com.ecdue.lim.events.LoadActivityEvent;
import com.ecdue.lim.events.LoadImageEvent;
import com.ecdue.lim.events.ShowAddItemDialog;
import com.ecdue.lim.events.ShowDatePicker;
import com.ecdue.lim.events.SignOutEvent;
import com.ecdue.lim.features.add_item.AddItemFragment;
import com.ecdue.lim.features.authentication.welcome.WelcomeActivity;
import com.ecdue.lim.utils.GoogleSignInUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends BaseActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    private FirebaseAuth auth;
    private GoogleSignInUtils googleSignInUtils;
    private AddItemFragment addItemFragment;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private ImageView productImageView;
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

        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData().getData() != null && productImageView != null){
                            Glide.with(MainActivity.this)
                                    .load(result.getData().getData())
                                    .into(productImageView);
                        }
                    }
                });
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
    public void onShowAddItemDialog(ShowAddItemDialog event){
        addItemFragment = AddItemFragment.newInstance();
        addItemFragment.show(getSupportFragmentManager(), "");
    }
    @Subscribe
    public void onShowDatePicker(ShowDatePicker event){
        if (addItemFragment.isResumed())
            addItemFragment.showDatePickerDialog();
        else
            Log.d(TAG, "addItemFragment is invisible");
    }
    @Subscribe
    public void onLoadImageEvent(LoadImageEvent event){
        //if (checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE))
        this.productImageView = event.getImageView();
        galleryLauncher.launch(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
    }
}