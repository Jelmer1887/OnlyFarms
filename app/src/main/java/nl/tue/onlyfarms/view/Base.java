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
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

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
    private Navigation navLogic = Navigation.getInstance();
    private ActivityBaseBinding binding;
    private HomeViewModel model;
    private boolean isClient;

    public boolean getIsClient() {
        return isClient;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
            if (savedInstanceState == null) {
                replaceFragment(isClient ? new Home() : new HomeVendor());
            }
        });
        super.onCreate(savedInstanceState);
        binding = ActivityBaseBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_base);

        topBar = findViewById(R.id.topBar);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationBar);

        topBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.open();
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.navto_logout) { logout(); return true;}

                replaceFragment(navLogic.toNavigator(id, isClient));
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