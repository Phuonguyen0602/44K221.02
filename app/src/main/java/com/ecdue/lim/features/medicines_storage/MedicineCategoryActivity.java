package com.ecdue.lim.features.medicines_storage;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecdue.lim.R;
import com.ecdue.lim.base.BaseActivity;
import com.ecdue.lim.databinding.ActivityMedicineCategoryBinding;
import com.ecdue.lim.events.ShowAddItemDialog;
import com.ecdue.lim.events.ShowDatePicker;
import com.ecdue.lim.features.add_item.AddItemFragment;

import org.greenrobot.eventbus.Subscribe;

public class MedicineCategoryActivity extends BaseActivity {
    private ActivityMedicineCategoryBinding binding;
    private MedicineCategoryViewModel viewModel;
    private AddItemFragment addItemFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MedicineCategoryViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_medicine_category);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        initRecyclerView();
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
}