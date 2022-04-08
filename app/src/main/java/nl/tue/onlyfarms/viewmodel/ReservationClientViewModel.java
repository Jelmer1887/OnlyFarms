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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

import nl.tue.onlyfarms.model.FireBaseService;
import nl.tue.onlyfarms.model.Product;
import nl.tue.onlyfarms.model.Reservation;
import nl.tue.onlyfarms.model.User;

/**
 * ViewModel for the reservation screens (make-reservation & reservation-list)
 * */
public class ReservationClientViewModel extends ViewModel {
    private final static String TAG = "ReservationClientViewModel";

    private final MutableLiveData<String> userUid;
    private MutableLiveData<Reservation> unconfirmedReservation;
    private MutableLiveData<Set<Reservation>> reservations;
    private final FireBaseService<Reservation> reservationFireBaseService;
    private final MutableLiveData<Boolean> allDataReceived;
    private final List<Observer<Set<Reservation>>> activeReservationObservers;
    private final Map<String, Function<Reservation, Boolean>> filters = new HashMap<>();

    /**
     * Constructs a new viewModel for client reservations
     * */
    public ReservationClientViewModel() {
        this.reservationFireBaseService = new FireBaseService<>(Reservation.class, "reservations");
        this.allDataReceived = new MutableLiveData<>(false);
        this.userUid = new MutableLiveData<>(null);
        this.unconfirmedReservation = new MutableLiveData<>(null);
        this.reservations = new MutableLiveData<>(null);
        this.activeReservationObservers = new ArrayList<>();

        userUid.observeForever(client -> {
            // Old reservation data is removed when user changes
            Log.d(TAG, "user changed -> removing old data/observers");
            this.activeReservationObservers.forEach( observer -> this.reservations.removeObserver(observer));
            this.activeReservationObservers.clear();
            this.allDataReceived.postValue(false);

            if (client == null) { return; }
            this.reservations = reservationFireBaseService.getAllMatchingField("userUid", client);

            Observer<Set<Reservation>> reservationObserver = data -> {
                if (data == null) { return; }
                Log.d(TAG, "reservation data received!");

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

    /**
     * Give the viewModel the client data. Required for operation of this viewModel
     * until this method is called, this viewModel will not do anything.
     * @param userUid {@link String} object to provide to the viewModel.
     * */
    public void setUserUid(String userUid) {
        this.userUid.postValue(userUid);
    }

    /**
     * Adds a condition every retrieved reservation need to satisfy in order to be stored/shown.
     * The provided {@param filterPredicate} will be evaluated with each incoming {@link Reservation}
     * object as argument to determine whether it should be filtered out of the dataset.
     * @param name {@link String} name of the condition (for retrieval and querying purposes)
     * @param filterPredicate {@link Function} that takes a {@link Reservation} as argument and
     *                                        return a {@link Boolean} representing if the reservation
     *                                        should be included (true) or not (false) in the dataset.
     * */
    public void addFilter(String name, Function<Reservation, Boolean> filterPredicate) {
        this.filters.put(name, filterPredicate);
    }

    /**
     * Removes a predicate from the set of predicates that are evaluated as filters to the data.
     * @param name {@link String} using which the predicates is indexed.
     * */
    public void removeFilter(String name) {
        this.filters.remove(name);
    }

    /**
     * Gets the value of a reservation that is currently being build, but is not confirmed yet.
     * @return {@link MutableLiveData<Reservation>} where some fields may be null!
     * */
    public MutableLiveData<Reservation> getUnconfirmedReservation() { return this.unconfirmedReservation; }

    /**
     * Changes or Creates a unconfirmed reservation. Such a reservation may contain null values
     * (though it would be better not to), and is updated with any data this method can find.
     * Internal fields of a reservation (such as its date, user and store) are determined automatically
     * and don't need to be provided. Products are associated with their uid to the reservation.
     * NOTE: this method does not guarantee that all products are associated at the same store!
     * @param product {@link Product} object to be changed, added or removed from the reservation.
     *                               to remove the product, change its quantity to 0.
     * NOTE2: this method does NOT trigger the MutableLiveData object's observers unless no
     *                               unconfirmed reservation was present.
     * */
    public void updateUnconfirmedReservation(Product product) {
        Calendar calendar = Calendar.getInstance();
        Reservation reservation = this.unconfirmedReservation.getValue();

        if (reservation == null) { reservation = new Reservation(); }
        if (reservation.getDate() == null) { reservation.setDate(calendar.getTime()); }
        if (reservation.getUid() == null) {reservation.setUid(UUID.randomUUID().toString());}
        if (reservation.getProducts() == null) {reservation.setProducts(new HashMap<>());}
        if (reservation.getStoreUid() == null) { reservation.setStoreUid(product.getStoreUid()); }
        if (reservation.getUserUid() == null) {reservation.setUserUid(FirebaseAuth.getInstance().getUid());}

        reservation.getProducts().put(product.getUid(), product.whatIsInCart());

        // products with a quantity <= 0 are added to a removal list, as the reservation cannot be
        // read and edited at the same time (Iterator design pattern property)
        List<String> removalList = new ArrayList<>();
        reservation.getProducts().forEach((uid, amount) -> {
            if (amount <= 0) { removalList.add(uid); }
        });
        for (String uid : removalList) { reservation.getProducts().remove(uid); }
        this.unconfirmedReservation.setValue(reservation);
    }

    /**
     * This method confirms and validates a reservation.
     * It iterates over all products listed in the reservation, and confirms that they are all
     * of the same store, and that there is sufficient stock available at that store to match
     * the listed quantities.
     * If the reservation is valid, it gets uploaded to the database and cleared from the
     * {@link MutableLiveData<Reservation>} object representing unconfirmed reservations.
     * If something is wrong with the reservation, or the upload fails, the reservation is
     * maintained, and the {@link Task<Void>} is failed with some feedback.
     * Implementing a response to the task is considered the responsibility of the associated view.
     * */
    public Task<Void> confirmReservation() {
        return UploadReservation(this.unconfirmedReservation.getValue());
    }

    public Task<Void> UploadReservation(Reservation r) {
        return reservationFireBaseService.updateToDatabase(r, r.getUid());
    }

    /**
     * Getter for client
     * @return the {@link MutableLiveData<User>} object which contain the user value the data is
     * retrieved for in this viewModel
     * */
    public MutableLiveData<String> getUserUid() { return this.userUid; }

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
