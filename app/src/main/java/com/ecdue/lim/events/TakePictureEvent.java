package com.ecdue.lim.events;

import android.widget.ImageView;

import com.ecdue.lim.base.BaseEvent;

public class TakePictureEvent extends BaseEvent {
    private ImageView imageView;

    public TakePictureEvent(ImageView imageView) {
        super("");
        this.imageView = imageView;
    }

    public ImageView getImageView() {
        return imageView;
    }
}
