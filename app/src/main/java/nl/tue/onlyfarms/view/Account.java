

package nl.tue.onlyfarms.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.model.FireBaseService;
import nl.tue.onlyfarms.model.User;
import nl.tue.onlyfarms.viewmodel.HomeViewModel;

public class Account extends Fragment {

    private static final String TAG = "AccountFragment";
    private HomeViewModel model;

    private EditText firstNameField;
    private EditText lastNameField;
    private EditText userNameField;
    private EditText emailField;
    private EditText oldPassWordField;
    private EditText newPassWordField;
    private Button confirmButton;
    private Button removeButton;


    public static Account newInstance() {
        return new Account();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        model = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        View fragmentView = getView();
        if (fragmentView == null) {
            throw new NullPointerException("view is null!");
        }
        firstNameField = fragmentView.findViewById(R.id.myAccount_firstName);
        lastNameField = fragmentView.findViewById(R.id.myAccount_lastName);
        userNameField = fragmentView.findViewById(R.id.myAccount_userName);
        emailField = fragmentView.findViewById(R.id.myAccount_email);
        confirmButton = fragmentView.findViewById(R.id.myAccount_confirmButton);
        removeButton = fragmentView.findViewById(R.id.myAccount_REMOVE);

        // Fill fields with current data (when the data is retrieved)
        model.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user == null) {
                Log.e(TAG, "attempted to fill fields, but user was null!");
                return;
            }
            firstNameField.setText(user.getFirstName());
            lastNameField.setText(user.getLastName());
            userNameField.setText(user.getUserName());
            emailField.setText(user.getEmailAddress());
        });


        confirmButton.setOnClickListener(v -> {
            Log.d(TAG, "submitting new account data to viewModel...");
            if (model.getUser().getValue() == null) {
                Toast.makeText(getActivity(), "user couldn't be determined: was null!", Toast.LENGTH_SHORT).show();
                throw new NullPointerException("failed to determine user, user is null!");
            }
            User newUser = new User(
                model.getUser().getValue().getUid(),
                userNameField.getText().toString().trim(),
                firstNameField.getText().toString().trim(),
                lastNameField.getText().toString().trim(),
                emailField.getText().toString().trim(),
                model.getUser().getValue().getStatus()
            );
            model.uploadUser(newUser).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "account updated!", Toast.LENGTH_SHORT).show();
                } else {
                    String msg = task.getException() == null ? "Something went wrong" : task.getException().getMessage();
                    Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
                }
            });
        });

        removeButton.setOnClickListener(v -> {
            assert getActivity() != null;
            Log.d(TAG, "Remove account pressed!");
            if (model.getUser().getValue() == null) {
                Toast.makeText(getActivity(), "user couldn't be determined: was null!", Toast.LENGTH_SHORT).show();
                throw new NullPointerException("failed to determine user, user is null!");
            }

            model.removeCurrentUser().observeForever(complete -> {
                String msg = "Your account was removed!";
                if (complete) { Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show(); }
            });
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getContext(), LoginView.class));
            getActivity().finish();
        });
    }
}