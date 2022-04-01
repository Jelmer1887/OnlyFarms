package nl.tue.onlyfarms.viewmodel;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import nl.tue.onlyfarms.view.LoginView;

public class LoginViewModel extends ViewModel {
    private final FirebaseAuth auth;

    private boolean checkEmpty(String val){
        return TextUtils.isEmpty(val);
    }

    public boolean checkFields(EditText[] fields){
        boolean valid = true;
        for (EditText f : fields) {
            String fieldValue = f.getText().toString().trim();
            if (!checkEmpty(fieldValue)) {
                continue;   // guard: skip non-empty fields
            }
            f.setText("");
            f.setError(f.getHint() + " must be filled in!");
            valid = false;
        }

        return valid;
    }

    public LoginViewModel() {
        this.auth = FirebaseAuth.getInstance();
    }

    public Task<AuthResult> login(String email, String password) {
        return auth.signInWithEmailAndPassword(email, password);
    }
}
