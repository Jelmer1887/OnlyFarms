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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class used to retrieve, monitor, update, remove and add stores to the store-database
 * Uses a semi-SingleTon design pattern to ensure a single instance exists.
 *
 * To use call {@code getInstance()} to retrieve the instance of the class
 * */
public class FirebaseStoreService {
    private static final String TAG = "FireBaseStoreService";
    private static MutableLiveData<Set<Store>> stores;
    private static Set<Store> storeList;
    private static String currentUid;

    private final static FirebaseDatabase database = OurFirebaseDatabase.getInstance();
    private final static DatabaseReference storeRef = database.getReference("stores");

    private static ChildEventListener listener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            Store foundStore = snapshot.getValue(Store.class);
            if (foundStore == null) {
                Log.e(TAG, "received null value for store from database!");
                throw new IllegalStateException("received null from database");
            }
            Log.d(TAG, "store received from database: " + foundStore.getName());
            storeList.add(foundStore);
            onStoreFound();
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            Store changedStore = snapshot.getValue(Store.class);
            if (changedStore == null) {
                Log.e(TAG, "received null value for store from database!");
                throw new IllegalStateException("received null from database");
            }
            Log.d(TAG, "store changed from database: " + changedStore.getName());
            if (storeList.removeIf(store -> store.getUid().equals(changedStore.getUid()))) {
                Log.d(TAG, "changed store found in storeList... updating...");
                storeList.add(changedStore);
                stores.postValue(storeList);
            }
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
    };

    public static MutableLiveData<Set<Store>> getStores() {
        if (stores == null) {
            Log.d(TAG, "Store list is null, creating new store list...");
            stores = new MutableLiveData<>();
        }
        Log.d(TAG, "store list present... getting stores");

        storeList = new HashSet<>();  // clear list && make sure its not null
        stores.postValue(storeList);  // pushing cleared data to prevent data old data from
                                        // showing up while waiting for new data.

        /* Request data from database */
        storeRef.orderByChild("uid").addChildEventListener(listener);

        return stores;
    }

    /**
     * Returns a MutableLiveData object with the stores
     * May trigger observer in several ways:
     * - object get re-created upon calling this method.
     * - become null: something went wrong (note, value may still have been null before)
     * - contain list of nulls or empty list: no stores found or no result is received yet.
     * - contain values: finished query
     * */
    public static  MutableLiveData<Set<Store>> getStores(String uid) {
        if (stores == null) {
            Log.d(TAG, "Store list is null, creating new store list...");
            stores = new MutableLiveData<>();
        }
        Log.d(TAG, "store list present... matching uid's...");
        if (currentUid == null) {
            Log.d(TAG, "no previous user found...");
            currentUid = uid;
        } else if (currentUid.equals(uid)) {
            Log.d(TAG, "same user requested... returning same object...");
            return stores;
        } else {
            Log.d(TAG, "different user requested...");
        }

        // reached => new user logged in && stores != null

        storeList = new HashSet<>();  // clear list && make sure its not null
        stores.postValue(storeList);  // pushing cleared data to prevent data old data from
                                        // showing up while waiting for new data.

        /* Request data from database */
        storeRef.orderByChild("userUid").equalTo(currentUid).addChildEventListener(listener);


        return stores;
    }

    // internal method responsible for update the MutableLiveData with new values
    private static void onStoreFound() {
        stores.postValue(storeList);
    }

    // internal method responsible for changing the list of stores when some value of it changes.
    private static void onStoreChanged() {
        return;
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
