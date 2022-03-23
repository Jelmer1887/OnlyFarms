package nl.tue.onlyfarms;

import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

import android.widget.EditText;

import nl.tue.onlyfarms.viewmodel.LoginViewModel;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void login_fields_valid() {
        EditText[] texts = Mockito.mock(EditText[].class);
        EditText val0 = Mockito.mock(EditText.class);
        Mockito.when(val0.getText().toString()).thenReturn("Hello");

        EditText val1 = Mockito.mock(EditText.class);
        Mockito.when(val1.getText().toString()).thenReturn("World");


        EditText val2 = Mockito.mock(EditText.class);
        Mockito.when(val2.getText().toString()).thenReturn("!");

        Mockito.when(texts[0]).thenReturn(val0);
        Mockito.when(texts[1]).thenReturn(val1);
        Mockito.when(texts[2]).thenReturn(val2);
        boolean check = LoginViewModel.checkFields(texts);
        assertTrue(check);
    }
    @Test
    public void login_fields_not_valid() {
        EditText[] texts = Mockito.mock(EditText[].class);
        EditText val0 = Mockito.mock(EditText.class);
        Mockito.when(val0.getText().toString()).thenReturn("Hello");

        EditText val1 = Mockito.mock(EditText.class);
        Mockito.when(val1.getText().toString()).thenReturn("World");

        Mockito.when(texts[0]).thenReturn(val0);
        Mockito.when(texts[1]).thenReturn(val1);
        Mockito.when(texts[2]).thenReturn(null);
        boolean check = LoginViewModel.checkFields(texts);
        assertFalse(check);
    }

}