package nl.tue.onlyfarms.model;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used to retrieve, monitor, update, remove and add stores to the store-database
 * Uses a semi-SingleTon design pattern to ensure a single instance exists.
 *
 * To use call {@code getInstance()} to retrieve the instance of the class
 * */
public class FirebaseStoreService {
    public static final String TAG = "FireBaseStoreService";
    final static FirebaseDatabase database = OurFirebaseDatabase.getInstance();
    final static DatabaseReference storeRef = database.getReference("stores");

    /**
     * Returns a MutableLiveData object with the stores
     * */
    public static List<MutableLiveData<Store>> getStores(String uid) {
        List<MutableLiveData<Store>> result = new ArrayList<>();
        Log.i(TAG, "fetching store data associated with user: " + uid);
        storeRef.orderByChild("userUid").equalTo(uid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d(TAG, "onChildAdded: " + snapshot.getValue());
                Store receivedData = snapshot.getValue(Store.class);
                if (receivedData == null) {
                    Log.i(TAG, "  onChildAdded: received null data");
                }

                /* post found data to the list of stores, if the data is new */
                for (MutableLiveData<Store> data : result) {
                    if (data.getValue() == null || receivedData == null) { continue; }
                    if (data.getValue().getUid().equals(receivedData.getUid())) {
                        // the data already exists in the list. This could be because:
                        // - system accidentally posted the same store twice to the database.
                        // - the store re-entered the list of matching stores after having left it.
                        Log.i(TAG, "onChildAdded: data received already found in list");
                        return;
                    }
                }
                MutableLiveData<Store> storeData = new MutableLiveData<>();
                storeData.postValue(receivedData);
                result.add(storeData);
                Log.i(TAG, "onChildAdded: received data posted to store-list");
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return result;
    }

    /**
     * Yeets a store object to the database, overwriting the existing matching element if any.
     * @param store store object ot push to the databse
     * Returns true if succesfull, false otherwise
     */
    public static void updateStore(Store store) {
        Task<Void> t = database.getReference().child("stores").child(store.getUid()).setValue(store);

        t.addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.i(TAG, "store pushed to database");
                }
            }
        });

        t.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG,"failed to push store to database: ", e);
            }
        });
    }
}
