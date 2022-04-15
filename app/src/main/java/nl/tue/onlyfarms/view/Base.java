package nl.tue.onlyfarms.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import nl.tue.onlyfarms.Navigation;
import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.model.User;
import nl.tue.onlyfarms.view.client.Home;
import nl.tue.onlyfarms.view.vendor.HomeVendor;
import nl.tue.onlyfarms.viewmodel.HomeViewModel;

public class Base extends AppCompatActivity {

    private static final String TAG = "Base";

    private MaterialToolbar topBar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Menu menu;
    private Navigation navLogic = Navigation.getInstance();
    private HomeViewModel model;

    public boolean isClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        invalidateOptionsMenu();  // notify the options menu that it needs to adjust itself
        Log.d(TAG, "creating model");
        model = new ViewModelProvider(this).get(HomeViewModel.class);

        Log.d(TAG, "checking model post-initialisation health...");
        if (model.getAllDataReceived() == null ) { throw new IllegalStateException("allDataReceived not initialised!"); }
        if (model.getAllDataReceived().getValue() == null) { throw new IllegalStateException("allDataReceived has null value!"); }
        Log.d(TAG, "model appears safe for use!");

        if (model.getAllDataReceived().getValue()) {
            Log.d(TAG, "all data available upon initialisation of Base, changing to fragments...");
            userReceived(savedInstanceState);
        } else {
            Log.d(TAG, "data unavailable upon initialisation of Base, attaching observers to change fragments once loaded!");
            model.getAllDataReceived().observe(this, isReceived -> {
                if (isReceived == null ) { return; }
                if (isReceived) {
                    userReceived(savedInstanceState);
                }
            });
        }

        setContentView(R.layout.activity_base);

        topBar = findViewById(R.id.topBar);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationBar);
        menu = navigationView.getMenu();

        topBar.setNavigationOnClickListener(this::onNavigationClick);
        navigationView.setNavigationItemSelectedListener(this::onNavItemSelected);
    }

    /*
     * Handles click event for different nav items.
     */
    private boolean onNavItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.navto_logout) { logout(); return true;}
        replaceFragment(navLogic.getTarget(id).apply(isClient));
        drawerLayout.close();

        return true;
    }

    /*
     * Sets items in the menu to visible depending on their setting.
     */
    private void onNavigationClick(View view) {
        drawerLayout.open();
        navLogic.getTargetsIterable().forEachRemaining(target -> {
            if (target == null) {
                throw new NullPointerException("navTarget received is null!");
            }

            target.setVisibility(menu, isClient);
        });
    }

    //PRE: User cannot be null && allDataReceived should be true!
    private void userReceived(Bundle savedInstanceState) {
        this.isClient = (model.getUser().getValue().getStatus() == User.Status.CLIENT);
        if (navigationView != null) {
            navigationView.invalidate();
        }
        if (savedInstanceState == null) {
            replaceFragment(isClient ? new Home() : new HomeVendor());
        }
    }

    /*
     * Helper method for replacing the main fragment.
     */
    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.replaceElement, fragment)
                .commitNow();
    }

    /*
     * Helper method for logging out.
     */
    public void logout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginView.class));
        finish();
    }
}