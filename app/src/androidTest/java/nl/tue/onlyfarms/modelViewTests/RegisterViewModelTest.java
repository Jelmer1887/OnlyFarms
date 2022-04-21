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

import nl.tue.onlyfarms.viewmodel.LoginViewModel;
import nl.tue.onlyfarms.viewmodel.RegisterViewModel;

@RunWith(AndroidJUnit4.class)
public class RegisterViewModelTest {

    @Mock
    EditText value0 = Mockito.mock(EditText.class);
    EditText value1 = Mockito.mock(EditText.class);
    EditText value2 = Mockito.mock(EditText.class);
    EditText password = Mockito.mock(EditText.class);
    Editable e00 = Mockito.mock(Editable.class);
    Editable e01 = Mockito.mock(Editable.class);
    Editable e02 = Mockito.mock(Editable.class);
    Editable passwordEditable = Mockito.mock(Editable.class);

    @Test
    public void login_fields_valid() {
        EditText[] texts = new EditText[3];
        boolean check = register(texts, "!", "Password");
        assertTrue(check);
    }

    @Test
    public void login_fields_not_valid_field() {
        EditText[] texts = new EditText[3];
        boolean check = register(texts, "", "Password");
        assertFalse(check);
    }

    @Test
    public void login_fields_not_valid_password() {
        EditText[] texts = new EditText[3];
        boolean check = register(texts, "!", "Pass");
        assertFalse(check);
    }

    public boolean register(EditText[] texts, String returns, String pass) {
        texts[0] = value0;
        texts[1] = value1;
        texts[2] = value2;

        Mockito.when(value0.getText()).thenReturn(e00);
        Mockito.when(value1.getText()).thenReturn(e01);
        Mockito.when(value2.getText()).thenReturn(e02);
        Mockito.when(password.getText()).thenReturn(passwordEditable);

        Mockito.when(e00.toString()).thenReturn("Hello");
        Mockito.when(e01.toString()).thenReturn("World");
        Mockito.when(e02.toString()).thenReturn(returns);
        Mockito.when(passwordEditable.toString()).thenReturn(pass);

        return RegisterViewModel.checkFields(texts, password);
    }
}