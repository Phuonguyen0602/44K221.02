package com.ecdue.lim.features.foods_storage;

import android.util.Log;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModel;

import com.ecdue.lim.data.Product;
import com.ecdue.lim.events.BackButtonClicked;
import com.ecdue.lim.events.CreateNotificationEvent;
import com.ecdue.lim.events.ShowAddItemDialog;
import com.ecdue.lim.events.ShowConfirmDeleteEvent;
import com.ecdue.lim.utils.DatabaseHelper;
import com.google.firebase.Timestamp;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class FoodCategoryViewModel extends ViewModel {
    private ArrayList<Product> products;
    private String category = DatabaseHelper.CATEGORY_FOOD;
    public FoodCategoryViewModel(){

    }
    public void onAddItemClicked(){
        EventBus.getDefault().post(new ShowAddItemDialog(""));
    }

    public ArrayList<Product> getData(FoodAdapter adapter){
        products = DatabaseHelper.getInstance().getFoods(adapter);
        return products;
    }
    public void onDeleteClicked(int id){
        Log.d("Delete item", "Item at position " + id + " selected");
        EventBus.getDefault().post(new ShowConfirmDeleteEvent(DatabaseHelper.CATEGORY_FOOD, id));

    }
    public void deleteConfirmed(ShowConfirmDeleteEvent event){
        DatabaseHelper.getInstance().deleteProduct(event.getCategory(), products.get(event.getPosition()));
    }

    public void onBackPressed(){
        EventBus.getDefault().post(new BackButtonClicked(""));
    }

    public void onSearchClicked(){
        EventBus.getDefault().post(new CreateNotificationEvent());
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
