package nl.tue.onlyfarms.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;

import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.databinding.ActivityLoginBinding;
import nl.tue.onlyfarms.viewmodel.LoginViewModel;
import pub.devrel.easypermissions.EasyPermissions;

public class LoginView extends AppCompatActivity {

    LoginViewModel model;

    FirebaseAuth firebaseAuth;
    ActivityLoginBinding binding;
    EditText emailElement, passwordElement;
    TextView registerElement;
    Button confirmElement;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        model = new ViewModelProvider(this).get(LoginViewModel.class);

        // get firebase authentication instance
        firebaseAuth = FirebaseAuth.getInstance();

        // ask for location perms
        askPerms();

        // retrieve elements from UI
        emailElement = findViewById(R.id.login_email);
        passwordElement = findViewById(R.id.login_password);
        registerElement = findViewById(R.id.login_text_toRegister);
        confirmElement = findViewById(R.id.login_submit);
    }

    public void submit(View view) {

        if (!model.checkFields(new EditText[]{emailElement, passwordElement})) { return; }

        String email = emailElement.getText().toString().trim();
        String password = passwordElement.getText().toString().trim();

        // authenticate user
        model.login(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(LoginView.this, "Logged in!", Toast.LENGTH_LONG).show();
                toBase();
            } else {
                Toast.makeText(LoginView.this, "Oops! " + task.getException().getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void toRegister() {
        startActivity(new Intent(getApplicationContext(), RegisterView.class));
    }
    public void toRegister(View view) { toRegister(); }

    public void toBase() {
        startActivity(new Intent(getApplicationContext(), Base.class));
        finish();
    }

    public void toBase(View view) { toBase(); }

    private void askPerms() {
        // Request Permissions
        String[] perms = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            Log.d("Login", "Permissions granted!");
            checkLogin();
        } else {
            EasyPermissions.requestPermissions(this, "Location is required for this app to function", 1, perms);
        }
    }

    private void checkLogin() {
        if (firebaseAuth.getCurrentUser() != null){
            Toast.makeText(LoginView.this, "Already logged in", Toast.LENGTH_LONG).show();
            toBase();
        }
    }

    /* Methods responsible for retrieving permissions to use the gps */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int i: grantResults) {
            if (i != PackageManager.PERMISSION_GRANTED ) {
                askPerms();
                return;
            }
        }

        checkLogin();

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}