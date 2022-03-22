package nl.tue.onlyfarms.view;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.model.Store;
import nl.tue.onlyfarms.view.client.HomeRecyclerViewHolder;
import nl.tue.onlyfarms.viewmodel.HomeViewModel;

public class StoreCardAdapter extends RecyclerView.Adapter<HomeRecyclerViewHolder> {
    private final static String TAG = "StoreCardAdapter";
    private int nrStores = 0;
    private List<Store> storeData = new ArrayList<>();

    public StoreCardAdapter(LifecycleOwner lifecycleOwner, MutableLiveData<Set<Store>> storeList) {

        // determine nr of stores and add stores to 'cards' map for creation / update.
        storeList.observe(lifecycleOwner, stores -> {
            nrStores = 0;
            storeData.clear();
            for (Store store : stores) {
                nrStores += 1;
                storeData.add(store);
            }
        });
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.frame_card_store;
    }

    @NonNull
    @Override
    public HomeRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new HomeRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeRecyclerViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder entered => binding data to viewHolders.");
        setFields(storeData.get(position), holder);
    }

    @Override
    public int getItemCount() {
        if (nrStores == 0) {
            Log.e(TAG, "storeList is most likely empty! -> not showing jack shit");
        }
        Log.d(TAG, "itemCount will be: " + nrStores);
        return nrStores;
    }

    private void setFields(Store store, HomeRecyclerViewHolder holder) {
        Log.d(TAG, "setFields: updating cards");
        holder.getNameField().setText(store.getName());
        holder.getAddressField().setText(store.getPhysicalAddress());
        holder.getImageField().setImageResource(R.drawable.ic_baseline_bug_report_24);
        holder.getRatingField().setText("Ratings coming soon (tm)");
        holder.getOpeningsHoursField().setText("OpeningHours coming soon (tm)");
        holder.getTagsField().setText("Tags coming soon (tm)");
    }
}