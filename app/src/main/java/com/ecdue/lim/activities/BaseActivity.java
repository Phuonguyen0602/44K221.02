package com.ecdue.lim.activities;

import androidx.appcompat.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onStart() {
        EventBus.getDefault().register(this);
        super.onStart();
    }
    @Override
    protected void onStop(){
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}
