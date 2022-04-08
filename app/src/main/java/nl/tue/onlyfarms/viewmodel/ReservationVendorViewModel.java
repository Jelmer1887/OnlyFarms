package nl.tue.onlyfarms.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

import nl.tue.onlyfarms.model.FireBaseService;
import nl.tue.onlyfarms.model.Product;
import nl.tue.onlyfarms.model.Reservation;
import nl.tue.onlyfarms.model.Store;
import nl.tue.onlyfarms.model.User;

/**
 * ViewModel for the reservation screens (make-reservation & reservation-list)
 * */
public class ReservationVendorViewModel extends ViewModel {
    private final static String TAG = "ReservationVendorViewModel";

    private MutableLiveData<Set<Reservation>> reservations;
    private final FireBaseService<Reservation> reservationFireBaseService;
    private final MutableLiveData<Boolean> allDataReceived;
    private final MutableLiveData<Set<Store>> stores;
    private final List<Observer<Set<Reservation>>> activeReservationObservers;
    private final Map<String, Function<Reservation, Boolean>> filters = new HashMap<>();

    /**
     * Constructs a new viewModel for client reservations
     * */
    public ReservationVendorViewModel() {
        this.reservationFireBaseService = new FireBaseService<>(Reservation.class, "reservations");
        this.allDataReceived = new MutableLiveData<>(false);
        this.reservations = new MutableLiveData<>(null);
        this.stores = new MutableLiveData<>(null);
        this.activeReservationObservers = new ArrayList<>();

        stores.observeForever(storeList -> {
            // Old reservation data is removed when user changes
            Log.d(TAG, "user changed -> removing old data/observers");
            this.activeReservationObservers.forEach( observer -> this.reservations.removeObserver(observer));
            this.activeReservationObservers.clear();
            this.allDataReceived.postValue(false);

            if (storeList == null) { return; }
            this.reservations = reservationFireBaseService.getAllAtReference();
            HashSet<String> storeSet = new HashSet<>();
            for (Store store : storeList) {
                storeSet.add(store.getUid());
            }

            Observer<Set<Reservation>> reservationObserver = data -> {
                if (data == null) { return; }
                Log.d(TAG, "reservation data received!");
                data.removeIf(reservation -> !storeSet.contains(reservation.getStoreUid()));
//                data.removeIf(reservation -> {
//                    for (Function<Reservation, Boolean> filter : this.filters.values()) {
//                        if (!filter.apply(reservation)) { return false; }
//                    }
//                    return true;
//                });
                this.allDataReceived.postValue(true);
                Log.d(TAG, "data: " + data.size());
            };

            this.reservations.observeForever(reservationObserver);
            activeReservationObservers.add(reservationObserver);
        });
    }

    public void setStores(Set<Store> stores) {
        this.stores.postValue(stores);
    }

    /**
     * Gets a representation of whether the model is safe to be used
     * @return {@link MutableLiveData<Boolean>} with {@code null} if the model is uninitialised,
     * {@code true} if the model is safe to use and {@code false} if the model is not safe yet
     * (e.g. no client is provided yet (use setClient first!)).
     * */
    public MutableLiveData<Boolean> getAllDataReceived() { return allDataReceived; }

    /**
     * Gets the filtered list of reservations
     * @return {@link MutableLiveData<Set<Reservation>>} with the filtered list of reservations
     */
    public MutableLiveData<Set<Reservation>> getFilteredReservations() {
        return reservations;
    }
}
