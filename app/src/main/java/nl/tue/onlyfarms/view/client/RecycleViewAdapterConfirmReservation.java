package nl.tue.onlyfarms.view.client;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.model.Product;

public class RecycleViewAdapterConfirmReservation extends RecyclerView.Adapter<RecycleViewAdapterConfirmReservation.MyViewHolder> {

    List<Product> products;
    Context context;

    public RecycleViewAdapterConfirmReservation(Set<Product> products, Context context) {
        this.products = new ArrayList<>();
        for (Product product : products) {
            if (product.whatIsInCart() > 0) {
                this.products.add(product);
            }
        }
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.frame_card_reservation_item, parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.itemName.setText(products.get(position).getName());
        holder.quantity.setText(String.valueOf(products.get(position).whatIsInCart()));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        TextView quantity;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.resitem_name);
            quantity = itemView.findViewById(R.id.resitem_number);
        }
    }
}
