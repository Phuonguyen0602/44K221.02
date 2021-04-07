package com.ecdue.lim.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.ecdue.lim.data.Product;

import java.util.Locale;

public class BindingAdapterUtil {
    @BindingAdapter("expireText")
    public static void setExpireText(TextView textView, long expire){
        long timeLeft = expire - DateTimeUtil.getCurrentDayTime();
        if (timeLeft < 0){
            textView.setText("Already expired on " + DateTimeUtil.milliSecToString(expire, DateTimeUtil.DEFAULT_DATE_FORMAT));
        }
        else if (timeLeft == 0){
            textView.setText("Expire today");
        }
        else {
            textView.setText("Expire on " + DateTimeUtil.milliSecToString(expire, DateTimeUtil.DEFAULT_DATE_FORMAT));
        }
    }
    @BindingAdapter("quantityText")
    public static void setQuantityText(TextView textView, Product product){
        if (product.getQuantity() > 0){
            if (product.getQuantity() - Math.floor(product.getQuantity()) == 0){
                textView.setText(String.format(Locale.US, "%d %s", (long)(product.getQuantity()), product.getUnit()));
            }
            else
                textView.setText(String.format(Locale.US, "%s %s", String.valueOf(product.getQuantity()), product.getUnit()));
        }
        else
            textView.setText("unknown");
    }
    @BindingAdapter("imageUrl")
    public static void loadProductImage(ImageView view, Product product){
        Log.d("Product", "Image location: " + product.getLocalImage());
        if (!product.getLocalImage().equals("") && Build.VERSION.SDK_INT <= 29){
            Log.d("Product", "Load local image at: " + product.getLocalImage());
            Bitmap image = BitmapFactory.decodeFile(product.getLocalImage());
            if (image != null)
                view.setImageBitmap(image);
            else {
                DatabaseHelper.getInstance().downloadImage(product.getImageUrl(), view);
            }
        }
        else if (product.getImageUrl() != null && !product.getImageUrl().equals("")){
            Log.d("Product", "Online url is not null");
            DatabaseHelper.getInstance().downloadImage(product.getImageUrl(), view);
        }
    }
}
