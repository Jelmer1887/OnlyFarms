package nl.tue.onlyfarms;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.fragment.app.Fragment;

import java.util.Iterator;
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
    private static Map<Integer, NavTarget<Boolean>> navTargets = new HashMap<>();

    private static Map<Integer, Function<Boolean, Fragment>> navTargetsClient = new HashMap<>();
    private static Map<Integer, Function<Boolean, Fragment>> navTargetsVendor = new HashMap<>();

    public Navigation() {}

    public static Navigation getInstance() {
        if (instance == null) {
            instance = new Navigation();
            instance.new NavTarget<Boolean>(R.id.navto_home, isClient -> isClient ? new Home() : new HomeVendor());
            instance.new NavTarget<Boolean>(false, true, R.id.navto_mystore, new addStore());
            instance.new NavTarget<Boolean>(R.id.navto_myaccount, new Account());
            instance.new NavTarget<Boolean>(true, false, R.id.navto_reservations, new ReservationsClient());
            instance.new NavTarget<Boolean>(R.id.navto_settings, new Settings());
        }

        return instance;
    }

    public NavTarget<Boolean> getTarget(int id) {
        if (!navTargets.containsKey(id)) {
            throw new IllegalArgumentException("requested ID not present in nav");
        }

        return navTargets.get(id);
    }

    public Iterator<NavTarget<Boolean>> getTargetsIterable() {
        return navTargets.values().iterator();
    }

    /**
     * Class representing a fragment that a navigation-menu element should point to.
     * Its primary purpose is to provide a cleaner appearance when adding elements to the navbar.
     * This class facilitates changing the destination based on some conditional function.
     *
     * Any NavTarget adds itself to the list of navTargets upon creation, if an ID is provided in
     * the constructor. If not, it will need to be added manually later.
     * */
    public class NavTarget<T>{
        private static final String TAG = "NavTarget";
        private final boolean toClient;
        private final boolean toVendor;
        private Function <T, Fragment> target;
        private final int ElementId;

        /**
         * Sets the target to the destination regardless of function input, adds this to targets.
         * */
        NavTarget(boolean toClient, boolean toVendor, int elementId, Fragment destination) {
            this.toClient = toClient;
            this.toVendor = toVendor;
            this.target = any -> destination;
            this.ElementId = elementId;
            //noinspection unchecked
            navTargets.put(elementId, (NavTarget<Boolean>) this);
            Log.d(TAG, "navTarget " + elementId + " added to list!");
        }

        /**
         * Sets the target to the destination based on function input, adds this to targets.
         * */
        NavTarget(boolean toClient, boolean toVendor, int elementId, Function<T, Fragment> destinationPredicate) {
            this.toClient = toClient;
            this.toVendor = toVendor;
            this.target = destinationPredicate;
            this.ElementId = elementId;
            //noinspection unchecked
            navTargets.put(elementId, (NavTarget<Boolean>) this);
            Log.d(TAG, "navTarget " + elementId + " added to list!");
        }

        /**
         * Sets the target to the destination regardless of function input, adds this to targets.
         * Assumes it needs to be visible to all.
         * */
        NavTarget(int elementId, Fragment destination) {
            this.toClient = true;
            this.toVendor = true;
            this.target = any -> destination;
            this.ElementId = elementId;
            //noinspection unchecked
            navTargets.put(elementId, (NavTarget<Boolean>) this);
            Log.d(TAG, "navTarget " + elementId + " added to list!");
        }

        /**
         * Sets the target to the destination based on function input, adds this to targets.
         * Assumes it needs to be visible to all.
         * */
        NavTarget(int elementId, Function<T, Fragment> destinationPredicate) {
            this.toClient = true;
            this.toVendor = true;
            this.target = destinationPredicate;
            this.ElementId = elementId;
            //noinspection unchecked
            navTargets.put(elementId, (NavTarget<Boolean>) this);
            Log.d(TAG, "navTarget " + elementId + " added to list!");
        }

        /**
         * Change the destination of the navTarget.
         * */
        public void setTarget(Function<T, Fragment> target) {
            this.target = target;
        }

        /**
         * returns whether the element of this target should be visible in the navigation menu
         * based on the boolean representing if the user is a client or vendor.
         * */
        public Boolean isVisible(Boolean isClient) {
            return isClient ? this.toClient : this.toVendor;
        }

        /**
         * Removes this navTarget from navTargets variable, if applicable.
         * */
        public boolean removeFromTargets() {
            return (navTargets.remove(this.ElementId) != null);
        }

        /***
         * returns the fragment this navTarget is pointing to, based on evaluating the function.
         */
        public Fragment apply(T destinationCondition) {
            Log.d(TAG, "target(" + destinationCondition + ") -> " + target.apply(destinationCondition));
            return target.apply(destinationCondition);
        }

        public void setVisibility(Menu menu, Boolean isClient) {
            menu.findItem(this.ElementId).setVisible(isVisible(isClient));
        }
    }

}