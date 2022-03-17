package nl.tue.onlyfarms.ui.main.vendor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.List;

import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.model.Store;
import nl.tue.onlyfarms.model.User;
import nl.tue.onlyfarms.viewmodel.vendor.MystoreViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_addStore#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_addStore extends Fragment {

    public static fragment_addStore newInstance() {
        return new fragment_addStore();
    }
    private final static String TAG = "addStoreView";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private MystoreViewModel model;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button confirmButton;
    private EditText nameField, descriptionField, locationField;

    public fragment_addStore() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_addStore.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_addStore newInstance(String param1, String param2) {
        fragment_addStore fragment = new fragment_addStore();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        model = new ViewModelProvider(this).get(MystoreViewModel.class);
        Log.i(TAG, "created model!");
        List<MutableLiveData<Store>> stores = model.getStores();
        MutableLiveData<User> user = model.getSubjectUser();
        user.observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {

                Log.i(TAG, "got user: " + user);
                nameField.setText(user.getFirstName());
            }
        });
        Log.d(TAG, "stores upon creation: " + stores);

        Toast.makeText(getActivity(), "retrieved: " + stores, Toast.LENGTH_LONG).show();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_store, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        confirmButton = getView().findViewById(R.id.addStore_submitButton);
        nameField = getView().findViewById(R.id.addStore_storeName);
        descriptionField = getView().findViewById(R.id.addStore_description);
        locationField = getView().findViewById(R.id.addStore_location);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text_name = nameField.getText().toString().trim();
                String text_description = descriptionField.getText().toString().trim();
                String text_location = locationField.getText().toString().trim();
                String uid = Long.toString(Calendar.getInstance().getTime().getTime());
                String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Store newStore = new Store(uid, userUid, text_name, text_description, text_location);
                model.updateStores(newStore);
            }
        });
    }
}