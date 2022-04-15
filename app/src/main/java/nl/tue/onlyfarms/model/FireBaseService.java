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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
/*
* This Generic class is used to facilitate communication with the database.
* It aims to:
* - Retrieve data from the database, using important fields
* - Upload and Update data to the database
* - Ensure received data remains up to date, through observers and listeners
*
* The class uses generic types to prevent duplication.
* For every kind of uploadable Model, a user would create a service.
* */
public class FireBaseService<T extends Model> {
    private String TAG = "FireBaseService<";

    // class of the type of Model to store in the database, determines as what kind of
    // object data is retrieved and uploaded.
    private final Class<T> aClass;

    // The database connection is handles through OurFireBaseDatabase
    private final static FirebaseDatabase database = OurFirebaseDatabase.getInstance();
    private final DatabaseReference reference;
    private final String referenceString;

    /*
    * Constructor for a new service,
    * @param aClass defines what kind of object is used
    * @param ref defines where data should be retrieved from, uploaded to and observed.
    * */
    public FireBaseService(Class<T> aClass, String ref){
        this.aClass = aClass;
        this.TAG = TAG + aClass.getName() + ">";
        this.referenceString = ref.toLowerCase(Locale.ROOT);
        this.reference = database.getReference(referenceString);

        Log.d(TAG, "service created looking at " + ref);
    }

    // Returns a MutableLiveData that contains a Set of the objects at the Reference
    // will NEVER have duplicates (it's a set)
    // the Set is updated with new changes as data in the database changes
    public MutableLiveData<Set<T>> getAllAtReference() {
        ExistanceListener e = new ExistanceListener();
        DataListener l = new DataListener(aClass,e.getExistance().getValue());
        Query query = reference.orderByChild("uid");
        query.addValueEventListener(e);
        query.addChildEventListener(l);
        return l.getMultipleResults();
    }

    // Returns a MutableLiveData that contains a Set of the objects at the Reference,
    // that contain the value specified at the specified field
    // will NEVER have duplicates (it's a set)
    // the Set is updated with new changes as data in the database changes
    public MutableLiveData<Set<T>> getAllMatchingField(String field, String value){
        ExistanceListener e = new ExistanceListener();
        DataListener l = new DataListener(aClass, e.getExistance().getValue());
        Log.e(TAG, "listening to " + field + " = " + value);
        Query query = reference.orderByChild(field).equalTo(value);
        query.addValueEventListener(e);
        query.addChildEventListener(l);
        return l.getMultipleResults();
    }

    // Returns a MutableLiveData that contains the first found object at the Reference,
    // that contain the value specified at the specified field
    // not guaranteed to return the same object, if multiple objects match the query.
    // the Object is updated with new changes as data in the database changes
    public MutableLiveData<T> getSingleMatchingField(String field, String value){
        ExistanceListener e = new ExistanceListener();
        DataListener l = new DataListener(aClass, e.getExistance().getValue());
        Query query = reference.orderByChild(field).equalTo(value);
        query.addValueEventListener(e);
        query.addChildEventListener(l);
        return l.getSingleResult();
    }

    // Store a object in the database
    // the object has to be of the type that is specified for this service.
    public Task<Void> updateToDatabase(T object, String uid) {
        Log.d(TAG, "uploading " + object + " data to " + referenceString + "/" + uid);
        return database.getReference()
                .child(referenceString).child(uid).setValue(object);
    }

    /*
    * Delete an object in the database matching the specified uid,
    * does nothing if no object is found
    * returns a Task, so the user may listen-in on the deletion process, and respond by adding
    * listeners.
    * */
    public Task<Void> deleteFromDatabase(String uid) {
        Log.d(TAG, "deleting " + referenceString + "/"+uid + " if found");
        return database.getReference().child(referenceString)
                .child(uid).removeValue();
    }

    /*
    * A custom ValueEventListener to match our app
    * an existence listener is used to detect whether or not a query is not returned,
    * this happens when no match is found.
    */
    private class ExistanceListener implements ValueEventListener {
        // result boolean
        private MutableLiveData<Boolean> existance = new MutableLiveData<>(false);

