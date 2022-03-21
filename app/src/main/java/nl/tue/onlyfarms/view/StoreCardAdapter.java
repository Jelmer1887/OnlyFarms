package nl.tue.onlyfarms.view;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.model.Store;
import nl.tue.onlyfarms.view.client.HomeRecyclerViewHolder;
import nl.tue.onlyfarms.viewmodel.HomeViewModel;

public class StoreCardAdapter extends RecyclerView.Adapter<HomeRecyclerViewHolder> {
    private final static String TAG = "StoreCardAdapter";
    private Random random;
    private int nrStores = 0;
    private Set<Store> currentlyShownStores = new HashSet<>();
    private Map<Store, HomeRecyclerViewHolder> cards = new HashMap<>();

    public StoreCardAdapter(int seed, LifecycleOwner lifecycleOwner, MutableLiveData<Set<Store>> storeList) {
        this.random = new Random(seed);

        // determine nr of stores and add stores to 'cards' map for creation / update.
        storeList.observe(lifecycleOwner, stores -> {
            nrStores = 0;
            for (Store store : stores) {
                nrStores += 1;

                // check if there is already a viewHolder associated with the store,
                // if so, then it should be refreshed to reflect any possible changes.
                if (cards.containsKey(store)) {
                    HomeRecyclerViewHolder holder = cards.get(store);
                    if (holder != null) {
                        setFields(store, holder);
                    }
                    continue;
                }
                cards.put(store, null); // associated view will be added once they are created by 'onCreateViewHolder'
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
        Store store = getNextStore();
        cards.put(store, holder);
        holder.getView().setText(String.valueOf(random.nextInt()));
        Log.d(TAG, "onBindViewHolder: matching store " + store.getUid() + " and " + holder.getAdapterPosition());
        setFields(store, holder);
    }

    @Override
    public int getItemCount() {
        if (nrStores == 0) {
            Log.e(TAG, "storeList is most likely empty! -> not showing jack shit");
        }
        Log.d(TAG, "itemCount will be: " + nrStores);
        return nrStores;
    }

    private Store getNextStore() {
        String msg = "getNextStore: cards:\n";
        for (Store s : cards.keySet()) {
            msg += "[" + s + " <=> " + cards.get(s) + "]\n";
        }
        Log.d(TAG, msg);
        for (Store s : cards.keySet()) {
            if (cards.get(s) == null) {
                Log.d(TAG, "getNextStore: next store = " + s);
                return s;
            }
        }
        throw new IllegalStateException("no empty Stores left in 'cards' lists to associate!");
    }

    private void setFields(Store store, HomeRecyclerViewHolder holder) {
        Log.d(TAG, "setFields: updating cards");
        holder.getNameField().setText(store.getName());
        holder.getAddressField().setText(store.getPhysicalAddress());
    }
}