package com.ecdue.lim.features.settings;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ecdue.lim.data.Product;
import com.ecdue.lim.events.BackButtonClickedEvent;
import com.ecdue.lim.events.ChangeCosmeticNotificationEvent;
import com.ecdue.lim.events.ChangeFoodNotificationEvent;
import com.ecdue.lim.events.ChangeMedicineNotificationEvent;
import com.ecdue.lim.events.ChangeNotificationTimeEvent;
import com.ecdue.lim.utils.DatabaseHelper;
import com.ecdue.lim.utils.DateTimeUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Locale;

public class SettingsViewModel extends ViewModel {
    public static final String TAG = SettingsViewModel.class.getSimpleName();
    private MutableLiveData<Boolean> notificationAllowed = new MutableLiveData<>(true);
    private MutableLiveData<String> foodNotification = new MutableLiveData<>("");
    private long foodExpThreshold = 0;
    private long cosmeticExpThreshold = 0;
    private long medicineExpThreshold = 0;
    private long notificationTime = 0;
    private MutableLiveData<String> cosmeticNotification = new MutableLiveData<>("");
    private MutableLiveData<String> medicineNotification = new MutableLiveData<>("");
    private MutableLiveData<String> notificationTimeString = new MutableLiveData<>("");
    public void onBackPressed(){
        EventBus.getDefault().post(new BackButtonClickedEvent(""));
    }


    public void initialize(){
        DatabaseHelper db = DatabaseHelper.getInstance();

        foodExpThreshold = db.getFoodExpThreshold();
        foodNotification.setValue("" + foodExpThreshold + ((foodExpThreshold > 1) ? " days" : " day"));

        cosmeticExpThreshold = db.getCosmeticExpThreshold();
        cosmeticNotification.setValue("" + cosmeticExpThreshold + ((cosmeticExpThreshold > 1) ? " days" : " day"));

        medicineExpThreshold = db.getMedicineExpThreshold();
        medicineNotification.setValue("" + medicineExpThreshold + ((medicineExpThreshold > 1) ? " days" : " day"));

        notificationTime = db.getNotificationTimeOfDay();
        long hour = notificationTime/(1000*60*60);
        long minute = (notificationTime%(1000*60*60))/(1000*60);
        notificationTimeString.setValue(String.format(Locale.US, "%02d:%02d", hour, minute));
    }

    public void onChangeFoodNotificationClicked(){
        EventBus.getDefault().post(new ChangeFoodNotificationEvent());
    }
    public void setNewFoodNotificationSetting(int day){
        DatabaseHelper dbHelper = DatabaseHelper.getInstance();
        Log.d(TAG, "User choose a new day: " + day);
        dbHelper.setFoodExpThreshold((long) day);
        foodExpThreshold = (long) day;
        foodNotification.setValue("" + foodExpThreshold + ((foodExpThreshold > 1) ? " days" : " day"));
        ArrayList<Product> products = dbHelper.getFoods();
        for (Product product : products){
            int daysLeft = DateTimeUtil.milliSecToDay(product.getExpire() - DateTimeUtil.getCurrentDayTime());
            product.setToBeNotified(daysLeft <= foodExpThreshold);
            dbHelper.removeProductNotification(product);
            dbHelper.createProductNotification(product);
        }

    }
    public void onChangeCosmeticNotificationClicked(){
        EventBus.getDefault().post(new ChangeCosmeticNotificationEvent());
    }
    public void setNewCosmeticNotificationSetting(int day){
        DatabaseHelper dbHelper = DatabaseHelper.getInstance();
        Log.d(TAG, "User choose a new day: " + day);
        dbHelper.setCosmeticExpThreshold((long) day);
        cosmeticExpThreshold = (long) day;
        cosmeticNotification.setValue("" + cosmeticExpThreshold + ((cosmeticExpThreshold > 1) ? " days" : " day"));
        ArrayList<Product> products = dbHelper.getCosmetics();
        for (Product product : products){
            int daysLeft = DateTimeUtil.milliSecToDay(product.getExpire() - DateTimeUtil.getCurrentDayTime());
            product.setToBeNotified(daysLeft <= cosmeticExpThreshold);
            dbHelper.removeProductNotification(product);
            dbHelper.createProductNotification(product);
        }
    }
    public void onChangeMedicineNotificationClicked(){
        EventBus.getDefault().post(new ChangeMedicineNotificationEvent());
    }
    public void setNewMedicineNotificationSetting(int day){
        DatabaseHelper dbHelper = DatabaseHelper.getInstance();
        Log.d(TAG, "User choose a new day: " + day);
        dbHelper.setMedicineExpThreshold((long) day);
        medicineExpThreshold = (long) day;
        medicineNotification.setValue("" + medicineExpThreshold + ((medicineExpThreshold > 1) ? " days" : " day"));
        ArrayList<Product> products = dbHelper.getMedicines();
        for (Product product : products){
            int daysLeft = DateTimeUtil.milliSecToDay(product.getExpire() - DateTimeUtil.getCurrentDayTime());
            product.setToBeNotified(daysLeft <= medicineExpThreshold);
            dbHelper.removeProductNotification(product);
            dbHelper.createProductNotification(product);
        }
    }

