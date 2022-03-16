package nl.tue.onlyfarms;

import android.content.Context;
import android.content.Intent;

import java.util.Map;
import java.util.HashMap;

import nl.tue.onlyfarms.ui.main.vendor.MyStore;
import nl.tue.onlyfarms.view.client.Home;

public class Navigation {
    private static Navigation instance;
    private static Map<Integer, Class> navTargets = new HashMap<>();

    public Navigation(){}

    public static Navigation getInstance() {
        if (instance == null) {
            instance = new Navigation();
            instance.addNavElement(R.id.navto_home, Home.class);
            instance.addNavElement(R.id.navto_mystore, MyStore.class);
            instance.addNavElement(R.id.navto_myaccount, Home.class);
            instance.addNavElement(R.id.navto_reservations, Home.class);
        }

        return instance;
    }

    public void addNavElement(int id, Class destination){ navTargets.put(id, destination); }

    public Intent toNavigator(int id, Context context) {
        if (!navTargets.containsKey(id)) {
            throw new IllegalArgumentException("requested ID not present in nav");
        }

        return new Intent(context, navTargets.get(id));
    }
}