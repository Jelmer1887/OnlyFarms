package nl.tue.onlyfarms.model;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class FirebaseUserService {
    private static final String TAG = "FirebaseUserService";

    public static void updateUser(User user) {
        FirebaseDatabase database = OurFirebaseDatabase.getInstance();
        database.getReference().child("users").child(user.getUid()).setValue(user);
        
    }

    // function that will hopefully find a user by name (this should work, but I don't have time to test it...
    /**
     * Retrieves a user object with the specified UID from the database
     * @param uid user-id of the user to retrieve
     * @pre There exists a user-data with {@code uid} in the database
     * @throws IllegalArgumentException if the uid is not present in the database (pre is violated)
     * @post {@code \result == model.User user where user.getUid() == uid}
     * @returns model.User object representing the requested user by uid.
     *
     * (Consider using {@code MutableLiveData<User>} instead as a return type, and using the onChildChanged hook to update it )
     * */
    public static MutableLiveData<User> getUser(String uid) {
        // TODO: return actual user with matching uid from database
        final FirebaseDatabase database = OurFirebaseDatabase.getInstance();
        final DatabaseReference userRef = database.getReference("users");
        MutableLiveData<User> result = new MutableLiveData<>();
        Log.d(TAG, "result variable generated");
        Task userTask = userRef.child(uid).get().addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                Log.d(TAG, "onComplete: It has completed");
                Log.d(TAG, "onComplete: "+ task.getResult());
                DataSnapshot data = (DataSnapshot) task.getResult();
            }
        });

        Log.d(TAG, "I AM STILL ALIVE");


        userRef.orderByChild("uid").equalTo(uid).addChildEventListener(new ChildEventListener() {
            // triggers once for each match
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d(TAG, "onChildAdded: " + snapshot.getValue());
                result.postValue(snapshot.getValue(User.class));
                Log.d(TAG, "Updated user value, onChildAdded");
            }

            // this should run any time the object is modified
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                result.postValue(snapshot.getValue(User.class));
                Log.d(TAG, "Updated user value, onChildChanged");
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "Database Cancelled");
            }
        });

//        userRef.child(uid).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Log.d(TAG, "");
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        return result;
        }
}
