package com.ecdue.lim.features.medicines_storage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.ecdue.lim.base.BaseAddProductActivity;
import com.ecdue.lim.databinding.ActivityMedicineCategoryBinding;
import com.ecdue.lim.events.BackButtonClicked;
import com.ecdue.lim.events.LoadImageEvent;
import com.ecdue.lim.events.ShowAddItemDialog;
import com.ecdue.lim.events.ShowConfirmDeleteEvent;
import com.ecdue.lim.events.ShowDatePicker;
import com.ecdue.lim.features.add_item.AddItemFragment;
import com.ecdue.lim.features.cosmetics_storage.CosmeticCategoryActivity;
import com.ecdue.lim.utils.DatabaseHelper;

import org.greenrobot.eventbus.Subscribe;

import static com.ecdue.lim.features.foods_storage.FoodCategoryActivity.BARCODE;

public class MedicineCategoryActivity extends BaseAddProductActivity {
    private ActivityMedicineCategoryBinding binding;
    private MedicineCategoryViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MedicineCategoryViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_medicine_category);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        initRecyclerView();
        defaultCategoryOption = 2;
        binding.edtFoodSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.searchProduct(s.toString());
            }
        });
        if (getIntent() != null){
            if (getIntent().getExtras() != null && getIntent().getExtras().getString(BARCODE) != null) {
                viewModel.getShowSearchBar().setValue(true);
                binding.edtFoodSearch.setText(getIntent().getExtras().getString(BARCODE));
                viewModel.searchProduct(getIntent().getExtras().getString(BARCODE));
            }
        }
    }
    private void initRecyclerView(){
        RecyclerView medicinesList = binding.rvMedicines;
        MedicineAdapter medicineAdapter = new MedicineAdapter();
        medicineAdapter.setProducts(viewModel.getData(medicineAdapter));
        medicineAdapter.setViewModel(viewModel);
        medicinesList.setAdapter(medicineAdapter);
        medicinesList.setLayoutManager(new LinearLayoutManager(this));
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