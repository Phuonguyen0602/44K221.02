package com.ecdue.lim.features.cosmetics_storage;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
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

    private CosmeticAdapter adapter;
    private MutableLiveData<Boolean> showSearchBar = new MutableLiveData<>(false);
    public CosmeticCategoryViewModel(){

    }
    public void onAddItemClicked(){
        EventBus.getDefault().post(new ShowAddItemDialog(""));
    }

    public ArrayList<Product> getData(CosmeticAdapter adapter){
        products = DatabaseHelper.getInstance().getCosmetics(adapter);
        this.adapter = adapter;
        return products;
    }
    public void onDeleteClicked(int id){
        Log.d("Delete item", "Item at position " + id + " selected");
        EventBus.getDefault().post(new ShowConfirmDeleteEvent(DatabaseHelper.CATEGORY_COSMETIC, id));

    }
    public void deleteConfirmed(ShowConfirmDeleteEvent event){
        DatabaseHelper.getInstance().deleteProduct(event.getCategory(), adapter.getProducts().get(event.getPosition()));
        //adapter.notifyItemRemoved(event.getPosition());
        adapter.getProducts().remove(event.getPosition());
        adapter.notifyItemRemoved(event.getPosition());
    }

    public void onBackPressed(){
        if (!showSearchBar.getValue())
            EventBus.getDefault().post(new BackButtonClicked(""));
        else {
            showSearchBar.setValue(false);
            searchProduct("");
        }
    }

    public void onSearchClicked(){
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
