package nl.tue.onlyfarms.viewmodel;

import android.text.TextUtils;
import android.widget.EditText;

import androidx.lifecycle.ViewModel;

import nl.tue.onlyfarms.model.FireBaseService;
import nl.tue.onlyfarms.model.User;

public class RegisterViewModel extends ViewModel {

    //private static boolean checkEmpty(String val){
      //  return TextUtils.isEmpty(val);
    //}

    public  static boolean checkFields(EditText[] fields, EditText pwrd){
        boolean valid = true;
        valid = AbstractLoginViewModel.check(fields);

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
