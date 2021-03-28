package com.ecdue.lim.features.foods_storage;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModel;

import com.ecdue.lim.data.Product;
import com.ecdue.lim.events.ShowAddItemDialog;
import com.ecdue.lim.utils.DatabaseHelper;
import com.google.firebase.Timestamp;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class FoodCategoryViewModel extends ViewModel {
    private ArrayList<Product> products;
    public FoodCategoryViewModel(){

    }
    public void onAddItemClicked(){
        EventBus.getDefault().post(new ShowAddItemDialog(""));
    }

    public ArrayList<Product> getData(){
        products = DatabaseHelper.getInstance().getFoods();
        return products;
    }
}
