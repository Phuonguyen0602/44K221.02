package com.ecdue.lim.features.foods_storage;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ecdue.lim.data.Product;
import com.ecdue.lim.events.BackButtonClickedEvent;
import com.ecdue.lim.events.ShowAddItemDialogEvent;
import com.ecdue.lim.events.ShowConfirmDeleteEvent;
import com.ecdue.lim.utils.DatabaseHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class FoodCategoryViewModel extends ViewModel {
    private ArrayList<Product> products;


    private String category = DatabaseHelper.CATEGORY_FOOD;
    private FoodAdapter adapter;
    private MutableLiveData<Boolean> showSearchBar = new MutableLiveData<>(false);

    public FoodCategoryViewModel(){

    }
    public void onAddItemClicked(){
        EventBus.getDefault().post(new ShowAddItemDialogEvent(""));
    }

    public ArrayList<Product> getData(FoodAdapter adapter){
        this.adapter = adapter;
        products = DatabaseHelper.getInstance().getFoods(adapter);
        return products;
    }
    public void onDeleteClicked(int id){
        Log.d("Delete item", "Item at position " + id + " selected");
        EventBus.getDefault().post(new ShowConfirmDeleteEvent(DatabaseHelper.CATEGORY_FOOD, id));

    }
    public void deleteConfirmed(ShowConfirmDeleteEvent event){
        DatabaseHelper.getInstance().deleteProduct(event.getCategory(), adapter.getProducts().get(event.getPosition()));
        //adapter.notifyItemRemoved(event.getPosition());
        adapter.getProducts().remove(event.getPosition());
        adapter.notifyItemRemoved(event.getPosition());

    }

    public void onBackPressed(){
        if (!showSearchBar.getValue())
            EventBus.getDefault().post(new BackButtonClickedEvent(""));
        else {
            showSearchBar.setValue(false);
            searchProduct("");
        }
    }

    public void onSearchClicked(){
        //EventBus.getDefault().post(new CreateNotificationEvent());
        showSearchBar.setValue(true);
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public MutableLiveData<Boolean> getShowSearchBar() {
        return showSearchBar;
    }

    public void setShowSearchBar(MutableLiveData<Boolean> showSearchBar) {
        this.showSearchBar = showSearchBar;
    }
    public void searchProduct(String name){
        if (adapter == null) return;
        if (!name.equals("")) {
            ArrayList<Product> searchResults = new ArrayList<>();
            for (Product product : products){
                String productName = product.getName().toLowerCase();
                name = name.toLowerCase();
                if (productName.contains(name) || (name.equals(product.getBarcode())))
                    searchResults.add(product);
            }
            adapter.setProducts(searchResults);
        }
        else {
            adapter.setProducts(products);
        }
        adapter.notifyDataSetChanged();
    }
}
