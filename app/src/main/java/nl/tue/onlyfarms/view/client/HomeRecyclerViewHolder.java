package nl.tue.onlyfarms.view.client;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import nl.tue.onlyfarms.R;

public class HomeRecyclerViewHolder extends RecyclerView.ViewHolder {

    private TextView view;
    private TextView nameField;
    private TextView addressField;

    public HomeRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView.findViewById(R.id.randomText);
        nameField = itemView.findViewById(R.id.storeCard_nameAndDistanceField);
        addressField = itemView.findViewById(R.id.storeCard_addressField);
    }

    public TextView getView(){
        return view;
    }

    public TextView getNameField() { return nameField; }

    public TextView getAddressField() { return addressField; }
}
