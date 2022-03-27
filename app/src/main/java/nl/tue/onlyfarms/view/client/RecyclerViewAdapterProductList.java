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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.model.Product;

public class RecyclerViewAdapterProductList extends RecyclerView.Adapter<RecyclerViewAdapterProductList.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapterProductList";
    private List<Product> products = new ArrayList<>();
    private ItemClickListener itemClickListener;

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
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "products in list: " + products.size());
        return products.size();
    }

    public Product getItem(int pos) { return products.get(pos); }

    private void setFields(@NonNull ViewHolder holder, Product product) {
        StringBuilder descr = new StringBuilder();
        product.getTags().forEach(tag -> {
            descr.append(tag).append(" ");
        });
        descr.append("\n").append(product.getDescription());

        holder.getNameField().setText(product.getName());
        holder.getDescriptionField().setText(descr.toString());
        holder.getQuantityField().setText("No Qnt Data");
        holder.getPriceField().setText(String.valueOf(product.getPrice()));
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView nameField;
        private final TextView priceField;
        private final TextView quantityField;
        private final TextView descriptionField;
        private final TextView quantitySelectedField;

        private final Button decreaseButton;
        private final Button increaseButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            nameField = itemView.findViewById(R.id.productCard_product_name);
            priceField = itemView.findViewById(R.id.productCard_price);
            quantityField = itemView.findViewById(R.id.productCard_quantity);
            descriptionField = itemView.findViewById(R.id.productCard_description_placeholder);
            quantitySelectedField = itemView.findViewById(R.id.productCard_number);
            decreaseButton = itemView.findViewById(R.id.productCard_button_decrease);
            increaseButton = itemView.findViewById(R.id.productCard_button_increase);
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

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }

    // implement this in parent activity to assign functionality to click
    public interface ItemClickListener {
        void onItemClick(View v, int position);
    }
}
