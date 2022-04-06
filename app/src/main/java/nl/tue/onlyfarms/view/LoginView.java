package nl.tue.onlyfarms.view;

import android.content.Intent;
import android.os.Bundle;
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

        // check if user is already logged in
        if (firebaseAuth.getCurrentUser() != null){
            Toast.makeText(LoginView.this, "Already logged in", Toast.LENGTH_LONG).show();
            toBase();
        }

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
}