package com.ecdue.lim.utils;

import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ecdue.lim.data.Product;
import com.ecdue.lim.features.cosmetics_storage.CosmeticAdapter;
import com.ecdue.lim.features.foods_storage.FoodAdapter;
import com.ecdue.lim.features.medicines_storage.MedicineAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

// The instance of this class is stored in static variable so it remains active for the program
// lifetime, which means the database changes can be detected all the time
public class DatabaseHelper {
    public static final String TAG = DatabaseHelper.class.getSimpleName();
    public static final String USER_INFO = "UserInfo";
    public static final String COSMETIC_THRESHOLD = "cosmetic_threshold";
    public static final String FOOD_THRESHOLD = "food_threshold";
    public static final String MEDICINE_THRESHOLD = "medicine_threshold";
    public static final String CATEGORY_FOOD = "Foods";
    public static final String CATEGORY_COSMETIC = "Cosmetics";
    public static final String CATEGORY_MEDICINE = "Medicines";
    public static long DEFAULT_FOOD_THRESHOLD = 2;
    public static long DEFAULT_COSMETIC_THRESHOLD = 14;
    public static long DEFAULT_MEDICINE_THRESHOLD = 14;

    private FirebaseFirestore db;
    private StorageReference storageReference;
    private FirebaseAuth auth;
    private String userUid;
    private static DatabaseHelper instance;

    private ArrayList<DocumentChange> foodsInfoChanges;
    private ArrayList<Product> foods;
    private FoodAdapter foodAdapter;

    private ArrayList<DocumentChange> cosmeticsInfoChanges;
    private ArrayList<Product> cosmetics;
    private CosmeticAdapter cosmeticAdapter;

    private ArrayList<DocumentChange> medicinesInfoChanges;
    private ArrayList<Product> medicines;
    private MedicineAdapter medicineAdapter;


    private ArrayList<DocumentChange> foodChanges = new ArrayList<>();
    private ArrayList<DocumentChange> cosmeticChanges = new ArrayList<>();
    private ArrayList<DocumentChange> medicineChanges = new ArrayList<>();

    private boolean hasFoodListener = false;
    private boolean hasCosmeticListener = false;
    private boolean hasMedicineListener = false;
    private long foodExpThreshold = DEFAULT_FOOD_THRESHOLD;
    private long cosmeticExpThreshold = DEFAULT_COSMETIC_THRESHOLD;
    private long medicineExpThreshold = DEFAULT_MEDICINE_THRESHOLD;

    private MutableLiveData<String> foodQuantity = null;
    private MutableLiveData<String> cosmeticQuantity = null;
    private MutableLiveData<String> medicineQuantity = null;

    private static int instances;
    private MutableLiveData<String> foodStatus;
    private MutableLiveData<String> cosmeticStatus;
    private MutableLiveData<String> medicineStatus;

    public static DatabaseHelper getInstance(){
        if (instance == null) {
            instance = new DatabaseHelper();
            instances++;
        }
        Log.d(TAG, "Instance created so far: " + instances);
        return instance;
    }
    private DatabaseHelper(){
        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        userUid = auth.getCurrentUser().getUid();

        foods = new ArrayList<>();
        foodsInfoChanges = new ArrayList<>();

        cosmeticsInfoChanges = new ArrayList<>();
        cosmetics = new ArrayList<>();

        medicinesInfoChanges = new ArrayList<>();
        medicines = new ArrayList<>();

    }

