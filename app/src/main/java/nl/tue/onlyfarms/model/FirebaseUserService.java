package nl.tue.onlyfarms.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseUserService {

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
    public static User getUser(String uid) {
        // TODO: return actual user with matching uid from database
        final FirebaseDatabase database = OurFirebaseDatabase.getInstance();
        final DatabaseReference userRef = database.getReference("users");
        userRef.orderByChild("firstName").equalTo("Tobias").addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                System.out.println(snapshot.getKey());
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

        return new User(
                FirebaseAuth.getInstance().getCurrentUser().getUid(),
                "UsrName",
                "A",
                "B",
                FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                User.Status.VENDOR
        );
        }
}
