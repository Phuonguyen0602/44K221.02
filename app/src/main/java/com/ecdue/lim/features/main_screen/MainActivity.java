package com.ecdue.lim.features.main_screen;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.ecdue.lim.R;
import com.ecdue.lim.base.BaseActivity;
import com.ecdue.lim.base.BaseAddProductActivity;
import com.ecdue.lim.events.LoadActivityEvent;
import com.ecdue.lim.events.LoadImageEvent;
import com.ecdue.lim.events.ShowAddItemDialog;
import com.ecdue.lim.events.ShowDatePicker;
import com.ecdue.lim.events.SignOutEvent;
import com.ecdue.lim.events.TakePictureEvent;
import com.ecdue.lim.features.add_item.AddItemFragment;
import com.ecdue.lim.features.authentication.welcome.WelcomeActivity;
import com.ecdue.lim.utils.BitmapUtil;
import com.ecdue.lim.utils.DatabaseHelper;
import com.ecdue.lim.utils.GoogleSignInUtils;
import com.ecdue.lim.utils.PermissionUtil;
import com.ecdue.lim.utils.SharedPreferenceUtil;
import com.google.android.gms.common.util.SharedPreferencesUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

public class MainActivity extends BaseAddProductActivity {
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


        initActivityLauncher();
    }

    private void initActivityLauncher() {
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null && productImageView != null){
                            productImageView.setDrawingCacheEnabled(true);
                            Uri selectedImage = result.getData().getData();
                            String[] filePathColumn = { MediaStore.Images.Media.DATA };

                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            String picturePath = cursor.getString(columnIndex);
                            cursor.close();
                            addItemFragment.setAddedImageLocation(picturePath);

                            Log.d(TAG, "Picture path: " + picturePath);
                            Glide.with(getContext())
                                    .load(selectedImage)
                                    .into(productImageView);

                        }
                    }
                });
        cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null && productImageView != null){
                            productImageView.setDrawingCacheEnabled(true);
                            Bitmap image = (Bitmap) result.getData().getExtras().get("data");

                            Glide.with(getContext())
                                    .load(BitmapUtil.rotateImage(image,90))
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

    @Override
    protected void onStop() {
        Log.d(TAG, "MainActivity onStop");
        super.onStop();
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
    public void onShowDatePicker(ShowDatePicker event){
        if (addItemFragment.isResumed())
            addItemFragment.showDatePickerDialog();
        else
            Log.d(TAG, "addItemFragment is invisible");
    }

    @Subscribe
    public void onLoadImageEvent(LoadImageEvent event){
        this.productImageView = event.getImageView();
        if (PermissionUtil.hasPermissions(this, new String[]{PermissionUtil.READ_STORAGE, PermissionUtil.WRITE_STORAGE})) {
            galleryLauncher.launch(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
            //takePictureLauncher.launch(null);
        }
        else if (ActivityCompat.shouldShowRequestPermissionRationale(this, PermissionUtil.READ_STORAGE) || !PermissionUtil.requestStorageBefore) {
            PermissionUtil.requestStorageBefore = true;
            ActivityCompat.requestPermissions(this, PermissionUtil.STORAGE_PERM, PermissionUtil.STORAGE_REQUEST);
        }
        else {
            AlertDialog.Builder storagePermDialog = new AlertDialog.Builder(this);
            storagePermDialog.setTitle("Storage permission is denied");
            storagePermDialog.setMessage("The storage permission is required to load and store your product images. Please go to app settings and allow it!");
            storagePermDialog.setPositiveButton("Go to settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    openAppSettings();
                }
            });
            storagePermDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            storagePermDialog.show();
        }
    }

    private void openAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.parse("package:" + getApplicationContext().getPackageName());
        intent.setData(uri);
        startActivity(intent);
    }

    @Subscribe
    public void onTakePictureEvent(TakePictureEvent event){
        this.productImageView = event.getImageView();
        if (PermissionUtil.hasPermission(this, PermissionUtil.CAMERA)) {
            cameraLauncher.launch(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));
        }
        else if (ActivityCompat.shouldShowRequestPermissionRationale(this, PermissionUtil.CAMERA) || !SharedPreferenceUtil.getHasRequestCameraBefore(this)) {
            SharedPreferenceUtil.setHasRequestCameraBefore(this, true);
            ActivityCompat.requestPermissions(this, PermissionUtil.CAMERA_PERM, PermissionUtil.CAMERA_REQUEST);
        }
        else {
            AlertDialog.Builder cameraPermDialog = new AlertDialog.Builder(this);
            cameraPermDialog.setTitle("Camera permission denied");
            cameraPermDialog.setMessage("The camera permission is required to take a picture. Please go to app settings and allow it!");
            cameraPermDialog.setPositiveButton("Go to settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    openAppSettings();
                }
            });
            cameraPermDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            cameraPermDialog.show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionUtil.STORAGE_REQUEST:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    galleryLauncher.launch(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
                }
                break;
            case PermissionUtil.CAMERA_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    galleryLauncher.launch(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));
                }
                break;
        }
    }
}