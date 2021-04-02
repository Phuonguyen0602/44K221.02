package com.ecdue.lim.features.cosmetics_storage;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.ecdue.lim.data.Product;
import com.ecdue.lim.events.BackButtonClicked;
import com.ecdue.lim.events.ShowAddItemDialog;
import com.ecdue.lim.events.ShowConfirmDeleteEvent;
import com.ecdue.lim.features.foods_storage.FoodAdapter;
import com.ecdue.lim.utils.DatabaseHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class CosmeticCategoryViewModel extends ViewModel {
    private ArrayList<Product> products;
    private String category = DatabaseHelper.CATEGORY_COSMETIC;
    public CosmeticCategoryViewModel(){

    }
    public void onAddItemClicked(){
        EventBus.getDefault().post(new ShowAddItemDialog(""));
    }

    public ArrayList<Product> getData(CosmeticAdapter adapter){
        products = DatabaseHelper.getInstance().getCosmetics(adapter);
        return products;
    }
    public void onDeleteClicked(int id){
        Log.d("Delete item", "Item at position " + id + " selected");
        EventBus.getDefault().post(new ShowConfirmDeleteEvent(DatabaseHelper.CATEGORY_COSMETIC, id));

    }
    public void deleteConfirmed(ShowConfirmDeleteEvent event){
        DatabaseHelper.getInstance().deleteProduct(event.getCategory(), products.get(event.getPosition()));
    }

    public void onBackPressed(){
        EventBus.getDefault().post(new BackButtonClicked(""));
    }

    public void onSearchClicked(){

    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
