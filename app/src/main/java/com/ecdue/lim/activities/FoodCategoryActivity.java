package com.ecdue.lim.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.ecdue.lim.R;
import com.ecdue.lim.databinding.ActivityFoodCategoryBinding;
import com.ecdue.lim.viewmodels.FoodCategoryViewModel;

public class FoodCategoryActivity extends AppCompatActivity {
    private ActivityFoodCategoryBinding binding;
    private FoodCategoryViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_food_category);
        viewModel = new ViewModelProvider(this).get(FoodCategoryViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
    }
}