package com.ecdue.lim.features.main_screen.storage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ecdue.lim.data.Product;
import com.ecdue.lim.utils.DatabaseHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class StorageViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<Boolean> showLoading = new MutableLiveData<>(false);
    private ArrayList<Product> foods, cosmetics, medicines;
    private ArrayList<Product> allProducts;
    private final DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
    private StorageAdapter adapter;
    public StorageViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }
    public ArrayList<Product> getData(StorageAdapter adapter){
        this.adapter = adapter;
        showLoading.setValue(true);
        foods = databaseHelper.getFoods();
        cosmetics = databaseHelper.getCosmetics();
        medicines = databaseHelper.getMedicines();

        allProducts = new ArrayList<>();
        allProducts.addAll(foods);
        allProducts.addAll(cosmetics);
        allProducts.addAll(medicines);
        Collections.sort(allProducts, new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                return Long.compare(o1.getExpire(), o2.getExpire());
            }
        });

        showLoading.setValue(false);
        return allProducts;
    }
    public void onMenuButtonClicked(){

    }
    public void searchProduct(String name){
        if (!name.equals("")) {
            ArrayList<Product> searchResults = new ArrayList<>();
            for (Product product : allProducts){
                String productName = product.getName().toLowerCase();
                name = name.toLowerCase();
                if (productName.contains(name))
                    searchResults.add(product);
            }
            adapter.setProducts(searchResults);
        }
        else {
            adapter.setProducts(allProducts);
        }
        adapter.notifyDataSetChanged();
    }
    public LiveData<String> getText() {
        return mText;
    }

    public MutableLiveData<Boolean> getShowLoading() {
        return showLoading;
    }

    public void setShowLoading(MutableLiveData<Boolean> showLoading) {
        this.showLoading = showLoading;
    }
}