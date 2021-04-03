package com.ecdue.lim.features.main_screen.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ecdue.lim.R;
import com.ecdue.lim.databinding.FragmentAccountBinding;

public class AccountFragment extends Fragment {
    private AccountViewModel accountViewModel;
    private FragmentAccountBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        accountViewModel =
                new ViewModelProvider(this).get(AccountViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false);
        binding.setViewModel(accountViewModel);
        binding.setLifecycleOwner(this);
//        View root = inflater.inflate(R.layout.fragment_home, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return binding.getRoot();
    }}
