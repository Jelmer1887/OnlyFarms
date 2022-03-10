package nl.tue.onlyfarms.ui.main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.ui.main.fragment_myStore;

public class MyStore extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_store_activity);
        /*if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment_myStore.newInstance())
                    .commitNow();
        }*/
    }
}