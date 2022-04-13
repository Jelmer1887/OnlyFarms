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

    //private boolean checkEmpty(String val){
        //return TextUtils.isEmpty(val);
    //}

    public boolean checkFields(EditText[] fields){
        return AbstractLoginViewModel.check(fields);
    }

    public LoginViewModel() {
        this.auth = FirebaseAuth.getInstance();
    }

    public Task<AuthResult> login(String email, String password) {
        return auth.signInWithEmailAndPassword(email, password);
    }
}
