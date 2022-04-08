package nl.tue.onlyfarms.view;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.model.Store;
import nl.tue.onlyfarms.view.client.HomeRecyclerViewHolder;

public class StoreCardAdapter extends RecyclerView.Adapter<HomeRecyclerViewHolder> {
    private final static String TAG = "StoreCardAdapter";
    private final List<Store> storeData = new ArrayList<>();
    private ItemClickListener itemClickListener;

    public StoreCardAdapter(LifecycleOwner lifecycleOwner, MutableLiveData<Set<Store>> storeList) {
        if (storeList == null) {
            String msg = "received null LifeData object as argument!";
            throw new NullPointerException(msg);
        }

        // determine nr of stores and add stores to 'cards' map for creation / update.
        Observer<Set<Store>> dataCopierListener = stores -> {
            if (stores == null) { return; }
            storeData.clear();
            storeData.addAll(stores);
        };
        storeList.observe(lifecycleOwner, dataCopierListener);
    }

    public StoreCardAdapter() {
        // a change to empty data -> set size to 0!
        storeData.clear();
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.frame_card_store;
    }

    @NonNull
    @Override
    public HomeRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new HomeRecyclerViewHolder(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeRecyclerViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder entered => binding data to viewHolders.");
        setFields(storeData.get(position), holder);
    }

    @Override
    public int getItemCount() {
        if (storeData.size() == 0) {
            Log.d(TAG, "storeList is most likely empty!");
        }
        Log.d(TAG, "itemCount will be: " + storeData.size());
        return storeData.size();
    }

    // method to retrieve store of clicked card
    public Store getItem(int id) { return storeData.get(id); }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener{
        void onItemClick(View view, int position);
    }

    private void setFields(Store store, HomeRecyclerViewHolder holder) {
        Log.d(TAG, "setFields: updating cards");
        holder.getNameField().setText(store.getName());
        holder.getAddressField().setText(store.getPhysicalAddress());
        holder.getImageField().setImageResource(R.drawable.ic_baseline_bug_report_24);
        holder.getOpeningsHoursField().setText(String.format(Locale.ROOT, "open %s - %s", store.getOpeningTime(), store.getClosingTime()));
    }
}