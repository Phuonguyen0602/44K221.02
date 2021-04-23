package com.ecdue.lim.features.main_screen.scan;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ecdue.lim.events.SearchByScanBarcodeEvent;

import org.greenrobot.eventbus.EventBus;

public class ScanViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public ScanViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }
    public void initialize(){

    }
    public void onScanClicked(){
        EventBus.getDefault().post(new SearchByScanBarcodeEvent());
    }
    public LiveData<String> getText() {
        return mText;
    }

}