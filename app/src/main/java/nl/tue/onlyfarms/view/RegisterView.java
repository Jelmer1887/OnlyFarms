package nl.tue.onlyfarms.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.viewmodel.RegisterViewModel;

public class RegisterView extends AppCompatActivity {
    EditText eMailElement, passwordElement;
    Button confirmButtonElement;
    ProgressBar spinnerElement;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // get firebase authentication instance
        firebaseAuth = FirebaseAuth.getInstance();

        // check if user is already logged in
        if (firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        // initialize component representations (retrieve elements from UI)
        eMailElement = findViewById(R.id.login_field_email);
        passwordElement = findViewById(R.id.login_field_password);
        confirmButtonElement = findViewById(R.id.register_button_submit);
        spinnerElement = findViewById(R.id.register_progressBar);

        confirmButtonElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // --- check the data filled in by the user
                RegisterViewModel.checkEmail(eMailElement);
                RegisterViewModel.checkPassword(passwordElement);

                String email = eMailElement.getText().toString().trim();
                String password = passwordElement.getText().toString().trim();

                // --- register the user with the database
                spinnerElement.setVisibility(View.VISIBLE);

                // add user to firebase, display error to user if failed or redirected is success.
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(RegisterView.this, "Account created!", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            Toast.makeText(RegisterView.this, "Something went wrong! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}