package com.ecdue.lim.data;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Product {
    private String name;
    private double quantity;
    private String unit;
    private String category;
    private long expire;
    private String imageUrl;
    private String barcode;
    private boolean toBeNotified;
    private boolean isExpired;

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
        this.imageUrl = "";
        this.barcode = "";
        this.toBeNotified = false;
        this.isExpired = false;
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
        product.setImageUrl((String) map.get("imageUrl"));
        product.setBarcode((String) map.get("barcode"));
        return product;
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
}
