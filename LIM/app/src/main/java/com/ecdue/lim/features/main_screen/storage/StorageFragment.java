package com.ecdue.lim.features.main_screen.storage;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecdue.lim.R;
import com.ecdue.lim.databinding.FragmentStorageBinding;
import com.ecdue.lim.features.foods_storage.FoodAdapter;

public class StorageFragment extends Fragment {

    private StorageViewModel viewModel;
    private FragmentStorageBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel =
                new ViewModelProvider(this).get(StorageViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_storage, container, false);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        binding.edtStorageSearch.addTextChangedListener(new TextWatcher() {
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

        initRecyclerView();
        return binding.getRoot();
    }
    private void initRecyclerView(){
        RecyclerView foodsList = binding.rvStorage;
        StorageAdapter storageAdapter = new StorageAdapter();
        storageAdapter.setProducts(viewModel.getData(storageAdapter));
        storageAdapter.setViewModel(viewModel);
        foodsList.setAdapter(storageAdapter);
        foodsList.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}