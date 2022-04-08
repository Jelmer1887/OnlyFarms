package nl.tue.onlyfarms.view.vendor;

import static android.content.ContentValues.TAG;

import android.content.Context;
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
import java.util.List;
import java.util.Set;

import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.model.Reservation;
import nl.tue.onlyfarms.view.client.RecyclerViewAdapterClientReservations;

public class RecyclerViewAdapterVendorReservation extends RecyclerView.Adapter<RecyclerViewAdapterVendorReservation.ViewHolder> {

    private List<Reservation> reservationData = new ArrayList<>();
    private LayoutInflater mInflater;
    private RecyclerViewAdapterClientReservations.ItemClickListener mClickListener;

    RecyclerViewAdapterVendorReservation(LifecycleOwner lifecycleOwner, MutableLiveData<Set<Reservation>> reservationsSet) {
        Observer<Set<Reservation>> reservationObserver = reservations -> {
            if (reservations == null) { return; }
            reservationData.clear();
            reservationData.addAll(reservations);
        };
        reservationsSet.observe(lifecycleOwner, reservationObserver);
        Log.d(TAG, "RecyclerViewAdapterClientReservations: " + reservationData.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.frame_card_reservation_vendor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String client_name = reservationData.get(position).getUid();
        holder.myTextView.setText(client_name);
    }

    @Override
    public int getItemCount() {
        return reservationData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.store_name);
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
