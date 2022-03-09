package nl.tue.onlyfarms.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.databinding.ActivityRegisterBinding;
import nl.tue.onlyfarms.viewmodel.RegisterViewModel;

public class RegisterView extends AppCompatActivity {
    EditText firstNameElement, lastNameElement, eMailElement, passwordElement;
    Button confirmButtonElement;
    RadioButton accountTypeClientElement, accountTypeVendorElement;
    ProgressBar spinnerElement;
    FirebaseAuth firebaseAuth;

    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_register);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // get firebase authentication instance
        firebaseAuth = FirebaseAuth.getInstance();

        // check if user is already logged in
        if (firebaseAuth.getCurrentUser() != null){
            Toast.makeText(RegisterView.this, "Already logged in", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        // initialize component representations (retrieve elements from UI)
        firstNameElement = findViewById(R.id.register_firstName);
        lastNameElement = findViewById(R.id.register_lastName);
        accountTypeClientElement = findViewById(R.id.register_accountType_client);
        accountTypeVendorElement = findViewById(R.id.register_accountType_vendor);
        eMailElement = findViewById(R.id.register_email);
        passwordElement = findViewById(R.id.register_password);
        confirmButtonElement = findViewById(R.id.register_submit);
        spinnerElement = findViewById(R.id.register_spinner);

        confirmButtonElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // --- check the data filled in by the user

                if (!RegisterViewModel.checkFields(new EditText[]{firstNameElement, lastNameElement, eMailElement, passwordElement}, passwordElement)){
                    spinnerElement.setVisibility(View.INVISIBLE);
                    return;
                }

                // --- register the user with the database
                spinnerElement.setVisibility(View.VISIBLE);

                // add user to firebase, display error to user if failed or redirected is success.
                firebaseAuth.createUserWithEmailAndPassword(
                        eMailElement.getText().toString().trim(),
                        passwordElement.getText().toString().trim())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            spinnerElement.setVisibility(View.INVISIBLE);
                            Toast.makeText(RegisterView.this, "Account created!", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        } else {
                            spinnerElement.setVisibility(View.INVISIBLE);
                            Toast.makeText(RegisterView.this, "Something went wrong! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}