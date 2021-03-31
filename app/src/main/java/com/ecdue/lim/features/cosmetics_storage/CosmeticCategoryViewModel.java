package com.ecdue.lim.features.cosmetics_storage;

import androidx.lifecycle.ViewModel;

import com.ecdue.lim.data.Product;
import com.ecdue.lim.events.ShowAddItemDialog;
import com.ecdue.lim.features.foods_storage.FoodAdapter;
import com.ecdue.lim.utils.DatabaseHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class CosmeticCategoryViewModel extends ViewModel {
    private ArrayList<Product> products;
    public CosmeticCategoryViewModel(){

    }
    public void onAddItemClicked(){
        EventBus.getDefault().post(new ShowAddItemDialog(""));
    }

    public ArrayList<Product> getData(CosmeticAdapter adapter){
        products = DatabaseHelper.getInstance().getCosmetics(adapter);
        return products;
    }
}
