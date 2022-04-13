package nl.tue.onlyfarms.view;

import android.content.Intent;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

public class AbstractReservationView extends AppCompatActivity {
    public boolean onOptionsItemSelected(MenuItem item) {
        // close current activity and return to previous
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void goHome() {
        final Intent intent = new Intent(getApplicationContext(), Base.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
