package nl.tue.onlyfarms.modelViewTests;

import android.content.Context;
import android.text.Editable;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.junit.Assert.assertNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.*;

import nl.tue.onlyfarms.viewmodel.LoginViewModel;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class LoginViewModelTest {

    @Mock
    EditText val0 = Mockito.mock(EditText.class);
    EditText val1 = Mockito.mock(EditText.class);
    EditText val2 = Mockito.mock(EditText.class);
    Editable e0 = Mockito.mock(Editable.class);
    Editable e1 = Mockito.mock(Editable.class);
    Editable e2 = Mockito.mock(Editable.class);

    @Test
    public void login_fields_valid() {
        EditText[] texts = new EditText[3];
        texts[0] = val0;
        texts[1] = val1;
        texts[2] = val2;

        Mockito.when(val0.getText()).thenReturn(e0);
        Mockito.when(val1.getText()).thenReturn(e1);
        Mockito.when(val2.getText()).thenReturn(e2);

        Mockito.when(e0.toString()).thenReturn("Hello");
        Mockito.when(e1.toString()).thenReturn("World");
        Mockito.when(e2.toString()).thenReturn("!");

        boolean check = nl.tue.onlyfarms.viewmodel.LoginViewModel.checkFields(texts);
        assertTrue(check);
    }

    @Test
    public void login_fields_not_valid() {

        EditText[] texts = new EditText[3];
        texts[0] = val0;
        texts[1] = val1;
        texts[2] = val2;

        Mockito.when(val0.getText()).thenReturn(e0);
        Mockito.when(val1.getText()).thenReturn(e1);
        Mockito.when(val2.getText()).thenReturn(e2);

        Mockito.when(e0.toString()).thenReturn("Hello");
        Mockito.when(e1.toString()).thenReturn("World");
        Mockito.when(e2.toString()).thenReturn("");

        boolean check = nl.tue.onlyfarms.viewmodel.LoginViewModel.checkFields(texts);
        assertFalse(check);
    }
}