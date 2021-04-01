package com.ecdue.lim.utils;

import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

// The instance of this class is stored in static variable so it remains active for the program
// lifetime, which means the database changes can be detected all the time
public class DatabaseHelper {
    public static final String TAG = DatabaseHelper.class.getSimpleName();
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

    private MutableLiveData<String> foodQuantity = null;
    private MutableLiveData<String> cosmeticQuantity = null;
    private MutableLiveData<String> medicineQuantity = null;

    private static int instances;
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
                            foodChanges.clear();
                            foodChanges.addAll(value.getDocumentChanges());
                            processChanges(foodChanges, foods, foodAdapter);

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
            foodQuantity.postValue(foodQuantity.getValue());
            processChanges(foodChanges, foods, foodAdapter);
        }
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

                            cosmeticChanges.clear();
                            cosmeticChanges.addAll(value.getDocumentChanges());
                            processChanges(cosmeticChanges, cosmetics, cosmeticAdapter);

                            cosmeticQuantity.setValue((value.getDocuments().size() > 1) ?
                                    value.getDocuments().size() + " products" :
                                    value.getDocuments().size() + " product");
                            cosmeticQuantity.postValue(cosmeticQuantity.getValue());
                        }
                    });
        }
        else {
            cosmeticQuantity.postValue(cosmeticQuantity.getValue());
            processChanges(cosmeticChanges, cosmetics, cosmeticAdapter);
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

                            medicineChanges.clear();
                            medicineChanges.addAll(value.getDocumentChanges());
                            processChanges(medicineChanges, medicines, medicineAdapter);

                            medicineQuantity.setValue((value.getDocuments().size() > 1) ?
                                    value.getDocuments().size() + " products" :
                                    value.getDocuments().size() + " product");
                            medicineQuantity.postValue(medicineQuantity.getValue());
                        }
                    });
        }
        else {
            medicineQuantity.postValue(medicineQuantity.getValue());
            processChanges(medicineChanges, medicines, medicineAdapter);
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
                    int removeIndex = Collections.binarySearch(foods, removedProduct, new Comparator<Product>() {
                        @Override
                        public int compare(Product o1, Product o2) {
                            return Long.compare(o1.getExpire(), o2.getExpire());
                        }
                    });
                    if (removeIndex < 0)
                        removeIndex = (removeIndex + 1)*-1;
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
    public ArrayList<Product> getCosmetics(CosmeticAdapter adapter){
        this.cosmeticAdapter = adapter;
        return cosmetics;
    }
    public ArrayList<Product> getMedicines(MedicineAdapter adapter){
        this.medicineAdapter = adapter;
        return medicines;
    }


    public void addNewProduct(Product product) throws IllegalAccessException {
//        db.collection("users")
//                .document(userUid)
//                .collection(product.getCategory())
//                .document(product.getName() + product.getExpire())
//                .set(product.toHashMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful())
//                    Log.d(TAG, "Write new product successfully");
//                else
//                    Log.d(TAG, "Fail to write new product");
//            }
//        });
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
    public void writeTest(){
//        HashMap<String, Object> testData = new HashMap<>();
//        testData.put("name","Pork");
//        testData.put("quantity", 1);
//        testData.put("measurement", "kg");
//        Log.d("Database", "Writing test data for user with uid " + userUid);
//        db.collection("users")
//                .document(userUid)
//                .collection("Foods")
//                .document("food" + String.valueOf((int)(Math.random()*1000)))
//                .set(testData).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful())
//                    Log.d("Database", "Write data successfully");
//                else
//                    Log.d("Database", "Fail to write data");
//            }
//        });
    }
    public void readTestData(){
//        db.collection("users")
//                .document(userUid)
//                .collection("Foods")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.getResult().isEmpty())
//                            Log.d(TAG, "Path does not exist");
//                        else
//                            Log.d(TAG, "Path exists");
//                    }
//                });
//        db.collection("users")
//                .document(userUid)
//                .collection("Foods")
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                        if (error != null || value == null){
//                            Log.d(TAG, "Fail to read from database");
//                            return;
//                        }
//                        String source = value.getMetadata().hasPendingWrites() ?
//                                "Local" : "Server";
//                        for (DocumentSnapshot documentSnapshot : value.getDocuments()){
//                            Log.d(TAG, "From " + source + " : " + documentSnapshot.getData());
//                        }
//                    }
//                });
    }
    //endregion
    public void clearData(){
        foods.clear();
        cosmetics.clear();
        medicines.clear();
        instance = null;
    }
}
