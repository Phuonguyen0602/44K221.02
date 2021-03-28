package com.ecdue.lim.features.main_screen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ecdue.lim.R;
import com.ecdue.lim.databinding.FragmentStorageBinding;

public class StorageFragment extends Fragment {

    private StorageViewModel storageViewModel;
    private FragmentStorageBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        storageViewModel =
                new ViewModelProvider(this).get(StorageViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_storage, container, false);
        binding.setViewModel(storageViewModel);
        binding.setLifecycleOwner(this);
//        View root = inflater.inflate(R.layout.fragment_storage, container, false);
//        final TextView textView = root.findViewById(R.id.text_notifications);
//        storageViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return binding.getRoot();
    }
}