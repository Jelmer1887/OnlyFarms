package nl.tue.onlyfarms.model;

import com.google.firebase.database.FirebaseDatabase;

public class OurFirebaseDatabase {

    public static boolean USE_EMULATOR = false;

    public static FirebaseDatabase getInstance() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://onlyfarms-c32eb-default-rtdb.europe-west1.firebasedatabase.app");
        if (USE_EMULATOR)
            database.useEmulator("localhost", 9000);
        return database;
    }
}
