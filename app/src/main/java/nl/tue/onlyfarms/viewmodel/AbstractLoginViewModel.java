package nl.tue.onlyfarms.viewmodel;

import android.text.TextUtils;
import android.widget.EditText;

import androidx.lifecycle.ViewModel;

public class AbstractLoginViewModel extends ViewModel {
    protected static boolean checkEmpty(String val){
        return TextUtils.isEmpty(val);
    }

    public static boolean check(EditText[] fields) {
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


}

