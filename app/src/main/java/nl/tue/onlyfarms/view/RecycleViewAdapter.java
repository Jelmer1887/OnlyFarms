package nl.tue.onlyfarms.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import nl.tue.onlyfarms.R;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> {

    List<Reservation> reservationList;
    Context context;

    public RecycleViewAdapter(List<Reservation> reservationList, Context context) {
        this.reservationList = reservationList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.frame_card_product,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textview8.setText(reservationList.get(position).getId());
    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textview8;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textview8 = itemView.findViewById(R.id.store_name);
        }
    }
}
