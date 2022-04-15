package nl.tue.onlyfarms.view.client;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.model.Reservation;
import nl.tue.onlyfarms.model.Store;

public class RecyclerViewAdapterClientReservations extends RecyclerView.Adapter<RecyclerViewAdapterClientReservations.ViewHolder> {

    private List<Reservation> reservationData = new ArrayList<>();
    private Map<String, Store> stores = new HashMap<>();
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    RecyclerViewAdapterClientReservations(LifecycleOwner lifecycleOwner, MutableLiveData<Set<Reservation>> reservationsSet, MutableLiveData<Set<Store>> storesSet) {
        if (reservationsSet == null) {
            String msg = "received null LifeData object as argument!";
            throw new NullPointerException(msg);
        }

        // set the list of reservations to the updated data.
        Observer<Set<Reservation>> reservationObserver = reservations -> {
            if (reservations == null) { return; }
            reservationData.clear();
            reservationData.addAll(reservations);
        };
        reservationsSet.observe(lifecycleOwner, reservationObserver);

        // set the list of stores to the updated data.
        Observer<Set<Store>> storeObserver = stores -> {
            if (stores == null) { return; }
            this.stores.clear();
            for (Store store : stores) {
                this.stores.put(store.getUid(), store);
            }
        };
        storesSet.observe(lifecycleOwner, storeObserver);

        Log.d(TAG, "RecyclerViewAdapterClientReservations: " + reservationData.size());
    }

    RecyclerViewAdapterClientReservations() {
        this.reservationData.clear();
        this.stores.clear();
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.frame_card_reservation_client, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        setValues(holder, reservationData.get(position));
    }

    /*
     * Set the holder with the correct values.
     */
    private void setValues(ViewHolder holder, Reservation reservation) {
        Store store = stores.get(reservation.getStoreUid());
        holder.storeName.setText(store.getName());
        holder.storeAddress.setText(store.getPhysicalAddress());
        holder.storeOpen.setText("Open");
        AtomicInteger quantity = new AtomicInteger();
        reservation.getProducts().forEach((key, value) -> quantity.addAndGet(value));
        holder.itemQuantity.setText(String.valueOf(quantity.get()));
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return reservationData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView storeName;
        TextView storeAddress;
        TextView storeOpen;
        TextView itemQuantity;

        ViewHolder(View itemView) {
            super(itemView);
            storeName = itemView.findViewById(R.id.tvStoreName);
            storeAddress = itemView.findViewById(R.id.tvStoreAddress);
            storeOpen = itemView.findViewById(R.id.tvOpen);
            itemQuantity = itemView.findViewById(R.id.tvQuantity);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    /*
     * Convenience method for getting data at click position.
     */
    public Reservation getItem(int id) {
        return reservationData.get(id);
    }

    /*
     * Gets the store at a position.
     */
    public Store getStore(int id) {
        return stores.get(reservationData.get(id).getStoreUid());
    }

    /*
     * Allows clicks events to be caught.
     */
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    /*
     * Parent activity will implement this method to respond to click events.
     */
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}

