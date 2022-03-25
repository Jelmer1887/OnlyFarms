package nl.tue.onlyfarms.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.model.Store;

public class StoreDetail extends AppCompatActivity {

    private Store store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.store = (Store) getIntent().getExtras().getSerializable("store");

        setContentView(R.layout.activity_store_detail);

        // Enabled action bar back to Base activity
        setSupportActionBar(findViewById(R.id.topBar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // close current activity and return to previous
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}