    public void onChangeNotificationTimeClicked(){
        EventBus.getDefault().post(new ChangeNotificationTimeEvent());
    }
    public void setNewNotificationTime(int hour, int minute){
        DatabaseHelper db = DatabaseHelper.getInstance();
        Log.d(TAG, "User choose new time: " + hour + ":" + minute);
        notificationTime = (long)(hour*60*60*1000 + minute*60*1000);
        notificationTimeString.setValue(String.format(Locale.US, "%02d:%02d", hour, minute));
        DatabaseHelper.getInstance().setNotificationTimeOfDay(notificationTime);
        ArrayList<Product> foods = db.getFoods();
        ArrayList<Product> cosmetics = db.getCosmetics();
        ArrayList<Product> medicines = db.getMedicines();

        for (Product food : foods){
            int daysLeft = DateTimeUtil.milliSecToDay(food.getExpire() - DateTimeUtil.getCurrentDayTime());
            if (daysLeft >= foodExpThreshold){
                db.removeProductNotification(food);
                db.createProductNotification(food);
            }
        }
        for (Product cosmetic : cosmetics){
            int daysLeft = DateTimeUtil.milliSecToDay(cosmetic.getExpire() - DateTimeUtil.getCurrentDayTime());
            if (daysLeft >= cosmeticExpThreshold){
                db.removeProductNotification(cosmetic);
                db.createProductNotification(cosmetic);
            }
        }
        for (Product medicine : medicines){
            int daysLeft = DateTimeUtil.milliSecToDay(medicine.getExpire() - DateTimeUtil.getCurrentDayTime());
            if (daysLeft >= medicineExpThreshold){
                db.removeProductNotification(medicine);
                db.createProductNotification(medicine);
            }
        }
    }
    public void onNotificationAllowed(){
        notificationAllowed.setValue(true);
        Log.d(TAG, "Notification is changed to on");
    }
    public void onNotificationDenied(){
        notificationAllowed.setValue(false);
        Log.d(TAG, "Notification is changed to off");
    }
    public MutableLiveData<Boolean> getNotificationAllowed() {
        return notificationAllowed;
    }

    public void setNotificationAllowed(MutableLiveData<Boolean> notificationAllowed) {
        this.notificationAllowed = notificationAllowed;
    }

    //region Logic
    public boolean validateNewNumberOfDays(String dayNum){
        try {
            Integer.parseInt(dayNum);
            return true;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }
    //endregion

    public MutableLiveData<String> getFoodNotification() {
        return foodNotification;
    }

    public void setFoodNotification(MutableLiveData<String> foodNotification) {
        this.foodNotification = foodNotification;
    }

    public long getFoodExpThreshold() {
        return foodExpThreshold;
    }

    public void setFoodExpThreshold(long foodExpThreshold) {
        this.foodExpThreshold = foodExpThreshold;
    }

    public long getCosmeticExpThreshold() {
        return cosmeticExpThreshold;
    }

    public void setCosmeticExpThreshold(long cosmeticExpThreshold) {
        this.cosmeticExpThreshold = cosmeticExpThreshold;
    }

    public long getMedicineExpThreshold() {
        return medicineExpThreshold;
    }

    public void setMedicineExpThreshold(long medicineExpThreshold) {
        this.medicineExpThreshold = medicineExpThreshold;
    }

    public MutableLiveData<String> getCosmeticNotification() {
        return cosmeticNotification;
    }

    public void setCosmeticNotification(MutableLiveData<String> cosmeticNotification) {
        this.cosmeticNotification = cosmeticNotification;
    }

    public MutableLiveData<String> getMedicineNotification() {
        return medicineNotification;
    }

    public void setMedicineNotification(MutableLiveData<String> medicineNotification) {
        this.medicineNotification = medicineNotification;
    }

    public MutableLiveData<String> getNotificationTimeString() {
        return notificationTimeString;
    }

    public void setNotificationTimeString(MutableLiveData<String> notificationTimeString) {
        this.notificationTimeString = notificationTimeString;
    }
}
