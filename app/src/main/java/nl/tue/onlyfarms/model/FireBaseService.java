package nl.tue.onlyfarms.model;

import android.nfc.Tag;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

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
    private final Set<T> resultList;

    private final static FirebaseDatabase database = OurFirebaseDatabase.getInstance();
    private static DatabaseReference reference;
    private String referenceString;

    private final ChildEventListener listener;

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
        MutableLiveData<T> firstResult = new MutableLiveData<>();
        getAllMatchingField(field, value).observe(lifecycleOwner, r -> {
            Iterator<T> iterator = r.iterator();
            firstResult.postValue(iterator.next());
        });

        return firstResult;
    }
    public MutableLiveData<T> getFirstMatchingField(LifecycleOwner lifecycleOwner, String field, int value) {
        resultList.clear();
        results.postValue(resultList);
        MutableLiveData<T> firstResult = new MutableLiveData<>();
        getAllMatchingField(field, value).observe(lifecycleOwner, r -> {
            Iterator<T> iterator = r.iterator();
            firstResult.postValue(iterator.next());
        });

        return firstResult;
    }
    public MutableLiveData<T> getFirstMatchingField(LifecycleOwner lifecycleOwner, String field, float value) {
        resultList.clear();
        results.postValue(resultList);
        MutableLiveData<T> firstResult = new MutableLiveData<>();
        getAllMatchingField(field, value).observe(lifecycleOwner, r -> {
            Iterator<T> iterator = r.iterator();
            firstResult.postValue(iterator.next());
        });

        return firstResult;
    }
    public MutableLiveData<T> getFirstMatchingField(LifecycleOwner lifecycleOwner, String field, boolean value) {
        resultList.clear();
        results.postValue(resultList);
        MutableLiveData<T> firstResult = new MutableLiveData<>();
        getAllMatchingField(field, value).observe(lifecycleOwner, r -> {
            Iterator<T> iterator = r.iterator();
            firstResult.postValue(iterator.next());
        });

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

    protected void onResultFound(){ results.postValue(resultList); }
}
