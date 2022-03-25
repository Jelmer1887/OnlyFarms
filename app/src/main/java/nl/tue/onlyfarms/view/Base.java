package nl.tue.onlyfarms.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Iterator;

import nl.tue.onlyfarms.Navigation;
import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.databinding.ActivityBaseBinding;
import nl.tue.onlyfarms.model.FireBaseService;
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
    private boolean isClient = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        invalidateOptionsMenu();  // notify the options menu that it needs to adjust itself
        Log.d(TAG, "creating model");
        model = new ViewModelProvider(this).get(HomeViewModel.class);
        new FireBaseService<>(User.class, "users").getSingleMatchingField("uid", FirebaseAuth.getInstance().getUid())
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
        });
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