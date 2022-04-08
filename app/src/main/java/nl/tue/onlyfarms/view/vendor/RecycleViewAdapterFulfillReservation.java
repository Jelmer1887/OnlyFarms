package nl.tue.onlyfarms.view.vendor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.model.Product;
import nl.tue.onlyfarms.model.Reservation;

public class RecycleViewAdapterFulfillReservation extends RecyclerView.Adapter<RecycleViewAdapterFulfillReservation.MyViewHolder> {

    List<String> products;
    List<Integer> quantities;
    Context context;

    public RecycleViewAdapterFulfillReservation(Reservation reservation, Set<Product> productSet, Context context) {
        HashMap<String, String> productNames = new HashMap<>();

        for (Product product : productSet)
            productNames.put(product.getUid(), product.getName());

        this.products = new ArrayList<>();
        this.quantities = new ArrayList<>();
        reservation.getProducts().forEach((product, quantity) -> {
            products.add(productNames.get(product));
            quantities.add(quantity);
        });
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
        holder.itemName.setText(products.get(position));
        holder.quantity.setText(String.valueOf(quantities.get(position)));
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
