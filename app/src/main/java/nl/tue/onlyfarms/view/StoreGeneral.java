package nl.tue.onlyfarms.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.model.Store;

public class StoreGeneral extends AppCompatActivity {

    private Store store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.store = (Store) getIntent().getExtras().getSerializable("store");

        setContentView(R.layout.activity_store_general);

        // Enabled action bar back to Base activity
        setSupportActionBar(findViewById(R.id.topBar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.see_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), StoreDetail.class);
                intent.putExtra("store", store);
                startActivity(intent);
            }
        });
    }
}