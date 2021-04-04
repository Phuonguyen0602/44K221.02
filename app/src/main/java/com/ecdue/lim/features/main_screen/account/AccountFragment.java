package com.ecdue.lim.features.main_screen.account;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ecdue.lim.R;
import com.ecdue.lim.databinding.FragmentAccountBinding;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class AccountFragment extends Fragment {
    private AccountViewModel viewModel;
    private FragmentAccountBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel =
                new ViewModelProvider(this).get(AccountViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
//        View root = inflater.inflate(R.layout.fragment_home, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        viewModel.initialize();
        Uri userPicture = viewModel.getUserPicture();
        if (userPicture != null) {
            Glide.with(this)
                    .load(userPicture)
                    .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(180, 0, RoundedCornersTransformation.CornerType.ALL)))
                    .into(binding.imgAccountPicture);
        }
        else {
            Glide.with(this)
                    .load(R.drawable.anonymous_avt)
                    .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(128, 0, RoundedCornersTransformation.CornerType.ALL)))
                    .into(binding.imgAccountPicture);
        }
        return binding.getRoot();
    }}
