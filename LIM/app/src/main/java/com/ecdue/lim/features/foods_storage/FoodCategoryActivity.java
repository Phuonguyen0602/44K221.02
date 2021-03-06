package com.ecdue.lim.features.foods_storage;

import androidx.core.app.NotificationCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import com.ecdue.lim.R;
import com.ecdue.lim.base.BaseAddProductActivity;
import com.ecdue.lim.databinding.ActivityFoodCategoryBinding;
import com.ecdue.lim.events.BackButtonClickedEvent;
import com.ecdue.lim.events.CreateNotificationEvent;
import com.ecdue.lim.events.ShowConfirmDeleteEvent;
import com.ecdue.lim.features.authentication.welcome.WelcomeActivity;
import com.ecdue.lim.utils.NotificationUtil;

import org.greenrobot.eventbus.Subscribe;

public class FoodCategoryActivity extends BaseAddProductActivity {
    public static final String TAG = FoodCategoryActivity.class.getSimpleName();
    private ActivityFoodCategoryBinding binding;
    private FoodCategoryViewModel viewModel;

    public static final String BARCODE = "barcode";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_food_category);
        viewModel = new ViewModelProvider(this).get(FoodCategoryViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);


        initRecyclerView();
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
        RecyclerView foodsList = binding.rvFoods;
        FoodAdapter foodAdapter = new FoodAdapter();
        foodAdapter.setProducts(viewModel.getData(foodAdapter));
        foodAdapter.setViewModel(viewModel);
        foodsList.setAdapter(foodAdapter);
        foodsList.setLayoutManager(new LinearLayoutManager(this));
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

    @Subscribe
    public void onCreateNotificationEvent(CreateNotificationEvent event){
        Intent intent = new Intent(this, WelcomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationUtil.createNotification(
                this,
                0,
                R.drawable.app_logo_small,
                "Expiration date reminder",
                "Your product will expire on 14/4/2021",
                NotificationCompat.PRIORITY_DEFAULT,
                true,
                pendingIntent
                );
    }
}