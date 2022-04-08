package nl.tue.onlyfarms.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.viewmodel.HomeViewModel;


public class Settings extends Fragment {
    private SeekBar seeker;
    private HomeViewModel model;
    private TextView distance;

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        assert getActivity() != null;
        assert getView() != null;

        model = new ViewModelProvider(getActivity()).get(HomeViewModel.class);
        seeker = getView().findViewById(R.id.seekBar);
        distance = getView().findViewById(R.id.distance);

        seeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                model.maxDistance.postValue((double) progress);
                distance.setText(String.format("Distance (%skm)", progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        super.onViewCreated(view, savedInstanceState);
    }
}