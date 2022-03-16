package nl.tue.onlyfarms.ui.main.vendor;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.viewmodel.vendor.MystoreViewModel;

public class fragment_myStore extends Fragment {

    private MystoreViewModel mViewModel;

    public static fragment_myStore newInstance() {
        return new fragment_myStore();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mystore, container, false);
    }

    /* The better way to do this is located in: https://developer.android.com/topic/libraries/architecture/viewmodel?gclid=CjwKCAiA4KaRBhBdEiwAZi1zzvGWeKDauuZad34873iu8S23nTcjZkxmHp0dCTYo4cJkCOU3LS2tthoCbGIQAvD_BwE&gclsrc=aw.ds#java */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MystoreViewModel.class);
        // TODO: Use the ViewModel
    }

}