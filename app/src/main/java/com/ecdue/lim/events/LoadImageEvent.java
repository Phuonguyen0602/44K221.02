package com.ecdue.lim.events;

import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.lifecycle.MutableLiveData;

import com.ecdue.lim.base.BaseEvent;

import java.util.BitSet;

public class LoadImageEvent extends BaseEvent {
    private MutableLiveData<Bitmap> imageHolder;
    private ImageView imageView;
    public LoadImageEvent(String message) {
        super(message);
    }

    public LoadImageEvent(MutableLiveData<Bitmap> imageHolder) {
        super("");
        this.imageHolder = imageHolder;
    }
    public LoadImageEvent(ImageView imageView) {
        super("");
        this.imageView = imageView;
    }
    public MutableLiveData<Bitmap> getImageHolder() {
        return imageHolder;
    }

    public void setImageHolder(MutableLiveData<Bitmap> imageHolder) {
        this.imageHolder = imageHolder;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
}
