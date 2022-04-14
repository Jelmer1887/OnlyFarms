package nl.tue.onlyfarms.view.client;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.view.AbstractHome;
import nl.tue.onlyfarms.view.StoreCardAdapter;
import nl.tue.onlyfarms.view.StoreGeneral;
import nl.tue.onlyfarms.viewmodel.HomeViewModel;

/**
 * Home fragment for a client,
 * it uses the {@link AbstractHome} class as default, and overwrites some methods to go to the
 * correct screens.
 * Showing the correct data is handled by the viewModel {@link HomeViewModel}
 */
public class Home extends AbstractHome implements StoreCardAdapter.ItemClickListener{

    @Nullable
    @Override
    /* overwrite what layout is used for the fragment */
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_client, container, false);
    }


    @Override
    /* the super-class takes care of most functionality, only the actionButton needs to be added. */
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        assert getView() != null;
        actionButton = getView().findViewById(R.id.floatingActionButton);

        // listener for action button press to go to the map-view.
        actionButton.setOnClickListener(v -> {
            assert getActivity() != null;
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.replaceElement, new MapView())
                    .commitNow();
        });
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onItemClick(View view, int position) {
        super.onItemClick(view, position);  // super checks the pre-condition
        Intent intent = new Intent(getContext(), StoreGeneral.class);
        intent.putExtra("store", adapter.getItem(position));
        Log.d("Home", "creating StoreGeneral activity with intent: " + intent);
        startActivity(intent);
    }
}