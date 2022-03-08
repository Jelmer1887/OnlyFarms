package nl.tue.onlyfarms.viewmodel;

import android.graphics.Color;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ProgressBar;

public class RegisterViewModel {

    private static boolean checkEmpty(String val){
        return TextUtils.isEmpty(val);
    }

    public  static boolean checkFields(EditText[] fields, EditText pwrd){
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

        if (pwrd.getText().toString().trim().length() < 6) {
            pwrd.setText("");
            pwrd.setError(pwrd.getHint() + " must be at least 6 characters long!");
            valid = false;
        }

        return valid;
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
