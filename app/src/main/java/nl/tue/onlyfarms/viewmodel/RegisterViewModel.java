package nl.tue.onlyfarms.viewmodel;

import android.text.TextUtils;
import android.widget.EditText;

import nl.tue.onlyfarms.model.FireBaseService;
import nl.tue.onlyfarms.model.User;

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

    public static void createUser(
            String uid,
            String userName,
            String firstName, String lastName,
            String emailAddress,
            User.Status status
    ) {
        User user = new User(uid, userName, firstName, lastName, emailAddress, status);
        new FireBaseService<>(User.class, "users").updateToDatabase(user, uid);
    }
}
