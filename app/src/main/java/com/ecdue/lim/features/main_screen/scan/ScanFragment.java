package com.ecdue.lim.features.main_screen.scan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ecdue.lim.R;
import com.ecdue.lim.databinding.FragmentScanBinding;

public class ScanFragment extends Fragment {

    private ScanViewModel scanViewModel;
    private FragmentScanBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        scanViewModel =
                new ViewModelProvider(this).get(ScanViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_scan, container, false);
        binding.setViewModel(scanViewModel);
        binding.setLifecycleOwner(this);
//        View root = inflater.inflate(R.layout.fragment_scan, container, false);
//        final TextView textView = root.findViewById(R.id.text_dashboard);
//        scanViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return binding.getRoot();
    }
}