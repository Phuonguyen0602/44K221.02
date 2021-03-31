package com.ecdue.lim.features.cosmetics_storage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.ecdue.lim.R;
import com.ecdue.lim.base.BaseActivity;
import com.ecdue.lim.databinding.ActivityCosmeticCategoryBinding;
import com.ecdue.lim.events.ShowAddItemDialog;
import com.ecdue.lim.events.ShowDatePicker;
import com.ecdue.lim.features.add_item.AddItemFragment;
import com.ecdue.lim.features.foods_storage.FoodAdapter;
import com.ecdue.lim.features.foods_storage.FoodCategoryViewModel;

import org.greenrobot.eventbus.Subscribe;

public class CosmeticCategoryActivity extends BaseActivity {
    private ActivityCosmeticCategoryBinding binding;
    private CosmeticCategoryViewModel viewModel;
    private AddItemFragment addItemFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cosmetic_category);
        viewModel = new ViewModelProvider(this).get(CosmeticCategoryViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        initRecyclerView();
    }
    private void initRecyclerView(){
        RecyclerView cosmeticsList = binding.rvCosmetics;
        CosmeticAdapter cosmeticAdapter = new CosmeticAdapter();
        cosmeticAdapter.setProducts(viewModel.getData(cosmeticAdapter));
        cosmeticsList.setAdapter(cosmeticAdapter);
        cosmeticsList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Subscribe
    public void onShowAddItemDialog(ShowAddItemDialog event){
        addItemFragment = AddItemFragment.newInstance();
        addItemFragment.setDefaultCategory(1);
        addItemFragment.show(getSupportFragmentManager(), "");
    }
    @Subscribe
    public void onShowDatePicker(ShowDatePicker event){
        if (addItemFragment.isResumed())
            addItemFragment.showDatePickerDialog();
    }
}