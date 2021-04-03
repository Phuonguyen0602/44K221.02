package com.ecdue.lim.features.main_screen.scan;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ecdue.lim.utils.DatabaseHelper;

public class ScanViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public ScanViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
    public void onWriteTestClicked(){
        //Log.d(DatabaseHelper.TAG, "User click Write test");
        DatabaseHelper.getInstance().writeTest();
    }
    public void onReadTestClicked(){
        DatabaseHelper.getInstance().readTestData();
    }
    public void turnOffCloudSync(){
        DatabaseHelper.getInstance().disableCloudSync();
    }
    public void turnOnCloudSync(){
        DatabaseHelper.getInstance().enableCloudSync();
    }


}