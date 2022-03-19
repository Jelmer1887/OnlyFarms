package nl.tue.onlyfarms.view;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import nl.tue.onlyfarms.R;

public class ReservationMakingClient extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_makingareservation_client);
    }
}
