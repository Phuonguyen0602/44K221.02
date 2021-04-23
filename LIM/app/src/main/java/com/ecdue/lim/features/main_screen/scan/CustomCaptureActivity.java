package com.ecdue.lim.features.main_screen.scan;

import com.journeyapps.barcodescanner.CaptureActivity;

public class CustomCaptureActivity extends CaptureActivity {
    @Override
    public void onBackPressed() {
        finish();
    }
}