        public ExistanceListener() {}

        // getter
        public MutableLiveData<Boolean> getExistance() {
            return existance;
        }

        @Override
        // onDataChange of a ValueEventListener is guaranteed to return, and used to detect
        // the existence of valid data.
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            existance.postValue(snapshot.exists());
        }

        @Override
        // Throw an error if the request failed
        public void onCancelled(@NonNull DatabaseError error) {
            throw error.toException();
        }
    }

    /*
    * Custom childEventListener class,
    * responsible for keeping returned data in the app up-to-date
    * */
    private class DataListener implements ChildEventListener{
        private static final String TAG = "DataListener";
        private final MutableLiveData<T> result;
        private final MutableLiveData<Set<T>> results;
        private final Class<T> aClass;
        private final Set<T> multiple_found;

        // Constructor used to create a new listener
        public DataListener(Class<T> aClass, Boolean existence) {
            this.multiple_found = new HashSet<>();
            this.result = new MutableLiveData<>();
            this.results = new MutableLiveData<>();
            this.aClass = aClass;

            /*
            * when a DataListener is added to a query that is empty (e.g. no valid result found),
            * the onChildXXXXXXX methods may not be called at all, and thus single result
            * may never be initialised properly.
            *
            * Thus, to check for existance, and ExistanceListener should first be added to the
            * same query, and it's result should be passed to this listener,
            * so the single result variable can be set correctly.
            * */

            if (!existence) {
                this.results.postValue(multiple_found);
                determineSingleResult();
            }
        }

        // updates the single result field
        // (to null if none are found)
        private void determineSingleResult() {
            Iterator<T> iter = multiple_found.iterator();
            if (iter.hasNext()) {
                result.postValue(iter.next());
                return;
            }
            result.postValue(null);
        }

        // -- getters --

        public MutableLiveData<Set<T>> getMultipleResults() {
            return results;
        }

        public MutableLiveData<T> getSingleResult() {
            return result;
        }

        @Override
        // This method is called for every child found in the query, and for every child that is
        // later added to the query.
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            Log.d(TAG, "addition change detected");

            // retrieve (newly added) values at the query
            T foundResult = snapshot.getValue(aClass);
            if (foundResult == null) {
                throw new IllegalStateException("the result is null!");
            }

            // update mutableLiveData objects with new values
            multiple_found.add(foundResult);
            results.postValue(multiple_found);
            determineSingleResult();
        }

        @Override
        // This method is called for every child in the query, that changes (e.g. a name is changed).
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            Log.d(TAG, "edit change detected");

            // retrieve changed value
            T foundResult = snapshot.getValue(aClass);
            if (foundResult == null) { return; }

            // update the corresponding object in the MutableLiveData:
            // find the object by id,
            // then remove the old one and add the new one.
            if (multiple_found.removeIf(object -> object.getUid().equals(foundResult.getUid()))) {
                multiple_found.add(foundResult);
                results.postValue(multiple_found);
                determineSingleResult();
            }
        }

        @Override
        // This method is called for every child in the query that is removed
        // -> was in the query and in the app, but is now removed from the database.
        // usually triggers after deleteFromDatabase is called.
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            Log.d(TAG, "removal change detected");

            // retrieve the removed value
            T removedResult = snapshot.getValue(aClass);
            if (removedResult == null){ return; }

            // remove the object from the MutableLiveData, by id.
            if (multiple_found.removeIf(object -> object.getUid().equals(removedResult.getUid()))) {
                results.postValue(multiple_found);
                determineSingleResult();
            } else {
                Log.d(TAG, "removed object not found!");
            }
        }

        @Override
        // called when a child is moved from one place in the query to another.
        // This doesn't change anything for the availability of the data,
        // and is thus ignored.
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        // Called when the query fails
        public void onCancelled(@NonNull DatabaseError error) {
            // echo the exception thrown by FireBase
            throw error.toException();
        }
    }
}
