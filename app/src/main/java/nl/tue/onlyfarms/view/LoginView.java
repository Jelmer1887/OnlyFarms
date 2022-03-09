package nl.tue.onlyfarms.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.databinding.ActivityLoginBinding;

public class LoginView extends AppCompatActivity {

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
    }
}