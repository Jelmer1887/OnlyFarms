package nl.tue.onlyfarms.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
    public static void getUser() {
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

        }
}
