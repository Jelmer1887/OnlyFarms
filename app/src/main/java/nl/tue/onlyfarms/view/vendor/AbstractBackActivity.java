package nl.tue.onlyfarms.view.vendor;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import nl.tue.onlyfarms.R;

/**
 * Class generalising activities where a vendor goes back between screens
 * */
public class AbstractBackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        // Enabled action bar back to Base activity
        setSupportActionBar(findViewById(R.id.topBar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            Log.e("AbstractBackActivity", "getSupportActionBar() returned null after 'findViewById(...)' call! -> skipping 'setDisplayHomeAsUpEnabled(true)'");
        }

        super.onCreate(savedInstanceState);
    }

    // for back button
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // close current activity and return to previous
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
