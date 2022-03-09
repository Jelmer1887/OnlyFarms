package nl.tue.onlyfarms.model;

import com.google.firebase.database.FirebaseDatabase;

public class OurFirebaseDatabase {
    public static FirebaseDatabase getInstance() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://onlyfarms-c32eb-default-rtdb.europe-west1.firebasedatabase.app");
        // Simluator code
        //database.useEmulator("", 9000);
        return database;
    }
}
