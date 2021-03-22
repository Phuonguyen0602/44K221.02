package com.ecdue.lim.data;

import java.lang.reflect.Field;
import java.util.HashMap;

public class Product {
    private String name;
    private int quantity;
    private String unit;
    private String category;
    private long expire;
    private String imageUrl;
    private String barcode;
    private boolean toBeNotified;
    private boolean isExpire;

    public HashMap<String, Object> toHashMap() throws IllegalAccessException {
        HashMap<String, Object> result = new HashMap<>();
        for (Field field : Product.class.getDeclaredFields()){
            field.setAccessible(true);
            result.put(field.getName(), field.get(this));
        }
        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
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

    public void setExpire(long expire) {
        this.expire = expire;
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

    public boolean isExpire() {
        return isExpire;
    }

    public void setExpire(boolean expire) {
        isExpire = expire;
    }
}
