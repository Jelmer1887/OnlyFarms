package nl.tue.onlyfarms.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.viewmodel.HomeViewModel;


public class Settings extends Fragment {
    private SeekBar seeker;
    private HomeViewModel model;

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        assert getActivity() != null;
        assert getView() != null;

        model = new ViewModelProvider(getActivity()).get(HomeViewModel.class);
        seeker = getView().findViewById(R.id.seekBar);

        seeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                model.maxDistance = (progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);

    }
}