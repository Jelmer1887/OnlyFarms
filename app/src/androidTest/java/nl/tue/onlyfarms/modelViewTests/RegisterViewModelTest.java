package nl.tue.onlyfarms.modelViewTests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.text.Editable;
import android.widget.EditText;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import nl.tue.onlyfarms.viewmodel.RegisterViewModel;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class RegisterViewModelTest {

    @Mock
    EditText val0 = Mockito.mock(EditText.class);
    EditText val1 = Mockito.mock(EditText.class);
    EditText val2 = Mockito.mock(EditText.class);
    EditText password = Mockito.mock(EditText.class);
    Editable e0 = Mockito.mock(Editable.class);
    Editable e1 = Mockito.mock(Editable.class);
    Editable e2 = Mockito.mock(Editable.class);
    Editable passwordEditable = Mockito.mock(Editable.class);

    @Test
    public void login_fields_valid() {
        EditText[] texts = new EditText[3];
        texts[0] = val0;
        texts[1] = val1;
        texts[2] = val2;

        Mockito.when(val0.getText()).thenReturn(e0);
        Mockito.when(val1.getText()).thenReturn(e1);
        Mockito.when(val2.getText()).thenReturn(e2);
        Mockito.when(password.getText()).thenReturn(passwordEditable);

        Mockito.when(e0.toString()).thenReturn("Hello");
        Mockito.when(e1.toString()).thenReturn("World");
        Mockito.when(e2.toString()).thenReturn("!");
        Mockito.when(passwordEditable.toString()).thenReturn("Password");

        boolean check = RegisterViewModel.checkFields(texts, password);
        assertTrue(check);
    }

    @Test
    public void login_fields_not_valid_field() {
        EditText[] texts = new EditText[3];
        texts[0] = val0;
        texts[1] = val1;
        texts[2] = val2;

        Mockito.when(val0.getText()).thenReturn(e0);
        Mockito.when(val1.getText()).thenReturn(e1);
        Mockito.when(val2.getText()).thenReturn(e2);
        Mockito.when(password.getText()).thenReturn(passwordEditable);

        Mockito.when(e0.toString()).thenReturn("Hello");
        Mockito.when(e1.toString()).thenReturn("World");
        Mockito.when(e2.toString()).thenReturn("");
        Mockito.when(passwordEditable.toString()).thenReturn("Password");

        boolean check = RegisterViewModel.checkFields(texts, password);
        assertFalse(check);
    }

    @Test
    public void login_fields_not_valid_password() {
        EditText[] texts = new EditText[3];
        texts[0] = val0;
        texts[1] = val1;
        texts[2] = val2;

        Mockito.when(val0.getText()).thenReturn(e0);
        Mockito.when(val1.getText()).thenReturn(e1);
        Mockito.when(val2.getText()).thenReturn(e2);
        Mockito.when(password.getText()).thenReturn(passwordEditable);

        Mockito.when(e0.toString()).thenReturn("Hello");
        Mockito.when(e1.toString()).thenReturn("World");
        Mockito.when(e2.toString()).thenReturn("!");
        Mockito.when(passwordEditable.toString()).thenReturn("Pass");

        boolean check = RegisterViewModel.checkFields(texts, password);
        assertFalse(check);
    }
}