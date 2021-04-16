package com.ecdue.lim.features.cosmetics_storage;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import com.ecdue.lim.R;
import com.ecdue.lim.base.BaseAddProductActivity;
import com.ecdue.lim.databinding.ActivityCosmeticCategoryBinding;
import com.ecdue.lim.events.BackButtonClickedEvent;
import com.ecdue.lim.events.ShowConfirmDeleteEvent;

import org.greenrobot.eventbus.Subscribe;

public class CosmeticCategoryActivity extends BaseAddProductActivity {
    private static final String BARCODE = "barcode";
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
        RecyclerView cosmeticsList = binding.rvCosmetics;
        CosmeticAdapter cosmeticAdapter = new CosmeticAdapter();
        cosmeticAdapter.setProducts(viewModel.getData(cosmeticAdapter));
        cosmeticAdapter.setViewModel(viewModel);
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
    public void onBackButtonClicked(BackButtonClickedEvent event){
        finish();
    }
}