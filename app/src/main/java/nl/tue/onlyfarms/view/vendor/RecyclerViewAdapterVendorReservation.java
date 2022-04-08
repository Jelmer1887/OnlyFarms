package nl.tue.onlyfarms.view.vendor;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import nl.tue.onlyfarms.model.User;
import nl.tue.onlyfarms.view.client.RecyclerViewAdapterClientReservations;

public class RecyclerViewAdapterVendorReservation extends RecyclerView.Adapter<RecyclerViewAdapterVendorReservation.ViewHolder> {

    private List<Reservation> reservationData = new ArrayList<>();
    private Map<String, User> users = new HashMap<>();
    private RecyclerViewAdapterClientReservations.ItemClickListener mClickListener;

    RecyclerViewAdapterVendorReservation(LifecycleOwner lifecycleOwner, MutableLiveData<Set<Reservation>> reservationsSet, MutableLiveData<Set<User>> usersSet) {
        Observer<Set<Reservation>> reservationObserver = reservations -> {
            if (reservations == null) { return; }
            reservationData.clear();
            reservationData.addAll(reservations);
        };
        reservationsSet.observe(lifecycleOwner, reservationObserver);

        Observer<Set<User>> userObserver = users -> {
            if (users == null) { return; }
                for (User user : users) {
                    this.users.put(user.getUid(), user);
                }
        };
        usersSet.observe(lifecycleOwner, userObserver);
        Log.d(TAG, "RecyclerViewAdapterClientReservations: " + reservationData.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.frame_card_reservation_vendor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Reservation reservation = reservationData.get(position);
        String client_name = users.get(reservation.getUserUid()) != null ? users.get(reservationData.get(position).getUserUid()).getFullName(): "";
        holder.storeName.setText(client_name);
        AtomicInteger quantity = new AtomicInteger();
        reservation.getProducts().forEach((key, value) -> quantity.addAndGet(value));
        holder.itemQuantity.setText(String.valueOf(quantity.get()));
    }

    @Override
    public int getItemCount() {
        return reservationData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView storeName;
        TextView itemQuantity;

        ViewHolder(View itemView) {
            super(itemView);
            storeName = itemView.findViewById(R.id.store_name);
            itemQuantity = itemView.findViewById(R.id.tvQuantity);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    void setClickListener(RecyclerViewAdapterClientReservations.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
