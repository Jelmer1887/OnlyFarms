package nl.tue.onlyfarms.view.vendor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.view.client.RecyclerViewAdapterClientReservations;

public class MyRecyclerViewAdapterVendorReservation extends RecyclerView.Adapter<MyRecyclerViewAdapterVendorReservation.ViewHolder> {

    private List<String> mData;
    private LayoutInflater mInflater;

    MyRecyclerViewAdapterVendorReservation(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.frame_card_reservation_vendor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String client_name = mData.get(position);
        holder.myTextView.setText(client_name);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.store_name);
        }
    }
}
