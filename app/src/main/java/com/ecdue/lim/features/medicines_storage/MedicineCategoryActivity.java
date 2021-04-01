package com.ecdue.lim.features.medicines_storage;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ecdue.lim.R;
import com.ecdue.lim.base.BaseActivity;
import com.ecdue.lim.databinding.ActivityMedicineCategoryBinding;
import com.ecdue.lim.events.LoadImageEvent;
import com.ecdue.lim.events.ShowAddItemDialog;
import com.ecdue.lim.events.ShowDatePicker;
import com.ecdue.lim.features.add_item.AddItemFragment;
import com.ecdue.lim.features.cosmetics_storage.CosmeticCategoryActivity;
import com.ecdue.lim.utils.DatabaseHelper;

import org.greenrobot.eventbus.Subscribe;

public class MedicineCategoryActivity extends BaseActivity {
    private ActivityMedicineCategoryBinding binding;
    private MedicineCategoryViewModel viewModel;
    private AddItemFragment addItemFragment;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private ImageView productImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MedicineCategoryViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_medicine_category);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        initRecyclerView();
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData().getData() != null && productImageView != null){
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
                            Glide.with(MedicineCategoryActivity.this)
                                    .load(selectedImage)
                                    .into(productImageView);


                        }
                    }
                });
    }
    private void initRecyclerView(){
        RecyclerView medicinesList = binding.rvMedicines;
        MedicineAdapter medicineAdapter = new MedicineAdapter();
        medicineAdapter.setProducts(viewModel.getData(medicineAdapter));
        medicinesList.setAdapter(medicineAdapter);
        medicinesList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Subscribe
    public void onShowAddItemDialog(ShowAddItemDialog event){
        addItemFragment = AddItemFragment.newInstance();
        addItemFragment.setDefaultCategory(2);
        addItemFragment.show(getSupportFragmentManager(), "");
    }
    @Subscribe
    public void onShowDatePicker(ShowDatePicker event){
        if (addItemFragment.isResumed())
            addItemFragment.showDatePickerDialog();
    }
    @Subscribe
    public void onLoadImageEvent(LoadImageEvent event){
        //if (checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE))
        this.productImageView = event.getImageView();
        galleryLauncher.launch(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
    }
}