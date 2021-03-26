package com.ecdue.lim.viewmodels;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModel;

import com.ecdue.lim.events.ShowAddItemDialog;
import com.google.firebase.Timestamp;

import org.greenrobot.eventbus.EventBus;

public class FoodCategoryViewModel extends ViewModel {
    public FoodCategoryViewModel(){

    }
    public void onAddItemClicked(){
        EventBus.getDefault().post(new ShowAddItemDialog(""));
    }

}
