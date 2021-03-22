package com.ecdue.lim.utils;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;

import io.reactivex.rxjava3.core.Observer;

public class DatabaseHelper {
    public static final String TAG = DatabaseHelper.class.getSimpleName();
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String userUid;
    private static DatabaseHelper instance;
    public static DatabaseHelper getInstance(){
        if (instance == null)
            instance = new DatabaseHelper();
        return instance;
    }
    private DatabaseHelper(){
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        userUid = auth.getUid();
    }

    //region getter setter

    public FirebaseFirestore getDb() {
        return db;
    }

    //endregion

    //region public interfaces
    public void disableCloudSync(){
        db.disableNetwork();
    }
    public void enableCloudSync(){
        db.enableNetwork();
    }
    public void getFoodQuantity(MutableLiveData<String> foodNumber){
        db.collection("users")
                .document(userUid)
                .collection("foods")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null || value == null){
                            Log.d(TAG, "Fail to read from database");
                            return;
                        }
                        String source = value.getMetadata().hasPendingWrites() ?
                                "Local" : "Server";
                        for (DocumentSnapshot documentSnapshot : value.getDocuments()){
                            Log.d(TAG, "From " + source + " : " + documentSnapshot.getData());
                        }
                        foodNumber.postValue((value.getDocuments().size() > 1) ?
                                value.getDocuments().size() + " products" :
                                value.getDocuments().size() + " product");
                    }
                });
    }
    public void getCosmeticQuantity(MutableLiveData<String> cosmeticNumber){
        db.collection("users")
                .document(userUid)
                .collection("cosmetics")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null || value == null){
                            Log.d(TAG, "Fail to read from database");
                            return;
                        }
                        String source = value.getMetadata().hasPendingWrites() ?
                                "Local" : "Server";
                        for (DocumentSnapshot documentSnapshot : value.getDocuments()){
                            Log.d(TAG, "From " + source + " : " + documentSnapshot.getData());
                        }
                        cosmeticNumber.postValue((value.getDocuments().size() > 1) ?
                                value.getDocuments().size() + " products" :
                                value.getDocuments().size() + " product");
                    }
                });
    }
    public void getMedicineQuantity(MutableLiveData<String> medicineNumber){
        db.collection("users")
                .document(userUid)
                .collection("medicines")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null || value == null){
                            Log.d(TAG, "Fail to read from database");
                            return;
                        }
                        String source = value.getMetadata().hasPendingWrites() ?
                                "Local" : "Server";
                        for (DocumentSnapshot documentSnapshot : value.getDocuments()){
                            Log.d(TAG, "From " + source + " : " + documentSnapshot.getData());
                        }
                        medicineNumber.postValue((value.getDocuments().size() > 1) ?
                                value.getDocuments().size() + " products" :
                                value.getDocuments().size() + " product");
                    }
                });
    }

    public void writeTest(){
        HashMap<String, Object> testData = new HashMap<>();
        testData.put("name","Pork");
        testData.put("quantity", 1);
        testData.put("measurement", "kg");
        Log.d("Database", "Writing test data for user with uid " + userUid);
        db.collection("users")
                .document(userUid)
                .collection("foods")
                .document("food" + String.valueOf((int)(Math.random()*1000)))
                .set(testData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    Log.d("Database", "Write data successfully");
                else
                    Log.d("Database", "Fail to write data");
            }
        });
    }
    public void readTestData(){
        db.collection("users")
                .document(userUid)
                .collection("foods")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.getResult().isEmpty())
                            Log.d(TAG, "Path does not exist");
                        else
                            Log.d(TAG, "Path exists");
                    }
                });
        db.collection("users")
                .document(userUid)
                .collection("foods")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null || value == null){
                            Log.d(TAG, "Fail to read from database");
                            return;
                        }
                        String source = value.getMetadata().hasPendingWrites() ?
                                "Local" : "Server";
                        for (DocumentSnapshot documentSnapshot : value.getDocuments()){
                            Log.d(TAG, "From " + source + " : " + documentSnapshot.getData());
                        }
                    }
                });
    }
    //endregion

}
