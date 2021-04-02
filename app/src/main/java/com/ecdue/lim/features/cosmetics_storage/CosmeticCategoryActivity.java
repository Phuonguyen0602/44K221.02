package com.ecdue.lim.features.cosmetics_storage;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ecdue.lim.R;
import com.ecdue.lim.base.BaseActivity;
import com.ecdue.lim.base.BaseAddProductActivity;
import com.ecdue.lim.databinding.ActivityCosmeticCategoryBinding;
import com.ecdue.lim.events.BackButtonClicked;
import com.ecdue.lim.events.LoadImageEvent;
import com.ecdue.lim.events.ShowAddItemDialog;
import com.ecdue.lim.events.ShowConfirmDeleteEvent;
import com.ecdue.lim.events.ShowDatePicker;
import com.ecdue.lim.features.add_item.AddItemFragment;
import com.ecdue.lim.features.foods_storage.FoodAdapter;
import com.ecdue.lim.features.foods_storage.FoodCategoryActivity;
import com.ecdue.lim.features.foods_storage.FoodCategoryViewModel;
import com.ecdue.lim.utils.DatabaseHelper;

import org.greenrobot.eventbus.Subscribe;

public class CosmeticCategoryActivity extends BaseAddProductActivity {
    private ActivityCosmeticCategoryBinding binding;
    private CosmeticCategoryViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cosmetic_category);
        viewModel = new ViewModelProvider(this).get(CosmeticCategoryViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        initRecyclerView();
        defaultCategoryOption = 1;
    }
    private void initRecyclerView(){
        RecyclerView cosmeticsList = binding.rvCosmetics;
        CosmeticAdapter cosmeticAdapter = new CosmeticAdapter();
        cosmeticAdapter.setProducts(viewModel.getData(cosmeticAdapter));
        cosmeticsList.setAdapter(cosmeticAdapter);
        cosmeticsList.setLayoutManager(new LinearLayoutManager(this));
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