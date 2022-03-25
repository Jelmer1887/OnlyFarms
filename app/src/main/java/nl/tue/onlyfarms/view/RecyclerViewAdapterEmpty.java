package nl.tue.onlyfarms.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import nl.tue.onlyfarms.R;

public class RecyclerViewAdapterEmpty extends RecyclerView.Adapter<RecyclerViewAdapterEmpty.ViewHolderEmpty> {

    @NonNull
    @Override
    public ViewHolderEmpty onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.frame_empty, parent);
        return new ViewHolderEmpty(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderEmpty holder, int position) {
        // Any data is bound here
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolderEmpty extends RecyclerView.ViewHolder{

        public ViewHolderEmpty(@NonNull View itemView) {
            super(itemView);
        }
    }
}
