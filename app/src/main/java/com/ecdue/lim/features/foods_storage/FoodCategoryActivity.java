package com.ecdue.lim.features.foods_storage;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
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

import com.bumptech.glide.Glide;
import com.ecdue.lim.R;
import com.ecdue.lim.base.BaseActivity;
import com.ecdue.lim.base.BaseAddProductActivity;
import com.ecdue.lim.databinding.ActivityFoodCategoryBinding;
import com.ecdue.lim.events.BackButtonClicked;
import com.ecdue.lim.events.LoadImageEvent;
import com.ecdue.lim.events.ShowAddItemDialog;
import com.ecdue.lim.events.ShowConfirmDeleteEvent;
import com.ecdue.lim.events.ShowDatePicker;
import com.ecdue.lim.events.TakePictureEvent;
import com.ecdue.lim.features.add_item.AddItemFragment;
import com.ecdue.lim.features.authentication.welcome.WelcomeActivity;
import com.ecdue.lim.features.main_screen.MainActivity;
import com.ecdue.lim.utils.BitmapUtil;
import com.ecdue.lim.utils.DatabaseHelper;
import com.ecdue.lim.utils.PermissionUtil;
import com.ecdue.lim.utils.SharedPreferenceUtil;

import org.greenrobot.eventbus.Subscribe;

public class FoodCategoryActivity extends BaseAddProductActivity {
    public static final String TAG = FoodCategoryActivity.class.getSimpleName();
    private ActivityFoodCategoryBinding binding;
    private FoodCategoryViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_food_category);
        viewModel = new ViewModelProvider(this).get(FoodCategoryViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        initRecyclerView();
    }

    private void initRecyclerView(){
        RecyclerView foodsList = binding.rvFoods;
        FoodAdapter foodAdapter = new FoodAdapter();
        foodAdapter.setProducts(viewModel.getData(foodAdapter));
        foodAdapter.setViewModel(viewModel);
        foodsList.setAdapter(foodAdapter);
        foodsList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Subscribe
    public void onShowConfirmDeleteEvent(ShowConfirmDeleteEvent event){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete this product?");
        builder.setMessage("This action cannot be undone.");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                viewModel.deleteConfirmed(event);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
    @Subscribe
    public void onBackButtonClicked(BackButtonClicked event){
        finish();
    }
}