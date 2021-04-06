package com.ecdue.lim.events;

import androidx.lifecycle.MutableLiveData;

public class ScanBarcodeEvent {
    private MutableLiveData<String> barcodeReceiver;

    public ScanBarcodeEvent(MutableLiveData<String> barcodeReceiver) {
        this.barcodeReceiver = barcodeReceiver;
    }

    public MutableLiveData<String> getBarcodeReceiver() {
        return barcodeReceiver;
    }

    public void setBarcodeReceiver(MutableLiveData<String> barcodeReceiver) {
        this.barcodeReceiver = barcodeReceiver;
    }
}
