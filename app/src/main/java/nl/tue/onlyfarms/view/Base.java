package nl.tue.onlyfarms.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import nl.tue.onlyfarms.Navigation;
import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.databinding.ActivityBaseBinding;
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
    private ActivityBaseBinding binding;
    private HomeViewModel model;
    private boolean isClient;

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

        /*new FireBaseService<>(User.class, "users").getSingleMatchingField("uid", FirebaseAuth.getInstance().getUid())
        .observe(this, u -> {

            if (u == null) {
                Log.d(TAG, "User has been changed to null");
                return;
            }
            Log.d(TAG, "Valid user object received!");

            // tell model what data it should retrieve.
            model.startForUserType(u.getStatus());
            isClient = u.getStatus() == User.Status.CLIENT;
            Log.d(TAG, "invalidating navigationView...");
            navigationView.invalidate();  // notify the options menu that it needs to adjust itself
            if (savedInstanceState == null) {
                replaceFragment(isClient ? new Home() : new HomeVendor());
            }
        });*/
        binding = ActivityBaseBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_base);

        topBar = findViewById(R.id.topBar);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationBar);
        menu = navigationView.getMenu();

        topBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.open();
                navLogic.getTargetsIterable().forEachRemaining(target -> {
                    if (target == null) {
                        throw new NullPointerException("navTarget received is null!");
                    }

                    target.setVisibility(menu, isClient);
                });
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.navto_logout) { logout(); return true;}

                replaceFragment(navLogic.getTarget(id).apply(isClient));
                drawerLayout.close();

                return true;
            }
        });
    }

    //PRE: User cannot be null && allDataReceived should be true!
    private void userReceived(Bundle savedInstanceState) {
        if (model.getUser().getValue() == null) {
            throw new IllegalStateException("user was null while allDataReceived was true!");
        }
        this.isClient = (model.getUser().getValue().getStatus() == User.Status.CLIENT);
        navigationView.invalidate();
        if (savedInstanceState == null) {
            replaceFragment(isClient ? new Home() : new HomeVendor());
        }
    }

    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.replaceElement, fragment)
                .commitNow();
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginView.class));
        finish();
    }
}