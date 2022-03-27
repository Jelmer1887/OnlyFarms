package nl.tue.onlyfarms.model;

import android.nfc.Tag;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class FireBaseService<T extends Model> {
    private final Class<T> aClass;
    private String TAG = "FireBaseService<";

    private final static FirebaseDatabase database = OurFirebaseDatabase.getInstance();
    private final DatabaseReference reference;
    private final String referenceString;


    public FireBaseService(Class<T> aClass, String ref){
        this.aClass = aClass;
        this.TAG = TAG + aClass.getName() + ">";

        Log.d(TAG, "service created looking at " + ref);
        this.referenceString = ref.toLowerCase(Locale.ROOT);
        this.reference = database.getReference(referenceString);
    }

    public MutableLiveData<Set<T>> getAllAtReference() {
        DataListener l = new DataListener(aClass);
        reference.orderByChild("uid").addChildEventListener(l);
        return l.getMultipleResults();
    }

    public MutableLiveData<Set<T>> getAllMatchingField(String field, String value){
        DataListener l = new DataListener(aClass);
        reference.orderByChild(field).equalTo(value).addChildEventListener(l);
        return l.getMultipleResults();
    }

    public MutableLiveData<T> getSingleMatchingField(String field, String value){
        DataListener l = new DataListener(aClass);
        reference.orderByChild(field).equalTo(value).addChildEventListener(l);
        return l.getSingleResult();
    }

    public Task<Void> updateToDatabase(T object, String uid) {
        Log.d(TAG, "uploading " + object + " data to " + referenceString + "/" + uid);
        return database.getReference()
                .child(referenceString).child(uid).setValue(object);
    }

    public Task<Void> deleteFromDatabase(String uid) {
        Log.d(TAG, "deleting " + referenceString + "/"+uid + " if found");
        return database.getReference().child(referenceString)
                .child(uid).removeValue();
    }


    private class DataListener implements ChildEventListener{
        private static final String TAG = "DataListener";
        private final MutableLiveData<T> result;
        private final MutableLiveData<Set<T>> results;
        private final Class<T> aClass;
        private final Set<T> multiple_found;

        public DataListener(Class<T> aClass) {
            this.multiple_found = new HashSet<>();
            this.result = new MutableLiveData<>();
            this.results = new MutableLiveData<>();
            this.aClass = aClass;
        }

        private void determineSingleResult() {
            Iterator<T> iter = multiple_found.iterator();
            if (iter.hasNext()) {
                result.postValue(iter.next());
                return;
            }
            result.postValue(null);
        }

        public MutableLiveData<Set<T>> getMultipleResults() {
            return results;
        }

        public MutableLiveData<T> getSingleResult() {
            return result;
        }

        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            Log.d(TAG, "addition change detected");
            T foundResult = snapshot.getValue(aClass);
            if (foundResult == null) {
                throw new IllegalStateException("this btch empty-yo!");
            }
            multiple_found.add(foundResult);
            results.postValue(multiple_found);
            determineSingleResult();
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            Log.d(TAG, "edit change detected");
            T foundResult = snapshot.getValue(aClass);
            if (foundResult == null) { return; }
            if (multiple_found.removeIf(object -> object.getUid().equals(foundResult.getUid()))) {
                multiple_found.add(foundResult);
                results.postValue(multiple_found);
                determineSingleResult();
            }
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            Log.d(TAG, "removal change detected");
            T removedResult = snapshot.getValue(aClass);
            if (removedResult == null){ return; }
            Log.d(TAG, "looking for object in lists...");
            if (multiple_found.removeIf(object -> object.getUid().equals(removedResult.getUid()))) {
                Log.d(TAG, "found!");
                results.postValue(multiple_found);
                determineSingleResult();
            } else {
                Log.d(TAG, "not found!");
            }
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    }
}
