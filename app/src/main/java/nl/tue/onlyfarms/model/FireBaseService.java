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
    private static final String TAG = "FireBaseService";
    private final MutableLiveData<Set<T>> results;
    private final MutableLiveData<T> firstResult = new MutableLiveData<>();
    private final Set<T> resultList;

    private final static FirebaseDatabase database = OurFirebaseDatabase.getInstance();
    private static DatabaseReference reference;
    private final String referenceString;

    private final ChildEventListener listener;

    private final Observer<Set<T>> getFirst = new androidx.lifecycle.Observer<Set<T>>() {
        @Override
        public void onChanged(Set<T> ts) {
            Iterator<T> iterator = ts.iterator();
            if (iterator.hasNext()) {
                firstResult.postValue(iterator.next());
            } else {
                firstResult.postValue(null);
            }
        }
    };

    public FireBaseService(Class<T> aClass, String ref){
        referenceString = ref.toLowerCase(Locale.ROOT);
        reference = database.getReference(referenceString);
        this.results = new MutableLiveData<>();
        this.resultList = new HashSet<>();


        listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                T foundResult = snapshot.getValue(aClass);
                if (foundResult == null) {
                    throw new IllegalStateException("returned result from database is null!");
                }
                resultList.add(foundResult);
                onResultFound();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                T foundResult = snapshot.getValue(aClass);
                if (foundResult == null) {
                    throw new IllegalStateException("returned result from database is null!");
                }
                if (resultList.removeIf(store -> store.equals(foundResult))) {
                    Log.d(TAG, "changed result found in resultList... updating...");
                    resultList.add(foundResult);
                    onResultFound();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                T removedResult = snapshot.getValue(aClass);
                if (removedResult == null) {
                    throw new IllegalStateException("removed result from database is null!");
                }
                if (resultList.removeIf(store -> store.equals(removedResult))) {
                    Log.d(TAG, "removed result found in resultList... updating...");
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

    public MutableLiveData<Set<T>> getAllAtReference() {
        resultList.clear();
        results.postValue(resultList);

        reference.orderByChild("uid").addChildEventListener(listener);

        return results;
    }

    public MutableLiveData<Set<T>> getAllMatchingField(String field, String value) {
        resultList.clear();
        results.postValue(resultList);

        reference.orderByChild(field).equalTo(value).addChildEventListener(listener);

        return results;
    }
    public MutableLiveData<Set<T>> getAllMatchingField(String field, int value) {
        resultList.clear();
        results.postValue(resultList);

        reference.orderByChild(field).equalTo(value).addChildEventListener(listener);

        return results;
    }
    public MutableLiveData<Set<T>> getAllMatchingField(String field, boolean value) {
        resultList.clear();
        results.postValue(resultList);

        reference.orderByChild(field).equalTo(value).addChildEventListener(listener);

        return results;
    }
    public MutableLiveData<Set<T>> getAllMatchingField(String field, float value) {
        resultList.clear();
        results.postValue(resultList);

        reference.orderByChild(field).equalTo(value).addChildEventListener(listener);

        return results;
    }

    public MutableLiveData<T> getFirstMatchingField(LifecycleOwner lifecycleOwner, String field, String value) {
        resultList.clear();
        results.postValue(resultList);
        getAllMatchingField(field, value).observe(lifecycleOwner, getFirst);

        return firstResult;
    }
    public MutableLiveData<T> getFirstMatchingField(LifecycleOwner lifecycleOwner, String field, int value) {
        resultList.clear();
        results.postValue(resultList);
        getAllMatchingField(field, value).observe(lifecycleOwner, getFirst);

        return firstResult;
    }
    public MutableLiveData<T> getFirstMatchingField(LifecycleOwner lifecycleOwner, String field, float value) {
        resultList.clear();
        results.postValue(resultList);
        getAllMatchingField(field, value).observe(lifecycleOwner, getFirst);

        return firstResult;
    }
    public MutableLiveData<T> getFirstMatchingField(LifecycleOwner lifecycleOwner, String field, boolean value) {
        resultList.clear();
        results.postValue(resultList);
        getAllMatchingField(field, value).observe(lifecycleOwner, getFirst);

        return firstResult;
    }

    public Task<Void> updateToDatabase(T object, String uid) {

        return database.getReference()
                .child(referenceString)
                .child(uid).setValue(object);
    }

    public Task<Void> deleteFromDatabase(String uid) {
        return database.getReference().child(referenceString)
                .child(uid).removeValue();
    }

    private void onResultFound() { results.postValue(resultList); }
}
