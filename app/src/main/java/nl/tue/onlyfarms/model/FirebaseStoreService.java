package nl.tue.onlyfarms.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Class used to retrieve, monitor, update, remove and add stores to the store-database
 * Uses a semi-SingleTon design pattern to ensure a single instance exists.
 *
 * To use call {@code getInstance()} to retrieve the instance of the class
 * */
public class FirebaseStoreService {
    private static FirebaseStoreService instance;
    private static FirebaseDatabase database = OurFirebaseDatabase.getInstance();

    /**
     * Constructs a new instance of this class, shouldn't be called outside this class.
     * @returns {@code this}
     * */
    private FirebaseStoreService() { }

    /**
     * retrieves the instance of the class, or creates one
     * @return {@code instance}
     * @post {/result == instance && instance != null}
     * */
    public static FirebaseStoreService getInstance() {
        if (instance == null) { instance = new FirebaseStoreService(); }
        return instance;
    }

    /**
     * retrieves a store-object from the database, and monitors it.
     * @param uid uid of the store to retrieve
     * @return {@code MutableLiveData<List<Store>>} if {@param uid} was found in the database, and monitors.
     * Returns {@code null} if {@param was} was not found in the database.
     * */
    public MutableLiveData<List<Store>> getStore(String uid) {
        return null;
    }

    /**
     * Sends the given store object to the database, overwriting one with the same key (uid) if any.
     * @param store store object to send to the database.
     * @post Firebase has child store with uid, etc.. matching the store-object
     * @throws IllegalStateException if a DatabaseException is received
     * @throws NullPointerException if {@param store == null}
     * */
    public void updateStore(Store store) {
        if (store == null) {
            throw new NullPointerException("argument 'store' is null!");
        }
        Task writeStore = database.getReference().child("stores").child(store.getUid()).setValue(store);

        writeStore.addOnFailureListener(e -> {
            throw new IllegalStateException(e.getMessage());
        });
    }
}
