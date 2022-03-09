package nl.tue.onlyfarms.model;

import com.google.firebase.database.FirebaseDatabase;

public class FirebaseUserService {

    public static void updateUser(User user) {
        FirebaseDatabase database = OurFirebaseDatabase.getInstance();
        database.getReference().child("users").child(user.getUid()).setValue(user);
    }
}
