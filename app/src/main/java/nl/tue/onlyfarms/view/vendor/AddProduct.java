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

import java.util.Locale;
import java.util.UUID;

import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.model.Product;
import nl.tue.onlyfarms.viewmodel.ProductViewModel;

public class AddProduct extends AbstractBackActivity {

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

        String uid = getIntent().getStringExtra("storeUid");

        model = new ViewModelProvider(this).get(ProductViewModel.class);
        product = new Product(
                UUID.randomUUID().toString(),
                uid,
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

        if (getIntent().hasExtra("product")) {
            ((Button)findViewById(R.id.addProductButton)).setText(R.string.editProduct);
            ((TextView)findViewById(R.id.addProduct)).setText(R.string.editProduct);
            ((MaterialToolbar)findViewById(R.id.topBar)).setTitle(R.string.editProduct);
            product = (Product) getIntent().getExtras().getSerializable("product");
            prefillFields();
        }

        findViewById(R.id.addProductButton).setOnClickListener(v -> onSubmit());

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
        priceInput.setText(String.format(Locale.ROOT,"%.2f", product.getPrice()));
        perUnitInput.setText(product.getUnit());
    }
}
