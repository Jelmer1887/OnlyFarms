package nl.tue.onlyfarms.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.Random;
import java.util.UUID;

import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.model.FireBaseService;
import nl.tue.onlyfarms.model.Store;

public class addStore extends Fragment {
    private Button submitButton;

    public static addStore newInstance() { return new addStore(); }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_mystore, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        submitButton = getView().findViewById(R.id.confirm_store);

        submitButton.setOnClickListener(v -> {
            Random r = new Random();
            String placeHolder_name = String.valueOf(r.nextLong());
            String placeHolder_desc = String.valueOf(r.nextLong());
            String placeHolder_location = String.valueOf(r.nextDouble());
            String uid = UUID.randomUUID().toString();
            String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            Store newStore = new Store(uid, userUid, placeHolder_name, placeHolder_desc, placeHolder_location);
            // TODO: implement viewModel!
            new FireBaseService<>(Store.class, "stores").updateToDatabase(newStore, uid);
        });
    }
}
