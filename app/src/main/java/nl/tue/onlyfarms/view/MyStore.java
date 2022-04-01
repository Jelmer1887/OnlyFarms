package nl.tue.onlyfarms.view;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;

import nl.tue.onlyfarms.R;

public class MyStore extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String mode;
    final String TAG = "MyStore";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mystore);

        mode = (String) getIntent().getExtras().getSerializable("mode");

        if (mode.equals("edit")) {
            ((TextView)findViewById(R.id.addStore)).setText(R.string.editStore);
            ((MaterialToolbar)findViewById(R.id.topBar)).setTitle(R.string.editStore);
            prefillFields();
        }

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.hours, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        Spinner spinner2 = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.hours, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(this);

        // Enabled action bar back to Base activity
        setSupportActionBar(findViewById(R.id.topBar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            Log.e(TAG, "getSupportActionBar() returned null after 'findViewById(...)' call! -> skipping 'setDisplayHomeAsUpEnabled(true)'");
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(adapterView.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void prefillFields() {
        //TODO: Prefill fields
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
