package com.ecdue.lim.base;

import android.content.Context;
import android.content.Intent;

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
    protected  <T extends Class> void loadActivity(T c){
        Intent intent = new Intent(getApplicationContext(), c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    protected Context getContext(){
        return this;
    }
}
