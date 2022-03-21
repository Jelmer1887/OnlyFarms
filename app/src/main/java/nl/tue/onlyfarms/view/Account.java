

package nl.tue.onlyfarms.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.viewmodel.HomeViewModel;

public class Account extends Fragment {

    private static final String TAG = "AccountFragment";
    private HomeViewModel model;

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
    }
}