package com.ecdue.lim.features.main_screen.storage;

import android.util.Log;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.ecdue.lim.data.Product;
import com.ecdue.lim.events.PopupMenuClickedEvent;
import com.ecdue.lim.utils.DatabaseHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

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
    public void onMenuButtonClicked(View view, StorageViewModel viewModel){
        EventBus.getDefault().post(new PopupMenuClickedEvent(view, viewModel));
    }
    public void searchProduct(String name){
        if (adapter == null) return;
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
    public void onDeleteExpiredFoodsClicked(){
        ArrayList<Integer> deleteIndexList = new ArrayList<>();
        for (int i = 0; i < adapter.getItemCount(); i++){
            if (adapter.getProducts().get(i).getCategory().equals(DatabaseHelper.CATEGORY_FOOD) && adapter.getProducts().get(i).isExpired()){
                deleteIndexList.add(i);
            }
        }
        for (int i = deleteIndexList.size() - 1; i >= 0; i--){
            adapter.getProducts().remove(deleteIndexList.get(i).intValue());
            adapter.notifyItemRemoved(deleteIndexList.get(i));
        }
        DatabaseHelper.getInstance().deleteExpiredProducts(DatabaseHelper.CATEGORY_FOOD);

    }
    public void onDeleteExpiredCosmeticsClicked(){
        ArrayList<Integer> deleteIndexList = new ArrayList<>();
        for (int i = 0; i < adapter.getItemCount(); i++){
            if (adapter.getProducts().get(i).getCategory().equals(DatabaseHelper.CATEGORY_COSMETIC) && adapter.getProducts().get(i).isExpired()){
                deleteIndexList.add(i);
            }
        }
        for (int i = deleteIndexList.size() - 1; i >= 0; i--){
            adapter.getProducts().remove(deleteIndexList.get(i).intValue());
            adapter.notifyItemRemoved(deleteIndexList.get(i));
        }
        DatabaseHelper.getInstance().deleteExpiredProducts(DatabaseHelper.CATEGORY_COSMETIC);
    }
    public void onDeleteExpiredMedicinesClicked(){
        ArrayList<Integer> deleteIndexList = new ArrayList<>();
        for (int i = 0; i < adapter.getItemCount(); i++){
            if (adapter.getProducts().get(i).getCategory().equals(DatabaseHelper.CATEGORY_MEDICINE) && adapter.getProducts().get(i).isExpired()){
                deleteIndexList.add(i);
            }
        }
        for (int i = deleteIndexList.size() - 1; i >= 0; i--){
            adapter.getProducts().remove(deleteIndexList.get(i).intValue());
            adapter.notifyItemRemoved(deleteIndexList.get(i));
        }
        DatabaseHelper.getInstance().deleteExpiredProducts(DatabaseHelper.CATEGORY_MEDICINE);
    }
    public void onDeleteAllExpiredClick(){
        onDeleteExpiredFoodsClicked();
        onDeleteExpiredCosmeticsClicked();
        onDeleteExpiredMedicinesClicked();
    }
}