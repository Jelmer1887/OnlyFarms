package nl.tue.onlyfarms.view.client;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.model.Product;

public class RecyclerViewAdapterProductList extends RecyclerView.Adapter<RecyclerViewAdapterProductList.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapterProductList";
    private final List<Product> products = new ArrayList<>();

    public RecyclerViewAdapterProductList(LifecycleOwner lifecycleOwner, MutableLiveData<Set<Product>> productData) {
        // when product data changes, (re)build the list of products
        productData.observe(lifecycleOwner, productSet -> {
            Log.d(TAG, "change in data detected! clearing product list...");
            products.clear();
            if (productSet == null) return;
            products.addAll(productSet);
            Log.d(TAG, "added " + productSet + "to product list.");
        });
        Log.d(TAG, "constructor called!");
    }


    // Inflates layout when requested
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.frame_card_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setFields(holder, products.get(position));
        holder.setQuantitySelectedField();  // this prevents a recycled card from using the 'quantitySelected' value of its...
                                            // ...previously held product for the new product.
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "products in list: " + products.size());
        return products.size();
    }

    private void setFields(@NonNull ViewHolder holder, Product product) {

        // build strings required as values in the UI fields
        String quantity = String.format(Locale.ENGLISH, "%d %s", product.getQuantity(),product.getUnit());
        String priceString = "€ " + product.getPrice();

        StringBuilder description = new StringBuilder();
        product.getTags().forEach(tag -> description.append(tag).append(" "));
        description.append("\n").append(product.getDescription());

        holder.getNameField().setText(product.getName());
        holder.getDescriptionField().setText(description.toString());
        holder.getQuantityField().setText(quantity);
        holder.getPriceField().setText(priceString);
        holder.setMaxQuantity(product.getQuantity());
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView nameField;
        private final TextView priceField;
        private final TextView quantityField;
        private final TextView descriptionField;
        private final TextView quantitySelectedField;

        private final Button increaseButton;
        private final Button decreaseButton;

        private int selectedQuantity;
        private int maxQuantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameField = itemView.findViewById(R.id.productCard_product_name);
            priceField = itemView.findViewById(R.id.productCard_price);
            quantityField = itemView.findViewById(R.id.productCard_quantity);
            descriptionField = itemView.findViewById(R.id.productCard_description_placeholder);
            quantitySelectedField = itemView.findViewById(R.id.productCard_number);
            increaseButton = itemView.findViewById(R.id.productCard_button_increase);
            decreaseButton = itemView.findViewById(R.id.productCard_button_decrease);

            // quantitySelectedField must contain a number before entering listeners
            quantitySelectedField.setText(String.valueOf(0));

            // listeners required for responding to buttons
            increaseButton.setOnClickListener(v -> setQuantitySelectedField(1));
            decreaseButton.setOnClickListener(v -> setQuantitySelectedField(-1));
        }

        private void setQuantitySelectedField(int val) {
            selectedQuantity = Integer.parseInt((String) quantitySelectedField.getText());

            if (selectedQuantity + val < 0 || selectedQuantity + val > maxQuantity) {
                return;
            }

            //TODO: pass new value to other views here!
            quantitySelectedField.setText(String.valueOf(selectedQuantity + val));
        }

        private void setQuantitySelectedField() {
            quantitySelectedField.setText(String.valueOf(0));
        }

        protected void setMaxQuantity(int max) { this.maxQuantity = max; }

        protected TextView getNameField() {
            return nameField;
        }

        protected TextView getPriceField() {
            return priceField;
        }

        protected TextView getQuantityField() {
            return quantityField;
        }

        protected TextView getDescriptionField() {
            return descriptionField;
        }

        protected TextView getQuantitySelectedField() {
            return quantitySelectedField;
        }

        protected Button getDecreaseButton() {
            return decreaseButton;
        }

        protected Button getIncreaseButton() {
            return increaseButton;
        }
    }
}
