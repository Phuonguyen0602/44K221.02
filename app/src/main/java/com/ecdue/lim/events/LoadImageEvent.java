package com.ecdue.lim.events;

import android.widget.ImageView;

import com.ecdue.lim.base.BaseEvent;

public class LoadImageEvent extends BaseEvent {
    private ImageView imageView;

    public LoadImageEvent(ImageView imageView) {
        super("");
        this.imageView = imageView;
    }

    public ImageView getImageView() {
        return imageView;
    }

}
