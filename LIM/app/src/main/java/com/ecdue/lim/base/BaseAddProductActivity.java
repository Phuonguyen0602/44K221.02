package com.ecdue.lim.base;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.Glide;
import com.ecdue.lim.events.LoadImageEvent;
import com.ecdue.lim.events.ScanBarcodeEvent;
import com.ecdue.lim.events.SearchByScanBarcodeEvent;
import com.ecdue.lim.events.ShowAddItemDialogEvent;
import com.ecdue.lim.events.ShowDatePickerEvent;
import com.ecdue.lim.events.TakePictureEvent;
import com.ecdue.lim.features.add_item.AddItemFragment;
import com.ecdue.lim.features.cosmetics_storage.CosmeticCategoryActivity;
import com.ecdue.lim.features.foods_storage.FoodCategoryActivity;
import com.ecdue.lim.features.main_screen.scan.CustomCaptureActivity;
import com.ecdue.lim.features.medicines_storage.MedicineCategoryActivity;
import com.ecdue.lim.utils.BitmapUtil;
import com.ecdue.lim.utils.DatabaseHelper;
import com.ecdue.lim.utils.PermissionUtil;
import com.ecdue.lim.utils.SharedPreferenceUtil;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.greenrobot.eventbus.Subscribe;

public abstract class BaseAddProductActivity extends BaseActivity{
    public static final int SEARCH_BY_BARCODE_SCANNER = 18171;
    public static final int SCAN_FOR_BARCODE = 18172;
    protected AddItemFragment addItemFragment;
    protected ActivityResultLauncher<Intent> galleryLauncher;
    protected ImageView productImageView;
    protected ActivityResultLauncher<Intent> cameraLauncher;
    protected int defaultCategoryOption = 0;
    private MutableLiveData<String> tempLiveData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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
    @Subscribe
    public void onShowAddItemDialog(ShowAddItemDialogEvent event){
        addItemFragment = AddItemFragment.newInstance();
        addItemFragment.setDefaultCategory(defaultCategoryOption);
        addItemFragment.show(getSupportFragmentManager(), "");
    }
    @Subscribe
    public void onShowDatePicker(ShowDatePickerEvent event){
        if (addItemFragment.isResumed())
            addItemFragment.showDatePickerDialog();
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
    public void onScanBarcodeEvent(ScanBarcodeEvent event){
        if (PermissionUtil.hasPermission(this, PermissionUtil.CAMERA)) {
            IntentIntegrator integrator = new IntentIntegrator(this);
            this.tempLiveData = event.getBarcodeReceiver();
            integrator.setCaptureActivity(CustomCaptureActivity.class);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            integrator.setPrompt("Scan something");
            integrator.setOrientationLocked(false);
            integrator.setBeepEnabled(false);
            integrator.setRequestCode(SCAN_FOR_BARCODE);
            integrator.initiateScan();

        }
        else if (ActivityCompat.shouldShowRequestPermissionRationale(this, PermissionUtil.CAMERA) || !SharedPreferenceUtil.getHasRequestCameraBefore(this)) {
            SharedPreferenceUtil.setHasRequestCameraBefore(this, true);
            ActivityCompat.requestPermissions(this, PermissionUtil.CAMERA_PERM, PermissionUtil.CAMERA_REQUEST);
        }
        else {
            AlertDialog.Builder cameraPermDialog = new AlertDialog.Builder(this);
            cameraPermDialog.setTitle("Camera permission denied");
            cameraPermDialog.setMessage("The camera permission is required scan barcode. Please go to app settings and allow it!");
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

    @Subscribe
    public void onSearchByScanBarcodeEvent(SearchByScanBarcodeEvent event){
        if (PermissionUtil.hasPermission(this, PermissionUtil.CAMERA)) {
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setCaptureActivity(CustomCaptureActivity.class);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            integrator.setPrompt("Scan something");
            integrator.setOrientationLocked(false);
            integrator.setBeepEnabled(false);
            integrator.setRequestCode(SEARCH_BY_BARCODE_SCANNER);
            integrator.initiateScan();

        }
        else if (ActivityCompat.shouldShowRequestPermissionRationale(this, PermissionUtil.CAMERA) || !SharedPreferenceUtil.getHasRequestCameraBefore(this)) {
            SharedPreferenceUtil.setHasRequestCameraBefore(this, true);
            ActivityCompat.requestPermissions(this, PermissionUtil.CAMERA_PERM, PermissionUtil.CAMERA_REQUEST);
        }
        else {
            AlertDialog.Builder cameraPermDialog = new AlertDialog.Builder(this);
            cameraPermDialog.setTitle("Camera permission denied");
            cameraPermDialog.setMessage("The camera permission is required scan barcode. Please go to app settings and allow it!");
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
    protected void loadActivityWithBarcode(Class cl, String barcode){
        Intent intent = new Intent(this, cl);
        intent.putExtra(FoodCategoryActivity.BARCODE, barcode);
        startActivity(intent);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("ScanBarcode", "Request code: " + requestCode);
        switch (requestCode){
            case SEARCH_BY_BARCODE_SCANNER:
                IntentResult result = IntentIntegrator.parseActivityResult(resultCode, data);
                if(result != null) {
                    if(result.getContents() == null) {
                        Log.d("ScanBarcode", "Canceled");
                    } else {
                        String barcode = result.getContents();
                        String searchResult = DatabaseHelper.getInstance().searchProductCategoryWithBarcode(barcode);
                        if (searchResult == null)
                            searchResult = "";
                        switch (searchResult){
                            case DatabaseHelper.CATEGORY_FOOD:
                                loadActivityWithBarcode(FoodCategoryActivity.class, barcode);
                                break;
                            case DatabaseHelper.CATEGORY_COSMETIC:
                                loadActivityWithBarcode(CosmeticCategoryActivity.class, barcode);
                                break;
                            case DatabaseHelper.CATEGORY_MEDICINE:
                                loadActivityWithBarcode(MedicineCategoryActivity.class, barcode);
                                break;
                            default:
                                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                                builder.setMessage("No product found with this barcode!");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                builder.show();
                                break;
                        }
                    }
                } else {
                    super.onActivityResult(requestCode, resultCode, data);
                }
                break;
            case SCAN_FOR_BARCODE:
                IntentResult result1 = IntentIntegrator.parseActivityResult(resultCode, data);
                if(result1 != null) {
                    if(result1.getContents() == null) {
                        Log.d("ScanBarcode", "Canceled");
                    } else {
                        if (result1.getOriginalIntent().getExtras() != null) {
                            Log.d("ScanBarcode", result1.getContents());
                            if (tempLiveData != null) {
                                tempLiveData.setValue(result1.getContents());
                                tempLiveData.postValue(tempLiveData.getValue());
                                tempLiveData = null;
                            }
                            else
                                Log.d("ScanBarcode", "LiveData is null");
                        }
                    }
                } else {
                    super.onActivityResult(requestCode, resultCode, data);
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                Log.d("ScanBarcode", "No requestcode found");
                break;
        }

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
