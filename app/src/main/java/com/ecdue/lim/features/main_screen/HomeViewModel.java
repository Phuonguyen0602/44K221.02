package com.ecdue.lim.features.main_screen;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ecdue.lim.events.ShowAddItemDialog;
import com.ecdue.lim.features.cosmetics_storage.CosmeticCategoryActivity;
import com.ecdue.lim.features.foods_storage.FoodCategoryActivity;
import com.ecdue.lim.events.LoadActivityEvent;
import com.ecdue.lim.features.medicines_storage.MedicineCategoryActivity;
import com.ecdue.lim.utils.DatabaseHelper;
import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.EventBus;

public class HomeViewModel extends ViewModel {
    public static final String TAG = HomeViewModel.class.getSimpleName();
    private MutableLiveData<String> foodNumber = new MutableLiveData<>("");
    private MutableLiveData<String> cosmeticNumber = new MutableLiveData<>("");
    private MutableLiveData<String> medicineNumber = new MutableLiveData<>("");
    private MutableLiveData<String> foodStatus = new MutableLiveData<>("");
    private MutableLiveData<String> cosmeticStatus = new MutableLiveData<>("");
    private MutableLiveData<String> medicineStatus = new MutableLiveData<>("");
    private String userUid = FirebaseAuth.getInstance().getUid();


    public HomeViewModel() {

    }

    public void initialize(){
        foodNumber.setValue("test");
        DatabaseHelper.getInstance().getFoodQuantity(foodNumber);
        DatabaseHelper.getInstance().getCosmeticQuantity(cosmeticNumber);
        DatabaseHelper.getInstance().getMedicineQuantity(medicineNumber);
    }

    // MainActivity handles this
    public void onFoodCategoryClicked(){
        EventBus.getDefault().post(new LoadActivityEvent(FoodCategoryActivity.class));
    }
    public void onCosmeticCategoryClicked(){
        EventBus.getDefault().post(new LoadActivityEvent(CosmeticCategoryActivity.class));
    }
    public void onMedicineCategoryClicked(){
        EventBus.getDefault().post(new LoadActivityEvent(MedicineCategoryActivity.class));
    }
    public void onAddProductClicked(){
        EventBus.getDefault().post(new ShowAddItemDialog(""));
    }
    //region getter setter
    public MutableLiveData<String> getFoodNumber() {
        return foodNumber;
    }

    public void setFoodNumber(MutableLiveData<String> foodNumber) {
        this.foodNumber = foodNumber;
    }

    public MutableLiveData<String> getCosmeticNumber() {
        return cosmeticNumber;
    }

    public void setCosmeticNumber(MutableLiveData<String> cosmeticNumber) {
        this.cosmeticNumber = cosmeticNumber;
    }

    public MutableLiveData<String> getMedicineNumber() {
        return medicineNumber;
    }

    public void setMedicineNumber(MutableLiveData<String> medicineNumber) {
        this.medicineNumber = medicineNumber;
    }

    public MutableLiveData<String> getFoodStatus() {
        return foodStatus;
    }

    public void setFoodStatus(MutableLiveData<String> foodStatus) {
        this.foodStatus = foodStatus;
    }

    public MutableLiveData<String> getCosmeticStatus() {
        return cosmeticStatus;
    }

    public void setCosmeticStatus(MutableLiveData<String> cosmeticStatus) {
        this.cosmeticStatus = cosmeticStatus;
    }

    public MutableLiveData<String> getMedicineStatus() {
        return medicineStatus;
    }

    public void setMedicineStatus(MutableLiveData<String> medicineStatus) {
        this.medicineStatus = medicineStatus;
    }
    //endregion
}