package nl.tue.onlyfarms.model;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Class used to retrieve, monitor, update, remove and add stores to the store-database
 * Uses a semi-SingleTon design pattern to ensure a single instance exists.
 *
 * To use call {@code getInstance()} to retrieve the instance of the class
 * */
public class FirebaseStoreService {
    /**
     * retrieves a store-object from the database, and monitors it.
     * @param uid uid of the store to retrieve
     * @return {@code MutableLiveData<List<Store>>} if {@param uid} was found in the database, and monitors.
     * Returns {@code null} if {@param was} was not found in the database.
     * */
    public static MutableLiveData<Store> getStore(String uid) {
        FirebaseDatabase database = OurFirebaseDatabase.getInstance();
        MutableLiveData<Store> store = new MutableLiveData<>();
        database.getReference("stores").equalTo(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                store.postValue(snapshot.getValue(Store.class));
                Log.i("storeService", "retrieved: " + store.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw new IllegalStateException("database error: "+ error.getMessage());
            }
        });

        return store;
    }

    /**
     * Sends the given store object to the database, overwriting one with the same key (uid) if any.
     * @param store store object to send to the database.
     * @post Firebase has child store with uid, etc.. matching the store-object
     * @throws IllegalStateException if a DatabaseException is received
     * @throws NullPointerException if {@param store == null}
     * */
    public static void updateStore(Store store) {
        FirebaseDatabase database = OurFirebaseDatabase.getInstance();
        if (store == null) {
            throw new NullPointerException("argument 'store' is null!");
        }
        Task writeStore = database.getReference().child("stores").child(store.getUid()).setValue(store);

        writeStore.addOnFailureListener(e -> {
            throw new IllegalStateException(e.getMessage());
        });
    }
}