    //region getter setter
    public long getExpThreshold(String category){
        switch (category){
            case CATEGORY_FOOD:
                return foodExpThreshold;
            case CATEGORY_COSMETIC:
                return cosmeticExpThreshold;
            case CATEGORY_MEDICINE:
                return medicineExpThreshold;
            default:
                return 0;
        }
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
        if (!hasFoodListener) {
            hasFoodListener = true;
            this.foodQuantity = foodNumber;
            db.collection("users")
                    .document(userUid)
                    .collection("Foods")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                            if (error != null || value == null) {
                                Log.d(TAG, "Fail to read from database");
                                return;
                            }
                            //ArrayList<DocumentChange> documentChanges = new ArrayList<>(value.getDocumentChanges());
                            foodChanges.addAll(value.getDocumentChanges());
                            processChanges(foodChanges, foods, foodAdapter);
                            processProductExp(foods, foodExpThreshold, foodStatus);
                            foodChanges.clear();

                            foodQuantity.setValue((value.getDocuments().size() > 1) ?
                                    value.getDocuments().size() + " products" :
                                    value.getDocuments().size() + " product");
                            foodQuantity.postValue(foodQuantity.getValue());
                            Log.d(TAG, "Value of foodQuantity: " + foodQuantity.getValue());
                            //region Debug
                            String source = value.getMetadata().isFromCache() ?
                                    "Local" : "Server";
                            for (DocumentChange documentChange : foodChanges) {
                                Log.d(TAG, "Latest change from " + source + " : " + documentChange.getDocument().getData());
                            }
                            //endregion


                        }
                    });
        }
        else {
            foodQuantity = switchAndPostLiveData(foodNumber, foodQuantity);
            processChanges(foodChanges, foods, foodAdapter);
            processProductExp(foods, foodExpThreshold, foodStatus);
        }
    }

    private MutableLiveData<String> switchAndPostLiveData(MutableLiveData<String> newLiveData, MutableLiveData<String> liveData) {
        newLiveData.setValue(liveData.getValue());
        liveData = newLiveData;
        liveData.postValue(liveData.getValue());
        return liveData;
    }

    public void getCosmeticQuantity(MutableLiveData<String> cosmeticNumber){
        if (!hasCosmeticListener) {
            hasCosmeticListener = true;
            this.cosmeticQuantity = cosmeticNumber;
            db.collection("users")
                    .document(userUid)
                    .collection("Cosmetics")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null || value == null) {
                                Log.d(TAG, "Fail to read from database");
                                return;
                            }


                            cosmeticChanges.addAll(value.getDocumentChanges());
                            processChanges(cosmeticChanges, cosmetics, cosmeticAdapter);
                            processProductExp(cosmetics, cosmeticExpThreshold, cosmeticStatus);
                            cosmeticChanges.clear();

                            cosmeticQuantity.setValue((value.getDocuments().size() > 1) ?
                                    value.getDocuments().size() + " products" :
                                    value.getDocuments().size() + " product");
                            cosmeticQuantity.postValue(cosmeticQuantity.getValue());
                        }
                    });
        }
        else {
            cosmeticQuantity = switchAndPostLiveData(cosmeticNumber, cosmeticQuantity);
            processChanges(cosmeticChanges, cosmetics, cosmeticAdapter);
            processProductExp(cosmetics, cosmeticExpThreshold, cosmeticStatus);
        }
    }

    public void getMedicineQuantity(MutableLiveData<String> medicineNumber){
        if (!hasMedicineListener) {
            hasMedicineListener = true;
            this.medicineQuantity = medicineNumber;
            db.collection("users")
                    .document(userUid)
                    .collection("Medicines")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null || value == null) {
                                Log.d(TAG, "Fail to read from database");
                                return;
                            }


                            medicineChanges.addAll(value.getDocumentChanges());
                            processChanges(medicineChanges, medicines, medicineAdapter);
                            processProductExp(medicines, medicineExpThreshold, medicineStatus);
                            medicineChanges.clear();

                            medicineQuantity.setValue((value.getDocuments().size() > 1) ?
                                    value.getDocuments().size() + " products" :
                                    value.getDocuments().size() + " product");
                            medicineQuantity.postValue(medicineQuantity.getValue());
                        }
                    });
        }
        else {
            medicineQuantity = switchAndPostLiveData(medicineNumber, medicineQuantity);
            processChanges(medicineChanges, medicines, medicineAdapter);
            processProductExp(medicines, medicineExpThreshold, medicineStatus);
        }
    }

    private void processChanges(ArrayList<DocumentChange> documentChanges, ArrayList<Product> foods, RecyclerView.Adapter foodAdapter){
        for (DocumentChange documentChange : documentChanges){
            //products.add(Product.mapToProduct(documentSnapshot.getData()));
            switch (documentChange.getType()){
                case MODIFIED:
                    break;
                case ADDED:
                    Product newProduct = Product.mapToProduct(documentChange.getDocument().getData());
                    newProduct.setId(documentChange.getDocument().getId());
                    // Find the appropriate position to insert
                    int insertIndex = Collections.binarySearch(foods, newProduct, new Comparator<Product>() {
                        @Override
                        public int compare(Product o1, Product o2) {
                            return Long.compare(o1.getExpire(), o2.getExpire());
                        }
                    });
                    // Collections.binarySearch return (-(insert position)-1) when the item not found
                    if (insertIndex < 0)
                        insertIndex = (insertIndex + 1)*-1;
                    foods.add(insertIndex, newProduct);

                    if (foodAdapter != null) foodAdapter.notifyItemInserted(insertIndex);
                    break;

                case REMOVED:
                    Product removedProduct = Product.mapToProduct(documentChange.getDocument().getData());
                    int removeIndex = -1;
                    for (int i = 0; i < foods.size(); i++){
                        if (foods.get(i).getId().equals(documentChange.getDocument().getId())) {
                            removeIndex = i;
                            break;
                        }
                    }
                    if (removeIndex < 0)
                        break;
                    foods.remove(removeIndex);
                    if (foodAdapter != null) foodAdapter.notifyItemRemoved(removeIndex);
                    break;
            }
        }
    }

    // Receive the Adapter to notify when there's any change
    public ArrayList<Product> getFoods(FoodAdapter adapter){
        this.foodAdapter = adapter;
        return foods;
    }
    public void getFoodStorageStatus(MutableLiveData<String> foodStatusLive){
        if (this.foodStatus == null) {
            this.foodStatus = foodStatusLive;
            db.collection("users")
                    .document(userUid)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        if (task.getResult().get(FOOD_THRESHOLD) != null) {
                            foodExpThreshold = (long) task.getResult().get(FOOD_THRESHOLD);
                        } else {
                            HashMap<String, Object> temp = new HashMap<>();
                            temp.put(FOOD_THRESHOLD, DEFAULT_FOOD_THRESHOLD);
                            db.collection("users")
                                    .document(userUid)
                                    .set(temp, SetOptions.merge());
                        }
                        processProductExp(foods, foodExpThreshold, foodStatus);
                    }
                }
            });
        }
        else {
            foodStatus = switchAndPostLiveData(foodStatusLive, foodStatus);
        }
    }
    public void getCosmeticStorageStatus(MutableLiveData<String> cosmeticStatusLive){
        if (this.cosmeticStatus == null) {
            this.cosmeticStatus = cosmeticStatusLive;
            db.collection("users")
                    .document(userUid)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        if (task.getResult().get(COSMETIC_THRESHOLD) != null) {
                            cosmeticExpThreshold = (long) task.getResult().get(COSMETIC_THRESHOLD);
                        } else {
                            HashMap<String, Object> temp = new HashMap<>();
                            temp.put(COSMETIC_THRESHOLD, DEFAULT_COSMETIC_THRESHOLD);
                            db.collection("users")
                                    .document(userUid)
                                    .set(temp, SetOptions.merge());
                        }
                        processProductExp(cosmetics, cosmeticExpThreshold, cosmeticStatus);
                    }
                }
            });
        }
        else {
            cosmeticStatus = switchAndPostLiveData(cosmeticStatusLive, cosmeticStatus);
        }
    }

    public void getMedicineStorageStatus(MutableLiveData<String> medicineStatusLive){
        if (this.medicineStatus == null) {
            this.medicineStatus = medicineStatusLive;
            db.collection("users")
                    .document(userUid)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        if (task.getResult().get(MEDICINE_THRESHOLD) != null) {
                            medicineExpThreshold = (long) task.getResult().get(MEDICINE_THRESHOLD);
                        } else {
                            HashMap<String, Object> temp = new HashMap<>();
                            temp.put(MEDICINE_THRESHOLD, DEFAULT_MEDICINE_THRESHOLD);
                            db.collection("users")
                                    .document(userUid)
                                    .set(temp, SetOptions.merge());
                        }
                        processProductExp(medicines, medicineExpThreshold, medicineStatus);
                    }
                }
            });
        }
        else {
            medicineStatus = switchAndPostLiveData(medicineStatusLive, medicineStatus);
        }
    }
    private void processProductExp(ArrayList<Product> products, long expThreshold, MutableLiveData<String> liveData) {
        int nearExpProducts = 0;
        int expiredProducts = 0;
        for (Product product : products){
            long timeLeft = product.getExpire() - DateTimeUtil.getCurrentDayTime();
            if (timeLeft < 0){
                expiredProducts++;
                product.setExpired(true);
            }
            else if (timeLeft <= DateTimeUtil.dayToMilliSec(expThreshold)) {
                nearExpProducts++;
                product.setToBeNotified(true);
            }
        }
        switch (nearExpProducts){
            case 0:
                liveData.setValue("All your products are good");
                break;
            case 1:
                liveData.setValue("1 product is about to expire");
                break;
            default:
                liveData.setValue("" + nearExpProducts + " products are about to expire");
                break;
        }
        switch (expiredProducts){
            case 0:
                break;
            case 1:
                liveData.setValue("1 product is expired");
                break;
            default:
                liveData.setValue("" + expiredProducts + " products are expired");
                break;
        }
        liveData.postValue(liveData.getValue());
    }


    public ArrayList<Product> getCosmetics(CosmeticAdapter adapter){
        this.cosmeticAdapter = adapter;
        return cosmetics;
    }
    public ArrayList<Product> getMedicines(MedicineAdapter adapter){
        this.medicineAdapter = adapter;
        return medicines;
    }

    public ArrayList<Product> getFoods() {
        return foods;
    }

    public ArrayList<Product> getCosmetics() {
        return cosmetics;
    }

    public ArrayList<Product> getMedicines() {
        return medicines;
    }

    public void addNewProduct(Product product) throws IllegalAccessException {
        db.collection("users")
                .document(userUid)
                .collection(product.getCategory())
                .document()
                .set(product.toHashMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    Log.d(TAG, "Write new product successfully");
                else
                    Log.d(TAG, "Fail to write new product");
            }
        });
    }
    public void deleteProduct(String category, Product product){
        db.collection("users")
                .document(userUid)
                .collection(category)
                .document(product.getId())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                            Log.d(TAG, "Delete product successfully");
                        else
                            Log.d(TAG, "Fail to delete product");
                    }
                });
    }
    public String uploadImage(Uri image){
        String path = userUid + "/" + Timestamp.now().getSeconds();
        storageReference.child(path).putFile(image).addOnCompleteListener(
                new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful())
                            Log.d(TAG, "Upload image successfully, url: " +
                                    task.getResult().getMetadata().getPath());
                        else
                            Log.d(TAG, "Fail to upload image");
                    }
                }
        );
        return path;
    }
    public void downloadImage(String path, ImageView imageView){
        storageReference.child(path).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Glide.with(imageView)
                            .load(task.getResult())
                            .into(imageView);
                }
            }
        });
    }
    //endregion
    public void clearData(){
        foods.clear();
        cosmetics.clear();
        medicines.clear();
        instance = null;
    }

    public String getUserName() {
        return auth.getCurrentUser().isAnonymous() ? "Anonymous" : auth.getCurrentUser().getDisplayName();
    }
    public Uri getUserPicture(){
        return auth.getCurrentUser().getPhotoUrl();
    }
}
