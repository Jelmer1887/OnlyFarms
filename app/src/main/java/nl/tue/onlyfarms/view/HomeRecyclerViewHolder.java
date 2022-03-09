package nl.tue.onlyfarms.view;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import nl.tue.onlyfarms.R;

public class HomeRecyclerViewHolder extends RecyclerView.ViewHolder {

    private TextView view;
    public HomeRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView.findViewById(R.id.randomText);
    }

    public TextView getView(){
        return view;
    }
}
