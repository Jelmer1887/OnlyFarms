package nl.tue.onlyfarms.view.client;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

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
            products.clear();
            if (productSet == null) return;
            products.addAll(productSet);
        });
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
        //TODO: implement data here.
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public Product getItem(int pos) { return products.get(pos); }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
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
