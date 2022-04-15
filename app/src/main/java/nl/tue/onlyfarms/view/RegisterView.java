package nl.tue.onlyfarms.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.atomic.AtomicReference;

import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.model.User;
import nl.tue.onlyfarms.viewmodel.RegisterViewModel;

public class RegisterView extends AppCompatActivity {
    private EditText firstNameElement, lastNameElement, eMailElement, passwordElement;
    private Button confirmButtonElement;
    private RadioGroup accountTypeGroup;
    private ProgressBar spinnerElement;
    private FirebaseAuth firebaseAuth;
    private AtomicReference<User.Status> status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // get firebase authentication instance
        firebaseAuth = FirebaseAuth.getInstance();

        // check if user is already logged in
        if (firebaseAuth.getCurrentUser() != null){
            Toast.makeText(RegisterView.this, "Already logged in", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(), Base.class));
            finish();
        }

        // initialize component representations (retrieve elements from UI)
        firstNameElement = findViewById(R.id.register_firstName);
        lastNameElement = findViewById(R.id.register_lastName);
        accountTypeGroup = findViewById(R.id.register_accountType);
        eMailElement = findViewById(R.id.register_email);
        passwordElement = findViewById(R.id.register_password);
        confirmButtonElement = findViewById(R.id.register_submit);
        spinnerElement = findViewById(R.id.register_spinner);
        status = new AtomicReference<>(User.Status.CLIENT);

        // lister for changes to radio group
        accountTypeGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch(checkedId) {
                case R.id.register_accountType_client:
                    status.set(User.Status.CLIENT);
                    break;
                case R.id.register_accountType_vendor:
                    status.set(User.Status.VENDOR);
                    break;
            }
        });

        confirmButtonElement.setOnClickListener(this::onSubmit);
    }

    /*
     * Creates a new user with the provided data.
     */
    public void onSubmit(View v) {
        // check the data filled in by the user
        if (!RegisterViewModel.checkFields(new EditText[]{firstNameElement, lastNameElement, eMailElement, passwordElement}, passwordElement)){
            spinnerElement.setVisibility(View.INVISIBLE);
            return;
        }

        // register the user with the database
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
                            // Make user with the data
                            RegisterViewModel.createUser(task.getResult().getUser().getUid(), firstNameElement.getText().toString() + " " + lastNameElement.getText().toString(), firstNameElement.getText().toString(), lastNameElement.getText().toString(), eMailElement.getText().toString(), status.get());
                            startActivity(new Intent(getApplicationContext(), Base.class));
                            finish();
                        } else {
                            spinnerElement.setVisibility(View.INVISIBLE);
                            Toast.makeText(RegisterView.this, "Something went wrong! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}