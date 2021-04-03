package com.ecdue.lim.data;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.ecdue.lim.utils.DatabaseHelper;
import com.ecdue.lim.utils.DateTimeUtil;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Product {
    private String name;
    private double quantity;
    private String unit;
    private String category;
    private long expire;
    private long addDate;
    private String imageUrl;
    private String barcode;
    private boolean toBeNotified;
    private boolean isExpired;
    private String localImage;
    private String id;
    public Product(String name, float quantity, String unit, String category, long expire, String imageUrl, String barcode, boolean toBeNotified, boolean isExpired) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.category = category;
        this.expire = expire;
        this.imageUrl = imageUrl;
        this.barcode = barcode;
        this.toBeNotified = toBeNotified;
        this.isExpired = isExpired;
    }

    public Product(){
        this.name = "";
        this.quantity = 0;
        this.unit = "";
        this.category = "";
        this.expire = 0;
        this.addDate = 0;
        this.imageUrl = "";
        this.localImage = "";
        this.barcode = "";
        this.toBeNotified = false;
        this.isExpired = false;
        this.id = "";
    }

    public HashMap<String, Object> toHashMap() throws IllegalAccessException {
        HashMap<String, Object> result = new HashMap<>();
        for (Field field : Product.class.getDeclaredFields()){
            field.setAccessible(true);
            result.put(field.getName(), field.get(this));
        }
        return result;
    }
    public static Product mapToProduct(Map<String, Object> map) {
        Product product = new Product();
        product.setName((String) map.get("name"));
        product.setQuantity((Double) map.get("quantity"));
        product.setUnit((String) map.get("unit"));
        product.setCategory((String) map.get("category"));
        product.setExpire((Long) map.get("expire"));
        product.setAddDate(map.get("addDate") == null ? 0 : (Long) map.get("addDate"));
        product.setImageUrl((String) map.get("imageUrl"));
        product.setLocalImage(map.get("localImage") == null ? "" : (String) map.get("localImage"));
        product.setBarcode((String) map.get("barcode"));
        return product;
    }
    @BindingAdapter("imgRes")
    public static void setImageResource(ImageView view, int resource) {
        view.setImageResource(resource);
    }

    public String getLocalImage() {
        return localImage;
    }

    public void setLocalImage(String localImage) {
        this.localImage = localImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expired) {
        this.expire = expired;
    }

    public long getAddDate() {
        return addDate;
    }

    public void setAddDate(long addDate) {
        this.addDate = addDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public boolean isToBeNotified() {
        return toBeNotified;
    }

    public void setToBeNotified(boolean toBeNotified) {
        this.toBeNotified = toBeNotified;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expire) {
        isExpired = expire;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
