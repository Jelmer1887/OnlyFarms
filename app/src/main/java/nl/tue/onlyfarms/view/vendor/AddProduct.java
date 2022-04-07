package nl.tue.onlyfarms.view.vendor;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.UUID;

import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.model.Product;
import nl.tue.onlyfarms.viewmodel.ProductViewModel;

public class AddProduct extends AppCompatActivity {

    private final String TAG = "AddProduct";
    private Product product;
    private ProductViewModel model;

    // input fields
    private EditText productNameInput;
    private EditText descriptionInput;
    private EditText priceInput;
    private EditText perUnitInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addproduct);

        model = new ViewModelProvider(this).get(ProductViewModel.class);
        product = new Product(
                UUID.randomUUID().toString(),
                FirebaseAuth.getInstance().getCurrentUser().getUid(),
                "",
                "",
                5,
                5,
                "",
                null
                );

        productNameInput = findViewById(R.id.productNameInput);
        descriptionInput = findViewById(R.id.descriptionInput);
        priceInput = findViewById(R.id.priceInput);
        perUnitInput = findViewById(R.id.perUnitInput);

        if (getIntent().hasExtra("edit")) {
            Button button = findViewById(R.id.addProductButton);
            button.setText(R.string.editProduct);
        }

        if (getIntent().hasExtra("product")) {
            ((TextView)findViewById(R.id.addProduct)).setText(R.string.editProduct);
            ((MaterialToolbar)findViewById(R.id.topBar)).setTitle(R.string.editProduct);
            product = (Product) getIntent().getExtras().getSerializable("product");
            prefillFields();
        }

        findViewById(R.id.addProductButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubmit();
            }
        });

        // Enabled action bar back to Base activity
        setSupportActionBar(findViewById(R.id.topBar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            Log.e(TAG, "getSupportActionBar() returned null after 'findViewById(...)' call! -> skipping 'setDisplayHomeAsUpEnabled(true)'");
        }

    }

    private void onSubmit() {
        product.setName(productNameInput.getText().toString());
        product.setDescription(descriptionInput.getText().toString());
        product.setPrice(Double.parseDouble(priceInput.getText().toString()));
        product.setUnit(perUnitInput.getText().toString());

        model.UploadProduct(product);
        finish();
    }

    private void prefillFields() {
        productNameInput.setText(product.getName());
        descriptionInput.setText(product.getDescription());
        priceInput.setText(String.valueOf(product.getPrice()));
        perUnitInput.setText(product.getUnit());
    }

    // for back button
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
