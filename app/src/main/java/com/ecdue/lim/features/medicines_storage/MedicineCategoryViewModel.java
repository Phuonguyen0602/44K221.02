package com.ecdue.lim.features.medicines_storage;

import androidx.lifecycle.ViewModel;

import com.ecdue.lim.data.Product;
import com.ecdue.lim.events.ShowAddItemDialog;
import com.ecdue.lim.features.cosmetics_storage.CosmeticAdapter;
import com.ecdue.lim.utils.DatabaseHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class MedicineCategoryViewModel extends ViewModel {
    private ArrayList<Product> products;

    public MedicineCategoryViewModel() {
    }
    public void onAddItemClicked(){
        EventBus.getDefault().post(new ShowAddItemDialog(""));
    }

    public ArrayList<Product> getData(MedicineAdapter adapter){
        products = DatabaseHelper.getInstance().getMedicines(adapter);
        return products;
    }
}
