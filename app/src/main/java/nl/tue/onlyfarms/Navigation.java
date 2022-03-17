package nl.tue.onlyfarms;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import java.util.Map;
import java.util.HashMap;
import java.util.function.Supplier;

import nl.tue.onlyfarms.ui.main.vendor.MyStore;
import nl.tue.onlyfarms.view.Account;
import nl.tue.onlyfarms.view.client.Home;

public class Navigation {
    private static Navigation instance;
    // Supplier to make sure we get a new instance every time.
    private static Map<Integer, Supplier<Fragment>> navTargets = new HashMap<>();

    public Navigation(){}

    public static Navigation getInstance() {
        if (instance == null) {
            instance = new Navigation();
            instance.addNavElement(R.id.navto_home, Home::new);
            instance.addNavElement(R.id.navto_mystore, Home::new);
            instance.addNavElement(R.id.navto_myaccount, Account::new);
            instance.addNavElement(R.id.navto_reservations, Home::new);
        }

        return instance;
    }

    public void addNavElement(int id, Supplier<Fragment> destination){
        navTargets.put(id, destination);
    }

    public Fragment toNavigator(int id) {
        if (!navTargets.containsKey(id)) {
            throw new IllegalArgumentException("requested ID not present in nav");
        }

        return navTargets.get(id).get();
    }
}