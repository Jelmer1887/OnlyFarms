package nl.tue.onlyfarms.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.databinding.FragmentLoginBinding;

public class LoginView extends Fragment {

    private FragmentLoginBinding binding;
    // TODO: should a 'recyclerView' be here? Not sure...

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ){
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // assign views and any subcomponents here
        // TODO: add logic for views here (anything that needs to run upon the creation of the element)
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(LoginView.this)
                        .navigate(R.id.action_loginView_to_FirstFragment);
            }
        });
    }

    // TODO: event listeners go here (should only pass data to viewModel files)

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
