package nl.tue.onlyfarms.viewmodel;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ProgressBar;

public class RegisterViewModel {

    private static boolean checkEmpty(String val){
        return TextUtils.isEmpty(val);
    }

    public static boolean checkEmail(EditText email){
        String eMailValue = email.getText().toString().trim();

        // User did not enter a value
        if (checkEmpty(eMailValue)){
            email.setError("A value must be entered");
            return false;
        }

        return true;
    }

    public static boolean checkPassword(EditText password){
        String value = password.getText().toString().trim();

        // User did not enter a value
        if (checkEmpty(value)){
            password.setError("Password must be entered");
            return false;
        }

        // User password is too short
        if (value.length() < 4){
            password.setError("Password must be at least 4 characters long");
            return false;
        }

        return true;
    }
}
