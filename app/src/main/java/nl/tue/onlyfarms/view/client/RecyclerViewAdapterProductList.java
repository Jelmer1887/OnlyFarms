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
    private RecyclerViewAdapterProductList.ItemClickListener itemClickListener;

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
        Log.d(TAG, "onCreateViewHolder: Viewholder being created");
        return new ViewHolder(view, itemClickListener);
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

    // method to retrieve store of clicked card
    public Product getItem(int id) { return products.get(id); }

    private void setFields(@NonNull ViewHolder holder, Product product) {

        // build strings required as values in the UI fields
        String priceString = "€ " + String.format("%.2f", product.getPrice());

        holder.getNameField().setText(product.getName());
        holder.getDescriptionField().setText(product.getDescription());
        holder.getQuantityField().setText(product.getUnit());
        holder.getPriceField().setText(priceString);
        holder.setProduct(product);
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView nameField;
        private final TextView priceField;
        private final TextView quantityField;
        private final TextView descriptionField;
        private final TextView quantitySelectedField;

        private final Button increaseButton;
        private final Button decreaseButton;

        private Product product;

        private RecyclerViewAdapterProductList.ItemClickListener listener;

        public ViewHolder(@NonNull View itemView, ItemClickListener l) {
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
            increaseButton.setOnClickListener(v -> changeByQuantitySelectedField(1));
            decreaseButton.setOnClickListener(v -> changeByQuantitySelectedField(-1));

            listener = l;
            itemView.setOnClickListener(this);
            Log.d(TAG, "ViewHolder: listener has been set");
        }

        private void changeByQuantitySelectedField(int val) {
            //TODO: pass new value to other views here!
            product.changeBy(val);
            quantitySelectedField.setText(String.valueOf(product.whatIsInCart()));
        }

        private void setQuantitySelectedField() {
            quantitySelectedField.setText(String.valueOf(0));
        }

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

        protected void setProduct(Product product) {
            this.product = product;
            quantitySelectedField.setText(String.valueOf(product.whatIsInCart()));
        }

        @Override
        public void onClick(View v) {
            if (this.listener != null) {
                listener.onItemClick(v, getAdapterPosition());
            }
        }
    }

    public void setClickListener(RecyclerViewAdapterProductList.ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener{
        void onItemClick(View view, int position);
    }
}
