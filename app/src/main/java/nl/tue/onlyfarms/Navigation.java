package nl.tue.onlyfarms;

import androidx.fragment.app.Fragment;

import java.util.Map;
import java.util.HashMap;
import java.util.function.Function;

import nl.tue.onlyfarms.view.Account;
import nl.tue.onlyfarms.view.Settings;
import nl.tue.onlyfarms.view.addStore;
import nl.tue.onlyfarms.view.client.Home;
import nl.tue.onlyfarms.view.client.ReservationsClient;
import nl.tue.onlyfarms.view.vendor.HomeVendor;

public class Navigation {
    private static Navigation instance;
    // Supplier to make sure we get a new instance every time.
    private static Map<Integer, Function<Boolean, Fragment>> navTargets = new HashMap<>();

    public Navigation() {}

    public static Navigation getInstance() {
        if (instance == null) {
            instance = new Navigation();
            instance.addNavElement(R.id.navto_home, isClient -> isClient ? new Home() : new HomeVendor());
            instance.addNavElement(R.id.navto_mystore, isClient -> new addStore());
            instance.addNavElement(R.id.navto_myaccount, isClient -> new Account());
            instance.addNavElement(R.id.navto_reservations, isClient -> isClient ? new ReservationsClient() : new Home());
            instance.addNavElement(R.id.navto_settings, isClient -> new Settings());
        }

        return instance;
    }

    public void addNavElement(int id, Function<Boolean, Fragment> destination){
        navTargets.put(id, destination);
    }

    public Fragment toNavigator(int id, boolean isClient) {
        if (!navTargets.containsKey(id)) {
            throw new IllegalArgumentException("requested ID not present in nav");
        }

        return navTargets.get(id).apply(isClient);
    }

}