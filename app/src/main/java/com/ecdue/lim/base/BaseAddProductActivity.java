package com.ecdue.lim.base;

import android.content.Intent;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;

import com.ecdue.lim.events.ShowAddItemDialog;
import com.ecdue.lim.features.add_item.AddItemFragment;

import org.greenrobot.eventbus.Subscribe;

public abstract class BaseAddProductActivity extends BaseActivity{
    protected AddItemFragment addItemFragment;
    protected ActivityResultLauncher<Intent> galleryLauncher;
    protected ImageView productImageView;
    protected ActivityResultLauncher<Intent> cameraLauncher;
    @Subscribe
    public void onShowAddItemDialog(ShowAddItemDialog event){
        addItemFragment = AddItemFragment.newInstance();
        addItemFragment.show(getSupportFragmentManager(), "");
    }
}
