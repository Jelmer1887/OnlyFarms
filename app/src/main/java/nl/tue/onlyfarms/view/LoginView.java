package nl.tue.onlyfarms.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.databinding.ActivityLoginBinding;
import nl.tue.onlyfarms.viewmodel.LoginViewModel;

public class LoginView extends AppCompatActivity {

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

        // get firebase authentication instance
        firebaseAuth = FirebaseAuth.getInstance();

        // retrieve elements from UI
        emailElement = findViewById(R.id.login_email);
        passwordElement = findViewById(R.id.login_password);
        registerElement = findViewById(R.id.login_text_toRegister);
        confirmElement = findViewById(R.id.login_submit);
    }

    public void submit(View view) {

        if (!LoginViewModel.checkFields(new EditText[]{emailElement, passwordElement})) {
            return;
        }

        String email = emailElement.getText().toString().trim();
        String password = passwordElement.getText().toString().trim();

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(LoginView.this, "Logged in!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), Home.class));
                    finish();
                } else {
                    Toast.makeText(LoginView.this, "Oops! " + task.getException().getMessage().toString(), Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public void toRegister(View view) {
        startActivity(new Intent(getApplicationContext(), RegisterView.class));
    }
}