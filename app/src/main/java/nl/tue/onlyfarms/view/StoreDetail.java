package nl.tue.onlyfarms.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import nl.tue.onlyfarms.R;

public class StoreDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);

        // Enabled action bar back to Base activity
        setSupportActionBar(findViewById(R.id.topBar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}