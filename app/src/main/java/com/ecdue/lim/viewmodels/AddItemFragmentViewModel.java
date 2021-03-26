package com.ecdue.lim.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ecdue.lim.data.Product;
import com.ecdue.lim.events.ShowDatePicker;
import com.ecdue.lim.utils.DatabaseHelper;
import com.google.firebase.Timestamp;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

// EventBus subscribers: MainActivity, FoodCategory/CosmeticCategory/MedicineCategory activities
public class AddItemFragmentViewModel extends ViewModel {

    public static final String TAG = AddItemFragmentViewModel.class.getSimpleName();
    private MutableLiveData<String> expirationDate = new MutableLiveData<>("");
    private MutableLiveData<String> daysLeft = new MutableLiveData<>("");
    private int notifyBoundary = 3; // Set by user
    private int productDayLefts = 0;
    public void addNewItem(
            String productName,
            String quantity,
            String unit,
            String category,
            String expDate,
            String Barcode
    ){
        Log.d(TAG, "Adding item");
        Product product = new Product();
        product.setName(productName);
        product.setQuantity(!quantity.equals("") ? Float.parseFloat(quantity) : 0f);
        product.setUnit(unit);
        product.setCategory(category);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy");
        try {
            Date expireDate = simpleDateFormat.parse(expDate);
            product.setExpire(expireDate.getTime());
        } catch (ParseException e) {
            Log.d(TAG, "Unexpected Error");
        }
        // Image url
        product.setBarcode(Barcode);
        Log.d(TAG, "Days left: " + productDayLefts);
        product.setToBeNotified(productDayLefts <= notifyBoundary);
        product.setExpire(productDayLefts <= 0);
        try {
            DatabaseHelper.getInstance().writeNewItem(product);
        } catch (IllegalAccessException e) {
            Log.d(TAG, "Write fail");
            e.printStackTrace();
        }

    }
    //region Fields validation
    public boolean productNameValidation(String name){
        return (!name.equals(""));
    }
    public boolean quantityValidation(String quantity) {
        if (!quantity.equals(""))
            try {
                Integer.parseInt(quantity);
                return true;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return false;
            }
        else
            return true;
    }
    //endregion

    public void onEdtDateClicked(){
        EventBus.getDefault().post(new ShowDatePicker(""));
    }
    public void onChoosePictureClicked(){

    }
    public void onTakePictureClicked(){

    }

    public MutableLiveData<String> getExpirationDate() {
        return expirationDate;
    }

    public MutableLiveData<String> getDaysLeft() {
        return daysLeft;
    }

    public void calculateDaysLeft(int cDay, int cMonth, int cYear, int eDay, int eMonth, int eYear) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy");
        // Confusion: month in Date start at 0 or 1 ?
        Date currentDate = simpleDateFormat.parse("" + cDay + "/" + (cMonth+1) + "/" + cYear);
        Date expireDate = simpleDateFormat.parse("" + eDay + "/" + (eMonth+1) + "/" + eYear);

        long different = expireDate.getTime() - currentDate.getTime();
        int days = (int) (different / 1000 / 60 / 60 / 24);
        productDayLefts = days;
        if (days < 0)
            daysLeft.postValue("Already expired");
        else if (days == 0)
            daysLeft.postValue("Expire today");
        else if (days == 1)
            daysLeft.postValue("Expire tomorrow");
        else
            daysLeft.postValue("Expire in " + days + " days");

    }
    public void calculateDaysLeft(int cDay, int cMonth, int cYear, String chosenDate) {
        // month in chosenDate is indexed at 1 so it must be - 1 to match the right month
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy");
            // month in parse() starts from 1 to 12, but the cMonth comes from Calendar starts from
            // 0 to 11 so I'll have to +1 to cMonth
            Date currentDate = simpleDateFormat.parse("" + cDay + "/" + (cMonth+1) + "/" + cYear);
            Date expireDate = simpleDateFormat.parse(chosenDate);
            long different = expireDate.getTime() - currentDate.getTime();
            int days = (int) (different / 1000 / 60 / 60 / 24);
            productDayLefts = days;
            if (days < 0)
                daysLeft.postValue("Already expired");
            else if (days == 0)
                daysLeft.postValue("Expire today");
            else if (days == 1)
                daysLeft.postValue("Expire tomorrow");
            else
                daysLeft.postValue("Expire in " + days + " days");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    public boolean dateValidation(String input){
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy");
            simpleDateFormat.parse(input);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}