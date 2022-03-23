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
import java.util.Locale;
import java.util.Set;

public class FireBaseService<T> {
    private T t;
    private String TAG = "FireBaseService<";
    private final MutableLiveData<Set<T>> results;
    private final MutableLiveData<T> firstResult;
    private final Set<T> resultList;

    private final static FirebaseDatabase database = OurFirebaseDatabase.getInstance();
    private final DatabaseReference reference;
    private final String referenceString;

    private final ChildEventListener listener;

    private final Observer<Set<T>> getFirst = new androidx.lifecycle.Observer<Set<T>>() {
        @Override
        public void onChanged(Set<T> ts) {
            Log.d(TAG, "getFirst triggered");
            Iterator<T> iterator = ts.iterator();
            if (iterator.hasNext()) {
                T i = iterator.next();
                Log.d(TAG, "getFirst: found first value as: " + i);
                firstResult.postValue(i);

            } else {
                Log.d(TAG, "getFirst: no values present in list!");
                firstResult.postValue(null);
            }
        }
    };

    public FireBaseService(Class<T> aClass, String ref){
        TAG = TAG + aClass.getName() + ">";
        Log.d(TAG, "creating reference to " + ref);
        referenceString = ref.toLowerCase(Locale.ROOT);
        reference = database.getReference(referenceString);

        this.results = new MutableLiveData<>();
        this.resultList = new HashSet<>();
        this.firstResult = new MutableLiveData<>();

        Log.d(TAG, "created results object (" + this.results + "), resultList object ("
        + this.resultList + ") and firstResult object (" + this.firstResult + ")");

        Log.d(TAG, "creating listener...");
        listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d(TAG, "listener triggered: added");
                T foundResult = snapshot.getValue(aClass);
                if (foundResult == null) {
                    throw new IllegalStateException("returned result from database is null!");
                }
                Log.d(TAG, "listener: adding " + foundResult + " to list...");
                resultList.add(foundResult);
                onResultFound();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d(TAG, "listener triggered: changed");
                T foundResult = snapshot.getValue(aClass);
                if (foundResult == null) {
                    throw new IllegalStateException("returned result from database is null!");
                }
                Log.d(TAG, "listener: looking for " + foundResult + " in list to remove...");
                if (resultList.removeIf(store -> store.equals(foundResult))) {
                    Log.d(TAG, "listener: changed result found in resultList... updating...");
                    resultList.add(foundResult);
                    onResultFound();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "listener triggered: removed");
                T removedResult = snapshot.getValue(aClass);
                if (removedResult == null) {
                    throw new IllegalStateException("removed result from database is null!");
                }
                Log.d(TAG, "listener: looking for " + removedResult + " in list to remove...");
                if (resultList.removeIf(store -> store.equals(removedResult))) {
                    Log.d(TAG, "listener: removed result found in resultList... updating...");
                    onResultFound();
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
    }

    public MutableLiveData<T> getFirstResult() { return firstResult; }

    public MutableLiveData<Set<T>> getResults() {return results; }

    public void getAllAtReference() {
        Log.d(TAG, "adding listener to children at reference: " + referenceString);
        resultList.clear();
        results.postValue(resultList);

        reference.orderByChild("uid").addChildEventListener(listener);
    }

    public void getAllMatchingField(String field, String value) {
        Log.d(TAG, "adding listener to children at reference " +
                referenceString + "/" + field + " equal to " + value);
        resultList.clear();
        results.postValue(resultList);

        reference.orderByChild(field).equalTo(value).addChildEventListener(listener);
    }
    public void getAllMatchingField(String field, int value) {
        Log.d(TAG, "adding listener to children at reference " +
                referenceString + "/" + field + " equal to " + value);
        resultList.clear();
        results.postValue(resultList);

        reference.orderByChild(field).equalTo(value).addChildEventListener(listener);
    }
    public void getAllMatchingField(String field, boolean value) {
        Log.d(TAG, "adding listener to children at reference " +
                referenceString + "/" + field + " equal to " + value);
        resultList.clear();
        results.postValue(resultList);

        reference.orderByChild(field).equalTo(value).addChildEventListener(listener);
    }
    public void getAllMatchingField(String field, float value) {
        Log.d(TAG, "adding listener to children at reference " +
                referenceString + "/" + field + " equal to " + value);
        resultList.clear();
        results.postValue(resultList);

        reference.orderByChild(field).equalTo(value).addChildEventListener(listener);
    }

    public void getFirstMatchingField(String field, String value) {
        Log.d(TAG, "adding listener 'getFirst' to children at reference " +
                referenceString + "/" + field + " equal to " + value);
        resultList.clear();
        results.postValue(resultList);
        getAllMatchingField(field, value);
        results.observeForever(getFirst);
    }
    public void getFirstMatchingField(LifecycleOwner lifecycleOwner, String field, int value) {
        Log.d(TAG, "adding listener 'getFirst' to children at reference " +
                referenceString + "/" + field + " equal to " + value);
        resultList.clear();
        results.postValue(resultList);
        getAllMatchingField(field, value);
        results.observeForever(getFirst);
    }
    public void getFirstMatchingField(LifecycleOwner lifecycleOwner, String field, float value) {
        Log.d(TAG, "adding listener 'getFirst' to children at reference " +
                referenceString + "/" + field + " equal to " + value);
        resultList.clear();
        results.postValue(resultList);
        getAllMatchingField(field, value);
        results.observeForever(getFirst);
    }
    public void getFirstMatchingField(LifecycleOwner lifecycleOwner, String field, boolean value) {
        Log.d(TAG, "adding listener 'getFirst' to children at reference " +
                referenceString + "/" + field + " equal to " + value);
        resultList.clear();
        results.postValue(resultList);
        getAllMatchingField(field, value);
        results.observeForever(getFirst);
    }

    public Task<Void> updateToDatabase(T object, String uid) {
        Log.d(TAG, "uploading " + object + " data to " + referenceString + "/" + uid);
        return database.getReference()
                .child(referenceString)
                .child(uid).setValue(object);
    }

    public Task<Void> deleteFromDatabase(String uid) {
        Log.d(TAG, "deleting " + referenceString + "/"+uid + " if found");
        return database.getReference().child(referenceString)
                .child(uid).removeValue();
    }

    private void onResultFound() { results.postValue(resultList); }
}
