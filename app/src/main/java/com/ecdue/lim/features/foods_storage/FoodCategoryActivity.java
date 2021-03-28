package com.ecdue.lim.features.foods_storage;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.ecdue.lim.R;
import com.ecdue.lim.base.BaseActivity;
import com.ecdue.lim.databinding.ActivityFoodCategoryBinding;
import com.ecdue.lim.events.ShowAddItemDialog;
import com.ecdue.lim.events.ShowDatePicker;
import com.ecdue.lim.features.add_item.AddItemFragment;

import org.greenrobot.eventbus.Subscribe;

public class FoodCategoryActivity extends BaseActivity {
    public static final String TAG = FoodCategoryActivity.class.getSimpleName();
    private ActivityFoodCategoryBinding binding;
    private FoodCategoryViewModel viewModel;
    private AddItemFragment addItemFragment;

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
        FoodAdapter foodAdapter = new FoodAdapter(viewModel.getData());
        foodsList.setAdapter(foodAdapter);
        foodsList.setLayoutManager(new LinearLayoutManager(this));
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
}